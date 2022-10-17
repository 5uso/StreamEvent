package suso.event_manage;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.networking.v1.ServerLoginConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerLoginNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.Entity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.item.ItemStack;
import net.minecraft.server.*;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import org.slf4j.Logger;
import suso.event_manage.custom.blocks.CustomBlocks;
import suso.event_manage.custom.items.CustomItems;
import suso.event_manage.data.EventData;
import suso.event_manage.data.EventPlayerData;
import suso.event_manage.mixin.MinecraftServerAccess;
import suso.event_manage.mixin.PlayerManagerAccess;
import suso.event_manage.state_handlers.idle.IdleHandler;
import suso.event_manage.state_handlers.StateHandler;
import suso.event_manage.util.CommandUtil;
import suso.event_manage.util.MiscUtil;

import java.util.List;
import java.util.Set;

public class EventManager implements ModInitializer {
    public enum ServerState {
        IDLE, PRIMATICA_INGAME
    }

    private static EventManager instance;

    private MinecraftServer server;
    private EventData data;
    private StateHandler handler;

    private void onServerStart(MinecraftServer server) {
        this.server = server;
        this.setStateHandler(new IdleHandler());

        PlayerManager pm = server.getPlayerManager();
        Whitelist whitelist = pm.getWhitelist();
        whitelist.values().clear();
        pm.setWhitelistEnabled(true);

        OperatorList ops = ((PlayerManagerAccess) pm).getOps();
        ops.values().clear();

        data = EventData.getInstance();
        data.loadData(server);

        setGameRules(server);
    }

    private void onServerTick(MinecraftServer server) {
        PlayerManager pm = server.getPlayerManager();
        List<ServerPlayerEntity> players = pm.getPlayerList();

        handler.tick();
        for(ServerPlayerEntity player : players) handler.tickPlayer(player, data.getPlayerData(player));
    }

    public void onPlayerJoin(ServerPlayerEntity player) {
        if(!data.isPlayerRegistered(player)) {
            data.registerPlayer(player);
        }

        handler.onPlayerJoin(player, data.getPlayerData(player));
    }

    public void onPlayerRespawn(ServerPlayerEntity old, ServerPlayerEntity player, boolean alive) {
        handler.onPlayerRespawn(player, data.getPlayerData(player));
    }

    public boolean onPlayerDeath(ServerPlayerEntity player, DamageSource damageSource, float damageAmount) {
        return handler.onPlayerDeath(player, data.getPlayerData(player), damageSource, damageAmount);
    }

    public void onPlayerItemUsedOnBlock(ServerPlayerEntity player, BlockPos pos, ItemStack stack, Hand hand) {
        handler.onPlayerItemUsedOnBlock(player, data.getPlayerData(player), pos, stack, hand);
    }

    public boolean onPlayerRightClick(ServerPlayerEntity player, ItemStack stack, Hand hand) {
        return handler.onPlayerRightClick(player, data.getPlayerData(player), stack, hand);
    }

    public boolean onPlayerLand(ServerPlayerEntity player, double heightDifference, BlockPos landingPos) {
        return handler.onPlayerLand(player, data.getPlayerData(player), heightDifference, landingPos);
    }

