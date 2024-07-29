package suso.event_manage.state_handlers.primatica;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.damage.DamageRecord;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtInt;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtString;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.s2c.play.EntityVelocityUpdateS2CPacket;
import net.minecraft.particle.DustParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKey;
import net.minecraft.scoreboard.AbstractTeam;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3f;
import net.minecraft.world.GameMode;
import org.jetbrains.annotations.Nullable;
import suso.event_manage.EventManager;
import suso.event_manage.EvtBaseConstants;
import suso.event_manage.ModCheck;
import suso.event_manage.data.EventData;
import suso.event_manage.data.EventPlayerData;
import suso.event_manage.state_handlers.ScheduleInstance;
import suso.event_manage.state_handlers.StateCommands;
import suso.event_manage.state_handlers.StateHandler;
import suso.event_manage.state_handlers.TickableInstance;
import suso.event_manage.state_handlers.idle.IdleHandler;
import suso.event_manage.util.*;

import java.util.*;

public class PrimaticaIngameHandler implements StateHandler {
    private long durationMillis;
    private final long startMillis;
    private long leftMillis;
    private boolean overtime;
    private boolean ended;
    private boolean stop;

    private static final int powerupTarget = 30;
    private long nextPowerupMillis;

    protected final Set<Vec3d> orbLocations;
    private int orbTarget;
    private final PrimaticaScore scores;

    private final List<TickableInstance> tickables;

    private final Map<UUID, PrimaticaPlayerInfo> playerInfo;

    private final Random r = new Random();

    public PrimaticaIngameHandler(long durationMillis) {
        this.durationMillis = durationMillis;
        this.startMillis = ModCheck.getTime();
        this.leftMillis = durationMillis;
        this.overtime = false;
        this.ended = false;
        this.stop = false;

        this.orbLocations = new HashSet<>();
        this.orbTarget = 10;
        this.scores = new PrimaticaScore();

        this.tickables = new LinkedList<>();

        this.playerInfo = new HashMap<>();
    }

    public void triggerEnd(MinecraftServer server) {
        ended = true;
        tickables.add(new ScheduleInstance(200, () -> stop = true));

        server.getPlayerManager().getPlayerList().forEach(p -> p.changeGameMode(GameMode.SPECTATOR));
    }

    private void trySummonOrb(EventManager manager, ServerWorld world) {
        RndSet<Vec3d> possibleOrbSpots = PrimaticaInfo.getOrbLocations();
        for(ServerPlayerEntity player : world.getPlayers()) {
            if(manager.isEventPlayer(player)) {
                possibleOrbSpots.removeIf(pos -> pos.distanceTo(player.getPos()) < 20.0);
            }
        }

        Vec3d pos = possibleOrbSpots.getRandom();
        if(pos == null) return;

        if(!orbLocations.contains(pos)) tickables.add(new PrimaticaOrbInstance(world, pos.add(new Vec3d(0.5, 0.125, 0.5)), this));
    }

    private void trySummonPowerup(ServerWorld world, int powerupAmount) {
        BlockPos pos = PrimaticaPowerupInstance.getPowerupPosition(world);
        if(pos == null) return;

        int possible = PrimaticaInfo.Powerups.values().length;
        PrimaticaInfo.Powerups type = PrimaticaInfo.Powerups.values()[r.nextInt(possible)];
        tickables.add(new PrimaticaPowerupInstance(world, pos, type, this));
        double delayFactor = (double) powerupAmount / powerupTarget;
        nextPowerupMillis = ModCheck.getTime() + r.nextLong(5, 6 + (long)(20000 * delayFactor*delayFactor*delayFactor));
    }

    public void updateClientScores(ServerPlayerEntity player) {
        HudUtil.setPrimaticaScore(player, scores);
    }

    public void score(AbstractTeam team, ServerPlayerEntity player) {
        scores.score(team, overtime ? 5 : 1);
        EventManager.getInstance().getServer().getPlayerManager().getPlayerList().forEach(this::updateClientScores);
        HudUtil.broadcastFeedMessage(Objects.isNull(player) ? EvtBaseConstants.NULL_UUID : player.getUuid(), new Identifier("minecraft:textures/block/sunflower_front.png"), null);
    }

