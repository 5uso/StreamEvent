package suso.event_manage.state_handlers.primatica;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.scoreboard.AbstractTeam;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.GameMode;
import net.minecraft.world.World;
import suso.event_manage.EventManager;
import suso.event_manage.data.EventData;
import suso.event_manage.data.EventPlayerData;
import suso.event_manage.state_handlers.StateCommands;
import suso.event_manage.state_handlers.StateHandler;
import suso.event_manage.state_handlers.TickableInstance;
import suso.event_manage.util.InventoryUtil;
import suso.event_manage.util.RndSet;
import suso.event_manage.util.ShaderUtil;
import suso.event_manage.util.SoundUtil;

import java.util.*;

public class PrimaticaIngameHandler implements StateHandler {
    private long durationMillis;
    private final long startMillis;
    private long leftMillis;

    protected int powerupAmount;
    private static final int powerupTarget = 15;
    private long nextPowerupMillis;

    protected final Set<Vec3d> orbLocations;
    private int orbTarget;
    private final Map<AbstractTeam, Integer> scores;

    private final List<TickableInstance> tickables;

    private final Map<UUID, PrimaticaPlayerInfo> playerInfo;

    private final Random r = new Random();

    public PrimaticaIngameHandler(long durationMillis) {
        this.durationMillis = durationMillis;
        this.startMillis = System.currentTimeMillis();
        this.powerupAmount = 0;

        this.orbLocations = new HashSet<>();
        this.orbTarget = 10;
        this.scores = new HashMap<>();

        this.tickables = new LinkedList<>();

        this.playerInfo = new HashMap<>();

        prepare();
    }

    private void killEntities(MinecraftServer server) {
        List<? extends Entity> snowballList = server.getOverworld().getEntitiesByType(EntityType.SNOWBALL, e -> true);
        snowballList.forEach(Entity::kill);

        List<? extends Entity> armorStandList = server.getOverworld().getEntitiesByType(EntityType.ARMOR_STAND, e -> e.getScoreboardTags().contains("primatica_powerup"));
        armorStandList.forEach(Entity::kill);
    }

    private void prepare() {
        EventManager manager = EventManager.getInstance();
        MinecraftServer server = manager.getServer();

        killEntities(server);

        EventData edata = EventData.getInstance();
        server.getPlayerManager().getPlayerList().forEach(player -> onPlayerJoin(EventManager.getInstance(), server, player, Objects.requireNonNull(edata.getPlayerData(player))));
    }

    private void trySummonOrb(EventManager manager, World world) {
        RndSet<Vec3d> possibleOrbSpots = PrimaticaInfo.getOrbLocations();
        for(PlayerEntity player : world.getPlayers()) {
            if(manager.isEventPlayer((ServerPlayerEntity) player)) {
                possibleOrbSpots.removeIf(pos -> pos.distanceTo(player.getPos()) < 20.0);
            }
        }

        Vec3d pos = possibleOrbSpots.getRandom();
        if(!orbLocations.contains(pos)) tickables.add(new PrimaticaOrbInstance(world, pos, this));
    }

    private void trySummonPowerup(World world) {
        RndSet<Vec3d> possiblePowerupSpots = PrimaticaInfo.getPowerupLocations();

        Vec3d pos = possiblePowerupSpots.getRandom();
        List<ArmorStandEntity> other = world.getEntitiesByClass(ArmorStandEntity.class, Box.of(pos, 0.1, 0.1, 0.1), e -> e.getScoreboardTags().contains("primatica_powerup"));
        if(other.isEmpty()) {
            int possible = PrimaticaInfo.Powerups.values().length;
            PrimaticaInfo.Powerups type = PrimaticaInfo.Powerups.values()[r.nextInt(possible)];
            tickables.add(new PrimaticaPowerupInstance(world, pos, r.nextFloat(0.0f, 360.0f), type, this));
        }

        nextPowerupMillis = System.currentTimeMillis() + r.nextLong(5000, 15000);
    }

    public int getTeamScore(AbstractTeam team) {
        return scores.getOrDefault(team, 0);
    }

    public void setTeamScore(AbstractTeam team, int score) {
        if(team != null) scores.put(team, score);
    }

    public void increaseDuration(long millis) {
        durationMillis += millis;
    }

    public void setHasPowerup(UUID id, boolean value) {
        playerInfo.get(id).hasPowerup = value;
    }

    public boolean getHasPowerup(UUID id) {
        return playerInfo.get(id).hasPowerup;
    }

    protected void useAgility(ServerPlayerEntity player) {
        player.addStatusEffect(new StatusEffectInstance(StatusEffects.SPEED, 200, 0, false, false, true));
        player.addStatusEffect(new StatusEffectInstance(StatusEffects.JUMP_BOOST, 200, 4, false, false, true));

        setHasPowerup(player.getUuid(), false);
    }

