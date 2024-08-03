package suso.event_manage.state_handlers.primatica;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.LongArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import net.minecraft.scoreboard.Team;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import suso.event_manage.EventManager;
import suso.event_manage.state_handlers.StateCommands;
import suso.event_manage.util.CommandUtil;

import java.util.Random;

public class PrimaticaIngameCommands implements StateCommands {
    @Override
    public void register(MinecraftServer server) {
        CommandDispatcher<ServerCommandSource> dispatcher = server.getCommandManager().getDispatcher();

        dispatcher.register(ADDTIME_CMD);
        dispatcher.register(RANDOMSCORES_CMD);

        CommandUtil.sendCommandUpdate(server);
    }

    @Override
    public void unregister(MinecraftServer server) {
        CommandUtil.disableCommand(server, "addtime");
        CommandUtil.disableCommand(server, "randomscores");

        CommandUtil.sendCommandUpdate(server);
    }

    // /addtime
    private static final LiteralArgumentBuilder<ServerCommandSource> ADDTIME_CMD = LiteralArgumentBuilder.literal("addtime");
    static {
        ADDTIME_CMD.requires(src -> src.hasPermissionLevel(2));

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

    // /randomscores
    private static final LiteralArgumentBuilder<ServerCommandSource> RANDOMSCORES_CMD = LiteralArgumentBuilder.literal("randomscores");
    static {
        RANDOMSCORES_CMD.requires(src -> src.hasPermissionLevel(2));

        RANDOMSCORES_CMD.executes(context -> {
            if(EventManager.getInstance().getStateHandler() instanceof PrimaticaIngameHandler h) {
                h.resetScores();
                Random rnd = new Random();
                for(Team t : EventManager.getInstance().getServer().getScoreboard().getTeams()) {
                    int amt = rnd.nextInt(15);
                    for(int i = 0; i < amt; i++) h.score(t, null);
                }
            }
            return 0;
        });
    }
}