    public void resetScores() {
        this.scores.reset();
    }

    public void updateClientTimer(ServerPlayerEntity player) {
        HudUtil.setTimer(player, startMillis + durationMillis);
    }

    public void increaseDuration(long millis) {
        if(leftMillis < 0) {
            durationMillis -= leftMillis;
            overtime = millis <= 0;
        }
        durationMillis += millis;

        EventManager.getInstance().getServer().getPlayerManager().getPlayerList().forEach(this::updateClientTimer);
    }

    private void triggerOvertime(ServerWorld world) {
        overtime = true;
        tickables.removeIf(t -> {
            if(t instanceof PrimaticaOrbInstance orb) {
                orb.vanish();
                return true;
            }

            return false;
        });

        RndSet<Vec3d> possibleOrbSpots = PrimaticaInfo.getFinalOrbLocations();
        for(ServerPlayerEntity player : world.getPlayers()) {
            if(EventManager.getInstance().isEventPlayer(player)) {
                possibleOrbSpots.removeIf(pos -> pos.distanceTo(player.getPos()) < 20.0);
            }
        }

        Vec3d pos = possibleOrbSpots.getRandom();
        if(pos == null) {
            do {
                pos = new Vec3d(r.nextGaussian() * 35, 174.0, r.nextGaussian() * 35);
            } while(pos.squaredDistanceTo(0.5, 174.0, 0.5) > 70.0);
        }

        tickables.add(new PrimaticaOrbInstance(world, pos.add(new Vec3d(0.5, 0.125, 0.5)), this));
    }

    public void setHasPowerup(UUID id, boolean value) {
        playerInfo.get(id).hasPowerup = value;
    }

    public PrimaticaPlayerInfo getPlayerInfo(UUID id) {
        return playerInfo.get(id);
    }

    protected boolean useAgility(ServerPlayerEntity player) {
        player.addStatusEffect(new StatusEffectInstance(StatusEffects.SPEED, 200, 0, false, false, true));
        player.addStatusEffect(new StatusEffectInstance(StatusEffects.JUMP_BOOST, 200, 4, false, false, true));

        PrimaticaPlayerInfo info = playerInfo.get(player.getUuid());
        info.setAgilityActive(true);

        tickables.add(new ScheduleInstance(200, () -> endAgility(player)));

        AbstractTeam team = player.getScoreboardTeam();
        Vec3f color = team == null ? Vec3f.ZERO : ParticleUtil.teamColor(team);
        player.getWorld().spawnParticles(ParticleTypes.ENTITY_EFFECT, player.getX(), player.getY(), player.getZ(), 50, color.getX(), color.getY(), color.getZ(), 0.0);

        SoundUtil.playFadeSound(player, new Identifier("minecraft:entity.elder_guardian.curse"), 0.5f, 0.5f, false, SoundCategory.PLAYERS, false);
        SoundUtil.playFadeSound(player, new Identifier("minecraft:item.trident.thunder"), 1.0f, 2.0f, false, SoundCategory.PLAYERS, true);
        setHasPowerup(player.getUuid(), false);
        return true;
    }

    protected void endAgility(ServerPlayerEntity player) {
        StatusEffectInstance status = player.getStatusEffect(StatusEffects.JUMP_BOOST);
        if(status != null && status.getDuration() > 2) return;

        PrimaticaPlayerInfo info = playerInfo.get(player.getUuid());
        info.setAgilityActive(false);

        SoundUtil.playFadeSound(player, new Identifier("minecraft:entity.splash_potion.break"), 1.0f, 0.5f, false, SoundCategory.PLAYERS, false);
        SoundUtil.playFadeSound(player, new Identifier("minecraft:block.beacon.deactivate"), 1.0f, 2.0f, false, SoundCategory.PLAYERS, true);
    }

    protected boolean useBridge(ServerPlayerEntity player) {
        if(!PrimaticaBridgeInstance.shouldSpawn(player)) return false;

        tickables.add(new PrimaticaBridgeInstance(player));
        setHasPowerup(player.getUuid(), false);
        return true;
    }