    protected void useBridge(ServerPlayerEntity player) {
        tickables.add(new PrimaticaBridgeInstance(player));
        setHasPowerup(player.getUuid(), false);
    }

    protected void useGravity(ServerPlayerEntity player) {
        tickables.add(new PrimaticaGravityInstance(player));
        setHasPowerup(player.getUuid(), false);
    }

    private void initPlayer(ServerPlayerEntity player, EventPlayerData data) {
        InventoryUtil.clearPLayer(player);
        player.clearStatusEffects();

        if(data.isPlayer) {
            player.changeGameMode(GameMode.ADVENTURE);

            InventoryUtil.replaceSlot(player, 0, ItemStack.fromNbt(PrimaticaInfo.SWORD));
            InventoryUtil.replaceSlot(player, 1, ItemStack.fromNbt(PrimaticaInfo.BOW));
            InventoryUtil.replaceSlot(player, 2, ItemStack.fromNbt(PrimaticaInfo.PICKAXE));
            InventoryUtil.replaceSlot(player, 3, ItemStack.fromNbt(PrimaticaInfo.BLOCK));

            InventoryUtil.replaceSlot(player, 9, new ItemStack(Registry.ITEM.get(new Identifier("minecraft:arrow"))));

            InventoryUtil.replaceSlot(player, 103, ItemStack.fromNbt(PrimaticaInfo.HELMET));
            InventoryUtil.replaceSlot(player, 102, ItemStack.fromNbt(PrimaticaInfo.CHESTPLATE));
            InventoryUtil.replaceSlot(player, 101, ItemStack.fromNbt(PrimaticaInfo.LEGGINGS));
            InventoryUtil.replaceSlot(player, 100, ItemStack.fromNbt(PrimaticaInfo.BOOTS));

            player.addStatusEffect(new StatusEffectInstance(StatusEffects.INSTANT_HEALTH, 1, 100, false, false, false));

            setHasPowerup(player.getUuid(), false);
        } else {
            player.changeGameMode(GameMode.SPECTATOR);
        }
    }

    @Override
    public void tick(EventManager manager, MinecraftServer server) {
        World w = server.getOverworld();
        long currTime = System.currentTimeMillis();

        leftMillis = (startMillis + durationMillis) - currTime;

        tickables.removeIf(TickableInstance::ifTickRemove);

        orbTarget =  2 + (int) Math.round(Math.max(0, leftMillis - 30000) / (double)Math.max(0, durationMillis - 30000) * 8);
        if(orbLocations.size() < orbTarget) trySummonOrb(manager, w);

        if(currTime >= nextPowerupMillis && powerupAmount < powerupTarget) trySummonPowerup(w);
    }

    @Override
    public void tickPlayer(EventManager manager, MinecraftServer server, ServerPlayerEntity player, EventPlayerData data) {
        PrimaticaPlayerInfo info = playerInfo.get(player.getUuid());

        ShaderUtil.setShaderUniform(player, "MinigameTimer", Math.max((int) leftMillis, 0));

        if(!manager.isEventPlayer(player) || player.isDead()) return;

        player.getHungerManager().add(20, 0.0f);
        if(player.getY() < 20.0) player.kill();

        if(player.getY() < 69.0 && !info.isUnderground) {
            SoundUtil.updateFadeVolume(player, new Identifier("eniah:music.1a_main"), 0.0f, 40);
            SoundUtil.updateFadeVolume(player, new Identifier("eniah:music.1a_loweq"), 0.0f, 40);
            SoundUtil.updateFadeVolume(player, new Identifier("eniah:music.1a_underground"), 1.0f, 40);
            SoundUtil.updateFadeVolume(player, new Identifier("eniah:music.1a_undergroundloweq"), 0.0f, 40);
            SoundUtil.updateFadeVolume(player, new Identifier("eniah:music.1a_skyline"), 0.0f, 40);
            info.isUnderground = true;
        }

        if(player.getY() > 69.0 && info.isUnderground) {
            SoundUtil.updateFadeVolume(player, new Identifier("eniah:music.1a_main"), 1.0f, 40);
            SoundUtil.updateFadeVolume(player, new Identifier("eniah:music.1a_loweq"), 0.0f, 40);
            SoundUtil.updateFadeVolume(player, new Identifier("eniah:music.1a_underground"), 0.0f, 40);
            SoundUtil.updateFadeVolume(player, new Identifier("eniah:music.1a_undergroundloweq"), 0.0f, 40);
            SoundUtil.updateFadeVolume(player, new Identifier("eniah:music.1a_skyline"), 0.0f, 40);
            info.isUnderground = false;
        }

        if(player.getY() > 112.0 && !info.isSkyline) {
            SoundUtil.updateFadeVolume(player, new Identifier("eniah:music.1a_main"), 0.0f, 40);
            SoundUtil.updateFadeVolume(player, new Identifier("eniah:music.1a_loweq"), 0.0f, 40);
            SoundUtil.updateFadeVolume(player, new Identifier("eniah:music.1a_underground"), 0.0f, 40);
            SoundUtil.updateFadeVolume(player, new Identifier("eniah:music.1a_undergroundloweq"), 0.0f, 40);
            SoundUtil.updateFadeVolume(player, new Identifier("eniah:music.1a_skyline"), 1.0f, 40);
            info.isSkyline = true;
        }

        if(player.getY() < 112.0 && info.isSkyline) {
            SoundUtil.updateFadeVolume(player, new Identifier("eniah:music.1a_main"), 1.0f, 40);
            SoundUtil.updateFadeVolume(player, new Identifier("eniah:music.1a_loweq"), 0.0f, 40);
            SoundUtil.updateFadeVolume(player, new Identifier("eniah:music.1a_underground"), 0.0f, 40);
            SoundUtil.updateFadeVolume(player, new Identifier("eniah:music.1a_undergroundloweq"), 0.0f, 40);
            SoundUtil.updateFadeVolume(player, new Identifier("eniah:music.1a_skyline"), 0.0f, 40);
            info.isSkyline = false;
        }
    }

