package suso.event_manage.state_handlers;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import suso.event_manage.EventManager;
import suso.event_manage.data.EventPlayerData;
import suso.event_manage.state_handlers.commands.StateCommands;

public interface StateHandler {
    void tick(EventManager manager, MinecraftServer server);
    void tickPlayer(EventManager manager, MinecraftServer server, ServerPlayerEntity player, EventPlayerData data);
    void onPlayerJoin(EventManager manager, MinecraftServer server, ServerPlayerEntity player, EventPlayerData data);
    void cleanup(EventManager manager, MinecraftServer server);
    StateCommands getStateCommands();
    EventManager.ServerState getState();
}
