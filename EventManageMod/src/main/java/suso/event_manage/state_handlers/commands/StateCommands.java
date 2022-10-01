package suso.event_manage.state_handlers.commands;

import net.minecraft.server.MinecraftServer;

public interface StateCommands {
    void register(MinecraftServer server);
    void unregister(MinecraftServer server);
}
