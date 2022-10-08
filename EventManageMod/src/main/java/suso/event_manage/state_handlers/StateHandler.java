package suso.event_manage.state_handlers;

import net.minecraft.entity.damage.DamageSource;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import suso.event_manage.EventManager;
import suso.event_manage.data.EventPlayerData;

public interface StateHandler {
    void tick(EventManager manager, MinecraftServer server);
    void tickPlayer(EventManager manager, MinecraftServer server, ServerPlayerEntity player, EventPlayerData data);
    void onPlayerJoin(EventManager manager, MinecraftServer server, ServerPlayerEntity player, EventPlayerData data);
    void onPlayerRespawn(EventManager manager, MinecraftServer server, ServerPlayerEntity player, EventPlayerData data);
    boolean onPlayerDeath(EventManager manager, MinecraftServer server, ServerPlayerEntity player, EventPlayerData data, DamageSource damageSource, float damageAmount);
    void onPlayerItemUsedOnBlock(EventManager manager, MinecraftServer server, ServerPlayerEntity player, EventPlayerData data, BlockPos pos, ItemStack stack, Hand hand);
    void cleanup(EventManager manager, MinecraftServer server);
    boolean canDropItems(EventManager manager, ServerPlayerEntity player, EventPlayerData data);
    StateCommands getStateCommands();
    EventManager.ServerState getState();
}