    public void onPlayerJump(ServerPlayerEntity player, BlockPos jumpingPos) {
        handler.onPlayerJump(player, data.getPlayerData(player), jumpingPos);
    }
    }

    public void onSave() {
        Logger LOGGER = MinecraftServerAccess.getLOGGER();
        LOGGER.info("Saving event data...");

        data.saveData();

        LOGGER.info("Event data saved");
    }

    public void onEntityLoad(Entity entity, World world) {
        Set<String> tags = entity.getScoreboardTags();
        if(tags.contains("volatile_firstload")) entity.kill();
        else if(tags.contains("volatile")) tags.add("volatile_firstload");
    }

    public void cleanup(MinecraftServer server) {
        handler.cleanup();
    }

    public void setStateHandler(StateHandler handler) {
        Logger LOGGER = MinecraftServerAccess.getLOGGER();
        LOGGER.info("Changing state to " + handler.getState());
        if(this.handler != null) {
            this.handler.cleanup();
            this.handler.getStateCommands().unregister(server);
        }
        this.handler = handler;
        this.handler.getStateCommands().register(server);
    }

    private static void setGameRules(MinecraftServer server) {
        GameRules rules = server.getGameRules();
        rules.get(GameRules.DO_FIRE_TICK).set(false, server);
        rules.get(GameRules.DO_MOB_GRIEFING).set(false, server);
        rules.get(GameRules.KEEP_INVENTORY).set(true, server);
        rules.get(GameRules.DO_MOB_SPAWNING).set(false, server);
        rules.get(GameRules.DO_MOB_LOOT).set(false, server);
        rules.get(GameRules.DO_TILE_DROPS).set(false, server);
        rules.get(GameRules.DO_ENTITY_DROPS).set(false, server);
        rules.get(GameRules.COMMAND_BLOCK_OUTPUT).set(false, server);
        rules.get(GameRules.NATURAL_REGENERATION).set(true, server);
        rules.get(GameRules.DO_DAYLIGHT_CYCLE).set(false, server);
        rules.get(GameRules.LOG_ADMIN_COMMANDS).set(false, server);
        rules.get(GameRules.SHOW_DEATH_MESSAGES).set(true, server);
        rules.get(GameRules.RANDOM_TICK_SPEED).set(0, server);
        rules.get(GameRules.SEND_COMMAND_FEEDBACK).set(true, server);
        rules.get(GameRules.REDUCED_DEBUG_INFO).set(false, server);
        rules.get(GameRules.SPECTATORS_GENERATE_CHUNKS).set(true, server);
        rules.get(GameRules.SPAWN_RADIUS).set(0, server);
        rules.get(GameRules.DISABLE_ELYTRA_MOVEMENT_CHECK).set(true, server);
        rules.get(GameRules.MAX_ENTITY_CRAMMING).set(-1, server);
        rules.get(GameRules.DO_WEATHER_CYCLE).set(false, server);
        rules.get(GameRules.DO_LIMITED_CRAFTING).set(true, server);
        rules.get(GameRules.MAX_COMMAND_CHAIN_LENGTH).set(65536, server);
        rules.get(GameRules.ANNOUNCE_ADVANCEMENTS).set(false, server);
        rules.get(GameRules.DISABLE_RAIDS).set(true, server);
        rules.get(GameRules.DO_INSOMNIA).set(false, server);
        rules.get(GameRules.DO_IMMEDIATE_RESPAWN).set(false, server);
        rules.get(GameRules.DROWNING_DAMAGE).set(true, server);
        rules.get(GameRules.FALL_DAMAGE).set(false, server);
        rules.get(GameRules.FIRE_DAMAGE).set(true, server);
        rules.get(GameRules.FREEZE_DAMAGE).set(true, server);
        rules.get(GameRules.DO_PATROL_SPAWNING).set(false, server);
        rules.get(GameRules.DO_TRADER_SPAWNING).set(false, server);
        rules.get(GameRules.DO_WARDEN_SPAWNING).set(false, server);
        rules.get(GameRules.FORGIVE_DEAD_PLAYERS).set(true, server);
        rules.get(GameRules.UNIVERSAL_ANGER).set(false, server);
        rules.get(GameRules.PLAYERS_SLEEPING_PERCENTAGE).set(100, server);
    }

    public boolean isEventPlayer(ServerPlayerEntity player) {
        EventPlayerData pd = data.getPlayerData(player);
        if(pd == null) return false;
        return pd.isPlayer;
    }

    public boolean canDropItems(ServerPlayerEntity player) {
        return handler.canDropItems(player, data.getPlayerData(player));
    }

    public MinecraftServer getServer() {
        return server;
    }

    public static EventManager getInstance() {
        return instance;
    }

    @Override
    public void onInitialize() {
        instance = this;

        CustomBlocks.register();
        CustomItems.register();

        ServerLifecycleEvents.SERVER_STARTED.register(this::onServerStart);
        ServerTickEvents.END_SERVER_TICK.register(this::onServerTick);
        ServerLifecycleEvents.SERVER_STOPPING.register(this::cleanup);

        ServerLoginConnectionEvents.QUERY_START.register(ModCheck::handleConnection);
        ServerLoginNetworking.registerGlobalReceiver(EvtBaseConstants.LOGIN_CHECK, ModCheck::handleResponse);

        CommandRegistrationCallback.EVENT.register(CommandUtil::registerGlobalCommands);

        ServerPlayerEvents.AFTER_RESPAWN.register(this::onPlayerRespawn);
        ServerPlayerEvents.ALLOW_DEATH.register(this::onPlayerDeath);

        ServerEntityEvents.ENTITY_LOAD.register(this::onEntityLoad);

        ServerPlayNetworking.registerGlobalReceiver(EvtBaseConstants.JUMP_INPUT, MiscUtil::handleJumpInput);
    }
}
