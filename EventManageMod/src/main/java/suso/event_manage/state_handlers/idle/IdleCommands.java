package suso.event_manage.state_handlers.idle;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import suso.event_manage.EventManager;
import suso.event_manage.state_handlers.StateCommands;
import suso.event_manage.state_handlers.primatica.PrimaticaIngameHandler;
import suso.event_manage.util.CommandUtil;

public class IdleCommands implements StateCommands {
    @Override
    public void register(MinecraftServer server) {
        CommandDispatcher<ServerCommandSource> dispatcher = server.getCommandManager().getDispatcher();

        dispatcher.register(PRIMATICA_CMD);

        CommandUtil.sendCommandUpdate(server);
    }

    @Override
    public void unregister(MinecraftServer server) {
        CommandUtil.disableCommand(server, "primatica");

        CommandUtil.sendCommandUpdate(server);
    }

    // /primatica
    private static final LiteralArgumentBuilder<ServerCommandSource> PRIMATICA_CMD = LiteralArgumentBuilder.literal("primatica");
    static {
        PRIMATICA_CMD.executes(context -> {
            EventManager.getInstance().setStateHandler(new PrimaticaIngameHandler(5 * 60 * 1000));
            context.getSource().sendFeedback(() -> Text.literal("Starting primatica!"), true);
            return 1;
        });
    }
}
