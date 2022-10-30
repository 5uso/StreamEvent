package suso.event_manage.state_handlers.primatica;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.LongArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import net.minecraft.command.argument.GameProfileArgumentType;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.LiteralTextContent;
import net.minecraft.text.MutableText;
import suso.event_manage.EventManager;
import suso.event_manage.state_handlers.StateCommands;
import suso.event_manage.util.CommandUtil;

public class PrimaticaIngameCommands implements StateCommands {
    @Override
    public void register(MinecraftServer server) {
        CommandDispatcher<ServerCommandSource> dispatcher = server.getCommandManager().getDispatcher();

        dispatcher.register(ADDTIME_CMD);

        CommandUtil.sendCommandUpdate(server);
    }

    @Override
    public void unregister(MinecraftServer server) {
        CommandUtil.disableCommand(server, "addtime");

        CommandUtil.sendCommandUpdate(server);
    }

    // /addtime
    private static final LiteralArgumentBuilder<ServerCommandSource> ADDTIME_CMD = LiteralArgumentBuilder.literal("addtime");
    static {
        RequiredArgumentBuilder<ServerCommandSource, Long> time = CommandManager.argument("time", LongArgumentType.longArg());
        time.executes(context -> {
            if(EventManager.getInstance().getStateHandler() instanceof PrimaticaIngameHandler h) {
                long t = LongArgumentType.getLong(context, "time") * 1000;
                h.increaseDuration(t);
                return (int) t;
            }
            return 0;
        });
        ADDTIME_CMD.then(time);
    }
}
