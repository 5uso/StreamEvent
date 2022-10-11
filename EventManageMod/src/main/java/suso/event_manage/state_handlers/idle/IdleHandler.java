package suso.event_manage.state_handlers.idle;

import net.minecraft.entity.damage.DamageSource;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameMode;
import suso.event_manage.EventManager;
import suso.event_manage.data.EventPlayerData;
import suso.event_manage.state_handlers.StateHandler;
import suso.event_manage.state_handlers.StateCommands;
import suso.event_manage.util.InventoryUtil;

public class IdleHandler implements StateHandler {
    @Override
    public void tick(EventManager manager, MinecraftServer server) {

    }

    @Override
    public void tickPlayer(EventManager manager, MinecraftServer server, ServerPlayerEntity player, EventPlayerData data) {
        player.getHungerManager().add(20, 0.0f);
    }

    @Override
    public void onPlayerJoin(EventManager manager, MinecraftServer server, ServerPlayerEntity player, EventPlayerData data) {
        InventoryUtil.clearPLayer(player);
        player.clearStatusEffects();

        if(server.getPlayerManager().isOperator(player.getGameProfile())) {
            player.changeGameMode(GameMode.CREATIVE);
        } else {
            player.changeGameMode(GameMode.ADVENTURE);
        }
    }

    @Override
    public void onPlayerRespawn(EventManager manager, MinecraftServer server, ServerPlayerEntity player, EventPlayerData data) {

    }

    @Override
    public boolean onPlayerDeath(EventManager manager, MinecraftServer server, ServerPlayerEntity player, EventPlayerData data, DamageSource damageSource, float damageAmount) {
        player.setHealth(20.0f);
        return false;
    }

    @Override
    public void onPlayerItemUsedOnBlock(EventManager manager, MinecraftServer server, ServerPlayerEntity player, EventPlayerData data, BlockPos pos, ItemStack stack, Hand hand) {

    }

    @Override
    public boolean onPlayerRightClick(EventManager manager, MinecraftServer server, ServerPlayerEntity player, EventPlayerData data, ItemStack stack, Hand hand) {
        return false;
    }

    @Override
    public boolean onPlayerLand(EventManager manager, MinecraftServer server, ServerPlayerEntity player, EventPlayerData data) {
        return false;
    }

    @Override
    public void cleanup(EventManager manager, MinecraftServer server) {

    }

    @Override
    public boolean canDropItems(EventManager manager, ServerPlayerEntity player, EventPlayerData data) {
        return true; //TODO: Please change this
    }

    @Override
    public StateCommands getStateCommands() {
        return new IdleCommands();
    }

    @Override
    public EventManager.ServerState getState() {
       return EventManager.ServerState.IDLE;
    }
}