    protected boolean useGravity(ServerPlayerEntity player) {
        tickables.add(new PrimaticaGravityInstance(player, this));
        setHasPowerup(player.getUuid(), false);
        return true;
    }

    protected boolean useEMP(ServerPlayerEntity player) {
        HitResult hit = player.raycast(4.0, player.server.getTickTime(), true);
        if(hit instanceof BlockHitResult bhit) {
            BlockPos pos = bhit.getBlockPos().add(bhit.getSide().getVector());
            if(!player.world.getBlockState(pos).isAir()) return false;
            tickables.add(new PrimaticaEMPInstance(player, pos, this));
            setHasPowerup(player.getUuid(), false);
            return true;
        }
        return false;
    }

    protected boolean useGunk(ServerPlayerEntity player) {
        tickables.add(new PrimaticaGunkInstance(player));
        setHasPowerup(player.getUuid(), false);
        return true;
    }

    private boolean gunkMatchesTeam(String gunk, @Nullable AbstractTeam team) {
        if(team == null) return false;
        return PrimaticaInfo.getCorrespondingGunk(team.getColor().getColorIndex()).equals(gunk);
    }

    private void applyGunkJumpEffects(ServerPlayerEntity player, boolean ownTeam, double bounceHeight) {
        if(ownTeam) {
            Vec3d posDelta = player.getPosDelta();
            player.setVelocity(posDelta.x * 4.0, Math.max(1.5, bounceHeight), posDelta.z * 4.0);
            getPlayerInfo(player.getUuid()).gunkBounce = true;
            SoundUtil.playFadeSound(player, new Identifier("minecraft:entity.slime.jump"), 1.0f, 1.0f, false, SoundCategory.BLOCKS, true);
        } else player.setVelocity(0.0, 0.1, 0.0);

        player.networkHandler.sendPacket(new EntityVelocityUpdateS2CPacket(player));
    }

    private void initPlayer(ServerPlayerEntity player, EventPlayerData data) {
        InventoryUtil.clearPLayer(player);
        player.clearStatusEffects();

        if(data.isPlayer) {
            player.changeGameMode(GameMode.ADVENTURE);

            NbtCompound bowNbt = PrimaticaInfo.BOW.copy();
            NbtCompound blockNbt = PrimaticaInfo.BLOCK.copy();
            NbtCompound helmetNbt = PrimaticaInfo.HELMET.copy();
            NbtCompound chestplateNbt = PrimaticaInfo.CHESTPLATE.copy();
            NbtCompound leggingsNbt = PrimaticaInfo.LEGGINGS.copy();
            NbtCompound bootsNbt = PrimaticaInfo.BOOTS.copy();
            AbstractTeam team = player.getScoreboardTeam();
            if(team != null && team.getColor().getColorValue() != null) {
                bowNbt.getCompound("tag").put("CustomModelData", NbtInt.of(team.getColor().getColorIndex()));
                blockNbt.put("id", NbtString.of(PrimaticaInfo.getCorrespondingBlock(team.getColor().getColorIndex())));
                blockNbt.getCompound("tag").getList("CanPlaceOn", NbtList.STRING_TYPE).add(NbtString.of(PrimaticaInfo.getCorrespondingGunk(team.getColor().getColorIndex())));

                NbtCompound armorDisplay = new NbtCompound();
                armorDisplay.put("color", NbtInt.of(team.getColor().getColorValue()));
                helmetNbt.getCompound("tag").put("display", armorDisplay);
                chestplateNbt.getCompound("tag").put("display", armorDisplay);
                leggingsNbt.getCompound("tag").put("display", armorDisplay);
                bootsNbt.getCompound("tag").put("display", armorDisplay);
            }

            InventoryUtil.replaceSlot(player, 0, ItemStack.fromNbt(bowNbt));
            InventoryUtil.replaceSlot(player, 1, ItemStack.fromNbt(PrimaticaInfo.PICKAXE));

            InventoryUtil.replaceSlot(player, 2, ItemStack.fromNbt(blockNbt));

            InventoryUtil.replaceSlot(player, 9, new ItemStack(Registries.ITEM.get(new Identifier("minecraft:arrow"))));

            InventoryUtil.replaceSlot(player, 103, ItemStack.fromNbt(helmetNbt));
            InventoryUtil.replaceSlot(player, 102, ItemStack.fromNbt(chestplateNbt));
            InventoryUtil.replaceSlot(player, 101, ItemStack.fromNbt(leggingsNbt));
            InventoryUtil.replaceSlot(player, 100, ItemStack.fromNbt(bootsNbt));

            player.addStatusEffect(new StatusEffectInstance(StatusEffects.INSTANT_HEALTH, 1, 100, false, false, false));

            setHasPowerup(player.getUuid(), false);
            HudUtil.setInfo(player, null);

            PrimaticaPlayerInfo info = getPlayerInfo(player.getUuid());
            info.setAgilityActive(false);
            updateClientScores(player);
        } else {
            player.changeGameMode(GameMode.SPECTATOR);
        }
    }

