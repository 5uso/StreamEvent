package suso.event_manage.state_handlers;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.thrown.SnowballEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.DustParticleEffect;
import net.minecraft.particle.ItemStackParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.scoreboard.AbstractTeam;
import net.minecraft.scoreboard.Team;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3f;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.GameMode;
import net.minecraft.world.World;
import suso.event_manage.EventManager;
import suso.event_manage.data.EventData;
import suso.event_manage.data.EventPlayerData;
import suso.event_manage.game_info.PrimaticaInfo;
import suso.event_manage.state_handlers.commands.PrimaticaIngameCommands;
import suso.event_manage.state_handlers.commands.StateCommands;
import suso.event_manage.util.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PrimaticaIngameHandler implements StateHandler {
    private long durationMillis;
    private final long startMillis;
    private long leftMillis;

    private final Map<Vec3d, SnowballEntity> orbs;
    private int orbTarget;

    private final Map<AbstractTeam, Integer> scores;

    public PrimaticaIngameHandler(long durationMillis) {
        this.durationMillis = durationMillis;
        this.startMillis = System.currentTimeMillis();

        this.orbs = new HashMap<>();
        this.orbTarget = 3;

        this.scores = new HashMap<>();

        prepare();
    }

    private void prepare() {
        EventManager manager = EventManager.getInstance();
        MinecraftServer server = manager.getServer();
        List<? extends Entity> snowballList = server.getOverworld().getEntitiesByType(EntityType.SNOWBALL, e -> true);
        snowballList.forEach(Entity::kill);

        EventData edata = EventData.getInstance();
        server.getPlayerManager().getPlayerList().forEach(player -> initPlayer(manager, server, player, edata.getPlayerData(player)));
    }

    private void summonOrb(World world, Vec3d pos) {
        SnowballEntity e = new SnowballEntity(world, pos.x, pos.y, pos.z);

        e.setNoGravity(true);
        e.setGlowing(true);
        e.addScoreboardTag("primatica_objective");

        orbs.put(pos, e);
        world.spawnEntity(e);
    }

    private void trySummonOrb(EventManager manager, World world) {
        RndSet<Vec3d> possibleOrbSpots = PrimaticaInfo.getOrbLocations();
        for(PlayerEntity player : world.getPlayers()) {
            if(manager.isEventPlayer((ServerPlayerEntity) player)) {
                possibleOrbSpots.removeIf(pos -> pos.distanceTo(player.getPos()) < 20.0);
            }
        }

        Vec3d pos = possibleOrbSpots.getRandom();
        if(!orbs.containsKey(pos)) summonOrb(world, pos);
    }

    private void collectOrb(MinecraftServer server, ServerPlayerEntity player, SnowballEntity orb) {
        orb.kill();

        server.getOverworld().spawnParticles(new ItemStackParticleEffect(ParticleTypes.ITEM, new ItemStack(Registry.ITEM.get(new Identifier("minecraft:snowball")))), orb.getX(), orb.getY() + 0.3, orb.getZ(), 100, 0.0, 0.0, 0.0, 1.0);

        SoundUtil.playSound(player, new Identifier("minecraft:entity.player.levelup"), SoundCategory.MASTER, orb.getPos(), 1.0f, 2.0f);
        SoundUtil.playSound(server.getPlayerManager().getPlayerList(), new Identifier("minecraft:item.totem.use"), SoundCategory.MASTER, orb.getPos(), 0.5f, 2.0f);

        AbstractTeam team = player.getScoreboardTeam();
        int score = scores.getOrDefault(team, 0) + 1;
        scores.put(team, score);
        System.out.println(team == null ? "None" : team.getName() + ": " + score);

        server.getOverworld().spawnParticles(new DustParticleEffect(ParticleUtil.teamColor(team), 2.0f), player.getX(), player.getY() + 0.3, player.getZ(), 20, 1.0, 1.0, 1.0, 2.0);
    }

    private boolean tickOrb(MinecraftServer server, SnowballEntity orb) {
        if(orb.isRemoved()) return true;

        ServerWorld w = server.getOverworld();
        w.spawnParticles(new DustParticleEffect(new Vec3f(1.0f, 1.0f, 1.0f), 1.0f), orb.getX(), orb.getY() + 0.3, orb.getZ(), 1, 0.2, 0.2, 0.2, 0.0);

        PlayerEntity player = w.getClosestPlayer(orb, 20.0);
        if(player instanceof ServerPlayerEntity sPlayer) {
            server.getScoreboard().addPlayerToTeam(orb.getUuidAsString(), (Team) player.getScoreboardTeam());

            if(player.getBoundingBox().intersects(orb.getBoundingBox().expand(0.1))) {
                collectOrb(server, sPlayer, orb);
                return true;
            }
        } else {
            server.getScoreboard().clearPlayerTeam(orb.getUuidAsString());
        }

        return false;
    }

    public void increaseDuration(long millis) {
        durationMillis += millis;
    }

    private void useAgility(MinecraftServer server, ServerPlayerEntity player) {
        player.addStatusEffect(new StatusEffectInstance(StatusEffects.SPEED, 10, 0, false, false, true));
        player.addStatusEffect(new StatusEffectInstance(StatusEffects.JUMP_BOOST, 10, 4, false, false, true));
    }

    private void initPlayer(EventManager manager, MinecraftServer server, ServerPlayerEntity player, EventPlayerData data) {
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
        } else {
            player.changeGameMode(GameMode.SPECTATOR);
        }
    }

    @Override
    public void tick(EventManager manager, MinecraftServer server) {
        leftMillis = (startMillis + durationMillis) - System.currentTimeMillis();

        orbs.entrySet().removeIf(orb -> tickOrb(server, orb.getValue()));
        if(orbs.size() < orbTarget) trySummonOrb(manager, server.getOverworld());
    }

    @Override
    public void tickPlayer(EventManager manager, MinecraftServer server, ServerPlayerEntity player, EventPlayerData data) {
        ShaderUtil.setShaderUniform(player, "MinigameTimer", (int)leftMillis);

        if(!manager.isEventPlayer(player) || player.isDead()) return;

        player.getHungerManager().add(20, 0.0f);
    }

    @Override
    public void onPlayerJoin(EventManager manager, MinecraftServer server, ServerPlayerEntity player, EventPlayerData data) {
        SoundUtil.playFadeSound(player, new Identifier("suso:main"), 1.0f, 1.0f, true);

        initPlayer(manager, server, player, data);
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
    public void cleanup(EventManager manager, MinecraftServer server) {
        List<? extends Entity> snowballList = server.getOverworld().getEntitiesByType(EntityType.SNOWBALL, e -> true);
        snowballList.forEach(Entity::kill);

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