    @Override
    public void onPlayerJoin(EventManager manager, MinecraftServer server, ServerPlayerEntity player, EventPlayerData data) {
        if(playerInfo.get(player.getUuid()) == null) playerInfo.put(player.getUuid(), new PrimaticaPlayerInfo());

        SoundUtil.playFadeSound(player, new Identifier("eniah:music.1a_main"), 1.0f, 1.0f, true);
        SoundUtil.playFadeSound(player, new Identifier("eniah:music.1a_loweq"), 0.0f, 1.0f, true);
        SoundUtil.playFadeSound(player, new Identifier("eniah:music.1a_underground"), 0.0f, 1.0f, true);
        SoundUtil.playFadeSound(player, new Identifier("eniah:music.1a_undergroundloweq"), 0.0f, 1.0f, true);
        SoundUtil.playFadeSound(player, new Identifier("eniah:music.1a_skyline"), 0.0f, 1.0f, true);

        initPlayer(player, data);
    }

    @Override
    public void onPlayerRespawn(EventManager manager, MinecraftServer server, ServerPlayerEntity player, EventPlayerData data) {

    }

    @Override
    public boolean onPlayerDeath(EventManager manager, MinecraftServer server, ServerPlayerEntity player, EventPlayerData data, DamageSource damageSource, float damageAmount) {
        player.setSpawnPoint(server.getOverworld().getRegistryKey(), new BlockPos(216.0, 80.00, -14.0), 30.0f, true, false);
        return true;
    }

    @Override
    public void onPlayerItemUsedOnBlock(EventManager manager, MinecraftServer server, ServerPlayerEntity player, EventPlayerData data, BlockPos pos, ItemStack stack, Hand hand) {
        if(stack.itemMatches(i -> i.matchesId(new Identifier("minecraft:light_blue_concrete")))) {
            InventoryUtil.replaceSlot(player, hand == Hand.MAIN_HAND ? player.getInventory().selectedSlot : 99, ItemStack.fromNbt(PrimaticaInfo.BLOCK));
        }
    }

    @Override
    public boolean onPlayerRightClick(EventManager manager, MinecraftServer server, ServerPlayerEntity player, EventPlayerData data, ItemStack stack, Hand hand) {
        if(stack.itemMatches(r -> r.matchesId(new Identifier("minecraft:feather"))) && stack.getNbt() != null) {
            int powerupId = stack.getNbt().getInt("CustomModelData");
            switch (powerupId) {
                case 1 -> useAgility(player);
                case 2 -> useBridge(player);
                case 3 -> useGravity(player);
            }
            InventoryUtil.replaceSlot(player, hand == Hand.MAIN_HAND ? player.getInventory().selectedSlot : 99, ItemStack.EMPTY);
            return true;
        }
        return false;
    }

    @Override
    public void cleanup(EventManager manager, MinecraftServer server) {
        killEntities(server);

        for(ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {
            InventoryUtil.clearPLayer(player);
            player.clearStatusEffects();
        }
    }

    @Override
    public boolean canDropItems(EventManager manager, ServerPlayerEntity player, EventPlayerData data) {
        return false;
    }

    @Override
    public StateCommands getStateCommands() {
        return new PrimaticaIngameCommands();
    }

    @Override
    public EventManager.ServerState getState() {
        return EventManager.ServerState.PRIMATICA_INGAME;
    }
}
