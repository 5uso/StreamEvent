package suso.event_manage.state_handlers;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import suso.event_manage.EventManager;
import suso.event_manage.data.EventPlayerData;
import suso.event_manage.state_handlers.commands.IdleCommands;
import suso.event_manage.state_handlers.commands.PrimaticaIngameCommands;
import suso.event_manage.state_handlers.commands.StateCommands;

public class IdleHandler implements StateHandler {
    @Override
    public void tick(EventManager manager, MinecraftServer server) {

    }

    @Override
    public void tickPlayer(EventManager manager, MinecraftServer server, ServerPlayerEntity player, EventPlayerData data) {

    }

    @Override
    public void onPlayerJoin(EventManager manager, MinecraftServer server, ServerPlayerEntity player, EventPlayerData data) {

    }

    @Override
    public void cleanup(EventManager manager, MinecraftServer server) {

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
