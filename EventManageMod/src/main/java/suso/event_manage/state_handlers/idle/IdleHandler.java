package suso.event_manage.state_handlers.idle;

import net.minecraft.entity.Entity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameMode;
import suso.event_common.EventConstants;
import suso.event_manage.EventManager;
import suso.event_manage.data.EventPlayerData;
import suso.event_manage.state_handlers.StateHandler;
import suso.event_manage.state_handlers.StateCommands;
import suso.event_manage.util.InventoryUtil;

public class IdleHandler implements StateHandler {
    @Override
    public void tick() {

    }

    @Override
    public void tickPlayer(ServerPlayerEntity player, EventPlayerData data) {
        player.getHungerManager().add(20, 0.0f);
    }

    @Override
    public void onPlayerJoin(ServerPlayerEntity player, EventPlayerData data) {
        MinecraftServer server = EventManager.getInstance().getServer();

        InventoryUtil.clearPLayer(player);
        player.clearStatusEffects();

        if(server.getPlayerManager().isOperator(player.getGameProfile())) {
            player.changeGameMode(GameMode.CREATIVE);
        } else {
            player.changeGameMode(GameMode.ADVENTURE);
        }
    }

    @Override
    public void onPlayerRespawn(ServerPlayerEntity player, EventPlayerData data) {

    }

    @Override
    public boolean onPlayerDeath(ServerPlayerEntity player, EventPlayerData data, DamageSource damageSource, float damageAmount) {
        player.setHealth(20.0f);
        return false;
    }

    @Override
    public void onPlayerItemUsedOnBlock(ServerPlayerEntity player, EventPlayerData data, BlockPos pos, ItemStack stack, Hand hand) {

    }

    @Override
    public boolean onPlayerRightClick(ServerPlayerEntity player, EventPlayerData data, ItemStack stack, Hand hand) {
        return false;
    }

    @Override
    public boolean onPlayerPlacedBlock(ServerPlayerEntity player, EventPlayerData data, ItemPlacementContext context) {
        return false;
    }

    @Override
    public boolean onPlayerLand(ServerPlayerEntity player, EventPlayerData data, double xDifference, double heightDifference, double zDifference, BlockPos landingPos) {
        return false;
    }

    @Override
    public void onPlayerJump(ServerPlayerEntity player, EventPlayerData data, BlockPos jumpingPos) {

    }

    @Override
    public boolean onPlayerShoot(ServerPlayerEntity player, EventPlayerData data, ItemStack bow, int useTicks) {
        return false;
    }

    @Override
    public void onPlayerKill(ServerPlayerEntity player, Entity victim, DamageSource source) {

    }

    @Override
    public void prepare() {

    }

    @Override
    public void cleanup() {

    }

    @Override
    public boolean canDropItems(ServerPlayerEntity player, EventPlayerData data) {
        return true; //TODO: Please change this
    }

    @Override
    public StateCommands getStateCommands() {
        return new IdleCommands();
    }

    @Override
    public EventConstants.States getState() {
       return EventConstants.States.IDLE;
    }
}