    @Override
    public void tick() {
        EventManager manager = EventManager.getInstance();
        MinecraftServer server = manager.getServer();

        ServerWorld w = server.getOverworld();
        long currTime = ModCheck.getTime();

        leftMillis = (startMillis + durationMillis) - currTime;

        tickables.removeIf(TickableInstance::ifTickRemove);

        int powerupAmount = PrimaticaPowerupInstance.positions.size();
        if(currTime >= nextPowerupMillis && powerupAmount < powerupTarget) trySummonPowerup(w, powerupAmount);

        if(!overtime) {
            orbTarget =  2 + (int) Math.round(Math.max(0, leftMillis - 30000) / (double)Math.max(0, durationMillis - 30000) * 8);
            if(orbLocations.size() < orbTarget) trySummonOrb(manager, w);
            if(leftMillis <= 0) triggerOvertime(w);
        } else {
            if(!ended && orbLocations.size() == 0) triggerEnd(server);
        }

        if(stop) EventManager.getInstance().setStateHandler(new IdleHandler());
    }

    @Override
    public void tickPlayer(ServerPlayerEntity player, EventPlayerData data) {
        EventManager manager = EventManager.getInstance();
        PrimaticaPlayerInfo info = playerInfo.get(player.getUuid());

        if(!manager.isEventPlayer(player) || player.isDead()) return;

        player.getHungerManager().add(20, 0.0f);
        if(player.getY() < 20.0) player.kill();

        info.setUnderground(player.getY() < 69.0);
        info.setSkyline(player.getY() >= 112.0);
        info.setWithinEMP(info.withinEMPNow);
        info.withinEMPNow = false;
        info.setWithinGravity(info.withinGravityNow);
        info.withinGravityNow = false;

        float speed = ((float) player.getVelocity().length() - 0.5f) / 3.0f;
        speed = Math.min(Math.max(0.0f, speed), 2.0f);
        float heart = 1.0f - Math.min(player.getHealth(), 14.0f) / 14.0f;

        if(player.isDead()) {
            speed = 0.0f;
            heart = 0.0f;
        }

        SoundUtil.updateFadeVolume(player, new Identifier("suso:falling"), speed, 0);
        SoundUtil.updateFadeVolume(player, new Identifier("suso:heartbeat"), heart, 0);
        SoundUtil.updateFadePitch(player, new Identifier("suso:heartbeat"), heart + 0.8f, 0);
        SoundUtil.updateFadeVolume(player, new Identifier("suso:hologram"), info.hologramVolume, 0);
        SoundUtil.updateFadeVolume(player, new Identifier("suso:gravity"), info.gravityVolume, 0);

        info.hologramVolume = 0.0f;
        info.gravityVolume = 0.0f;

        if(info.isChargingBow && !player.isUsingItem()) {
            SoundUtil.stopSound(player, new Identifier("minecraft:item.crossbow.loading_middle"), null);
            SoundUtil.stopSound(player, new Identifier("suso:bow.charge"), null);
            SoundUtil.stopSound(player, new Identifier("suso:bow.loop"), null);
            info.isChargingBow = false;
        }

        if(info.agilityActive) {
            player.getWorld().spawnParticles(ParticleTypes.GLOW, player.getX(), player.getY() + 1.0, player.getZ(), 1, 0.3, 0.5, 0.3, 0.0);
        }

        if(player.isOnGround()) info.gunkBounce = false;
        if(info.gunkBounce) {
            AbstractTeam team = player.getScoreboardTeam();
            player.getWorld().spawnParticles(new DustParticleEffect(ParticleUtil.teamColor(team), 2), player.getX(), player.getY(), player.getZ(), Math.round(1.0f + speed), 0.3, 0.3, 0.3, 0.0);
        }
    }

