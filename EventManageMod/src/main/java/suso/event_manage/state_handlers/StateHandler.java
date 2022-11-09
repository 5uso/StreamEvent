package suso.event_manage.state_handlers;

import net.minecraft.entity.Entity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import suso.event_manage.EvtBaseConstants;
import suso.event_manage.data.EventPlayerData;

public interface StateHandler {
    void tick();
    void tickPlayer(ServerPlayerEntity player, EventPlayerData data);
    void onPlayerJoin(ServerPlayerEntity player, EventPlayerData data);
    void onPlayerRespawn(ServerPlayerEntity player, EventPlayerData data);
    boolean onPlayerDeath(ServerPlayerEntity player, EventPlayerData data, DamageSource damageSource, float damageAmount);
    void onPlayerItemUsedOnBlock(ServerPlayerEntity player, EventPlayerData data, BlockPos pos, ItemStack stack, Hand hand);
    boolean onPlayerRightClick(ServerPlayerEntity player, EventPlayerData data, ItemStack stack, Hand hand);
    boolean onPlayerPlacedBlock(ServerPlayerEntity player, EventPlayerData data, ItemPlacementContext context);
    boolean onPlayerLand(ServerPlayerEntity player, EventPlayerData data, double heightDifference, BlockPos landingPos);
    void onPlayerJump(ServerPlayerEntity player, EventPlayerData data, BlockPos jumpingPos);
    boolean onPlayerShoot(ServerPlayerEntity player, EventPlayerData data, ItemStack bow, int useTicks);
    void onPlayerKill(ServerPlayerEntity player, Entity victim, DamageSource source);
    void cleanup();
    boolean canDropItems(ServerPlayerEntity player, EventPlayerData data);
    StateCommands getStateCommands();
    EvtBaseConstants.States getState();
}