    @Override
    public void onPlayerJoin(ServerPlayerEntity player, EventPlayerData data) {
        if(playerInfo.get(player.getUuid()) == null) playerInfo.put(player.getUuid(), new PrimaticaPlayerInfo(player));

        SoundUtil.stopSound(player, null, null);
        SoundUtil.playFadeSound(player, new Identifier("eniah:music.1b_main"), 1.0f, 1.0f, true, SoundCategory.RECORDS, false);
        SoundUtil.playFadeSound(player, new Identifier("eniah:music.1b_loweq"), 0.0f, 1.0f, true, SoundCategory.RECORDS, false);
        SoundUtil.playFadeSound(player, new Identifier("eniah:music.1b_underground"), 0.0f, 1.0f, true, SoundCategory.RECORDS, false);
        SoundUtil.playFadeSound(player, new Identifier("eniah:music.1b_undergroundloweq"), 0.0f, 1.0f, true, SoundCategory.RECORDS, false);
        SoundUtil.playFadeSound(player, new Identifier("eniah:music.1b_skyline"), 0.0f, 1.0f, true, SoundCategory.RECORDS, false);
        SoundUtil.playFadeSound(player, new Identifier("suso:falling"), 0.0f, 1.0f, true, SoundCategory.PLAYERS, false);
        SoundUtil.playFadeSound(player, new Identifier("suso:heartbeat"), 0.0f, 1.0f, true, SoundCategory.PLAYERS, false);
        SoundUtil.playFadeSound(player, new Identifier("suso:hologram"), 0.0f, 1.0f, true, SoundCategory.BLOCKS, false);
        SoundUtil.playFadeSound(player, new Identifier("suso:gravity"), 0.0f, 1.0f, true, SoundCategory.PLAYERS, true);

        ShaderUtil.setPostShader(player, new Identifier("suso:shaders/post/main.json"));
        updateClientTimer(player);

        initPlayer(player, data);
    }

    @Override
    public void onPlayerRespawn(ServerPlayerEntity player, EventPlayerData data) {
        playerInfo.get(player.getUuid()).changePitch(1.0f, 0);
        ShaderUtil.setPostShader(player, new Identifier("suso:shaders/post/main.json"));
        initPlayer(player, data);
    }

    @Override
    public boolean onPlayerDeath(ServerPlayerEntity player, EventPlayerData data, DamageSource damageSource, float damageAmount) {
        MinecraftServer server = EventManager.getInstance().getServer();

        player.setSpawnPoint(server.getOverworld().getRegistryKey(), new BlockPos(0.5, 134.00, 87.5), -180.0f, true, false);

        PrimaticaPlayerInfo info = playerInfo.get(player.getUuid());
        info.changePitch(0.5f, 40);

        SoundUtil.playSound(player, new Identifier("eniah:sfx.crash"), SoundCategory.PLAYERS, player.getPos(), 1.0f, 1.0f);

        ShaderUtil.setPostShader(player, new Identifier("suso:shaders/post/glitched.json"));

        boolean displayed = false;
        DamageRecord src = player.getDamageTracker().getMostRecentDamage();
        if(src != null) {
            if(src.getAttacker() instanceof ServerPlayerEntity killer) {
                HudUtil.broadcastFeedMessage(killer.getUuid(), new Identifier("minecraft:textures/item/iron_sword.png"), player.getUuid());
                displayed = true;
            }
        }
        if(!displayed) {
            HudUtil.broadcastFeedMessage(player.getUuid(), new Identifier("minecraft:textures/block/poppy.png"), EvtBaseConstants.NULL_UUID);
        }

        return true;
    }

    @Override
    public void onPlayerItemUsedOnBlock(ServerPlayerEntity player, EventPlayerData data, BlockPos pos, ItemStack stack, Hand hand) {
    }

    @Override
    public boolean onPlayerRightClick(ServerPlayerEntity player, EventPlayerData data, ItemStack stack, Hand hand) {
        if(stack.isOf(Items.FEATHER) && stack.getNbt() != null) {
            int powerupId = stack.getNbt().getInt("CustomModelData");
            if(switch (powerupId) {
                case 1 -> useAgility(player);
                case 2 -> useBridge(player);
                case 3 -> useGravity(player);
                case 4 -> useEMP(player);
                case 5 -> useGunk(player);
                default -> false;
            }) {
                InventoryUtil.replaceSlot(player, hand == Hand.MAIN_HAND ? player.getInventory().selectedSlot : 99, ItemStack.EMPTY);
                player.swingHand(hand, true);
                HudUtil.setInfo(player, null);
            }
            return true;
        }

        if(stack.isOf(Items.BOW)) {
            PrimaticaPlayerInfo info = playerInfo.get(player.getUuid());
            info.isChargingBow = true;

            if(stack.getNbt() != null && stack.getNbt().getInt("CustomModelData") == 1) {
                SoundUtil.playSound(player, new Identifier("suso:bow.charge"), SoundCategory.PLAYERS, player.getPos(), 1.0f, 1.0f);
                SoundUtil.playFadeSound(player, new Identifier("suso:bow.loop"), 0.4f, 1.0f, true, SoundCategory.PLAYERS, true);
            } else {
                SoundUtil.playFadeSound(player,  new Identifier("minecraft:item.crossbow.loading_middle"), 1.0f, 1.0f, false, SoundCategory.PLAYERS, true);
            }
        }
        return false;
    }

    @Override
    public boolean onPlayerPlacedBlock(ServerPlayerEntity player, EventPlayerData data, ItemPlacementContext context) {
        final Box cube = new Box(context.getBlockPos());
        return PrimaticaEMPInstance.positions.entrySet().stream().anyMatch(p -> !player.isTeamPlayer(p.getValue().getScoreboardTeam()) && MiscUtil.distance(cube, p.getKey()) < 3.0);
    }

    @Override
    public boolean onPlayerLand(ServerPlayerEntity player, EventPlayerData data, double heightDifference, BlockPos landingPos) {
        if(player.fallDistance < 0.75) return false;

        BlockState landingBlock = player.getWorld().getBlockState(landingPos);
        Optional<RegistryKey<Block>> op = landingBlock.getRegistryEntry().getKey();
        if(op.isPresent()) {
            Identifier id = op.get().getValue();
            if(id.getPath().endsWith("_gunk") && !player.bypassesLandingEffects()) {
                boolean ownTeam = gunkMatchesTeam(id.toString(), player.getScoreboardTeam());
                if(player.isJumpPressed()) {
                    Vec3d posDelta = player.getPosDelta();
                    tickables.add(new ScheduleInstance(1, () -> applyGunkJumpEffects(player, ownTeam, posDelta.y * -1.3)));
                } else {
                    if(ownTeam) {
                        Vec3d posDelta = player.getPosDelta();
                        player.setVelocity(posDelta.x * 4.0, posDelta.y * -1.3, posDelta.z * 4.0);
                        getPlayerInfo(player.getUuid()).gunkBounce = true;
                        SoundUtil.playFadeSound(player, new Identifier("minecraft:entity.slime.jump"), 1.0f, 1.0f, false, SoundCategory.BLOCKS, true);
                    } else player.setVelocity(0.0, 0.1, 0.0);
                    player.networkHandler.sendPacket(new EntityVelocityUpdateS2CPacket(player));
                }
            }
        }

        return false;
    }

    @Override
    public void onPlayerJump(ServerPlayerEntity player, EventPlayerData data, BlockPos jumpingPos) {
        BlockState jumpingBlock = player.getWorld().getBlockState(jumpingPos);
        Optional<RegistryKey<Block>> op = jumpingBlock.getRegistryEntry().getKey();
        if(op.isPresent()) {
            Identifier id = op.get().getValue();
            if(id.getPath().endsWith("_gunk") && !player.bypassesLandingEffects()) {
                applyGunkJumpEffects(player, gunkMatchesTeam(id.toString(), player.getScoreboardTeam()), 0.0);
            }
        }
    }

    @Override
    public boolean onPlayerShoot(ServerPlayerEntity player, EventPlayerData data, ItemStack bow, int useTicks) {
        SoundUtil.stopSound(player, new Identifier("minecraft:item.crossbow.loading_middle"), null);

        if(bow.getNbt() == null || bow.getNbt().getInt("CustomModelData") != 1) return false;

        PrimaticaPlayerInfo info = playerInfo.get(player.getUuid());
        SoundUtil.stopSound(player, new Identifier("suso:bow.charge"), null);
        SoundUtil.stopSound(player, new Identifier("suso:bow.loop"), null);
        info.isChargingBow = false;

        if(useTicks < 20) return true;

        bow.decrement(1);
        if(bow.getCount() < 1) {
            bow = ItemStack.EMPTY;

            Inventory playerInventory = player.getInventory();
            int size = playerInventory.size();
            boolean hasMoreBows = false;
            for(int i = 0; i < size; i++) {
                if(playerInventory.getStack(i).isOf(Items.BOW)) {
                    hasMoreBows = true;
                    break;
                }
            }

            if(!hasMoreBows) {
                NbtCompound bowNbt = PrimaticaInfo.BOW.copy();
                AbstractTeam team = player.getScoreboardTeam();
                if(team != null && team.getColor().getColorValue() != null) {
                    bowNbt.getCompound("tag").put("CustomModelData", NbtInt.of(team.getColor().getColorIndex()));
                }
                bow = ItemStack.fromNbt(bowNbt);
                setHasPowerup(player.getUuid(), false);
                HudUtil.setInfo(player, null);
            }
        }
        InventoryUtil.replaceSlot(player, player.getActiveHand() == Hand.MAIN_HAND ? player.getInventory().selectedSlot : 99, bow);

        tickables.add(new PrimaticaArrowInstance(player));

        return true;
    }

    @Override
    public void onPlayerKill(ServerPlayerEntity player, Entity victim, DamageSource source) {
        if(victim instanceof ServerPlayerEntity v) {
            //SoundUtil.playSound(player, new Identifier("eniah:sfx.fall"), SoundCategory.PLAYERS, player.getPos(), 1.0f, 1.0f);
            //HudUtil.sendKill(player, v);
        }

        SoundUtil.playSound(player, new Identifier("eniah:sfx.fall"), SoundCategory.PLAYERS, player.getPos(), 1.0f, 1.0f);

        PacketByteBuf p = PacketByteBufs.create();
        p.writeInt(HudUtil.DataTypes.KILL.ordinal());

        p.writeString(victim.getName().getString());
        p.writeInt(victim.getScoreboardTeam() == null ? Formatting.WHITE.ordinal() : victim.getScoreboardTeam().getColor().ordinal());

        ServerPlayNetworking.send(player, EvtBaseConstants.HUD_DATA, p);
    }

    @Override
    public void prepare() {
        MinecraftServer server = EventManager.getInstance().getServer();

        EventData edata = EventData.getInstance();
        server.getPlayerManager().getPlayerList().forEach(player -> onPlayerJoin(player, Objects.requireNonNull(edata.getPlayerData(player))));
    }

    @Override
    public void cleanup() {
        MinecraftServer server = EventManager.getInstance().getServer();

        tickables.removeIf(t -> {
            t.remove();
            return true;
        });

        for(ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {
            InventoryUtil.clearPLayer(player);
            player.clearStatusEffects();

            ShaderUtil.setPostShader(player, new Identifier("none"));
            SoundUtil.stopSound(player, null, null);
        }
    }

    @Override
    public boolean canDropItems(ServerPlayerEntity player, EventPlayerData data) {
        return false;
    }

    @Override
    public StateCommands getStateCommands() {
        return new PrimaticaIngameCommands();
    }

    @Override
    public EvtBaseConstants.States getState() {
        return EvtBaseConstants.States.PRIMATICA_INGAME;
    }
}
