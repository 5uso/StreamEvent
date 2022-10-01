package suso.event_manage.util;

import com.mojang.authlib.GameProfile;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.command.argument.GameProfileArgumentType;
import net.minecraft.command.argument.GameProfileArgumentType.GameProfileArgument;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.LiteralTextContent;
import net.minecraft.text.MutableText;
import suso.event_manage.EventManager;
import suso.event_manage.data.EventData;
import suso.event_manage.data.EventPlayerData;

import java.util.Collection;

public class CommandUtil {
    public static void disableCommand(MinecraftServer server, String cmd) {
        CommandDispatcher<ServerCommandSource> dispatcher = server.getCommandManager().getDispatcher();
        ((ICommandNodeUtil) dispatcher.getRoot()).removeChild(cmd);

        PlayerManager pm = server.getPlayerManager();
        pm.getPlayerList().forEach(pm::sendCommandTree);
    }

    public static void disableCommand(CommandDispatcher<ServerCommandSource> dispatcher, String cmd) {
        ((ICommandNodeUtil) dispatcher.getRoot()).removeChild(cmd);
    }

    public static void registerGlobalCommands(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess registryAccess, CommandManager.RegistrationEnvironment environment) {
        dispatcher.register(EVENTPLAYER_CMD);

        disableCommand(dispatcher, "team");
    }

    // /eventplayer <add|remove> <player>
    private static final LiteralArgumentBuilder<ServerCommandSource> EVENTPLAYER_CMD = LiteralArgumentBuilder.literal("eventplayer");
    static {
        LiteralArgumentBuilder<ServerCommandSource> add = LiteralArgumentBuilder.literal("add");
        RequiredArgumentBuilder<ServerCommandSource, GameProfileArgument> add_player = CommandManager.argument("player", GameProfileArgumentType.gameProfile());
        add_player.executes(context -> executeEventplayer(context, true));
        add.then(add_player);
        EVENTPLAYER_CMD.then(add);

        LiteralArgumentBuilder<ServerCommandSource> remove = LiteralArgumentBuilder.literal("remove");
        RequiredArgumentBuilder<ServerCommandSource, GameProfileArgument> remove_player = CommandManager.argument("player", GameProfileArgumentType.gameProfile());
        remove_player.executes(context -> executeEventplayer(context, false));
        remove.then(remove_player);
        EVENTPLAYER_CMD.then(remove);
    }
    static private int executeEventplayer(CommandContext<ServerCommandSource> context, boolean isPlayer) throws CommandSyntaxException {
        Collection<GameProfile> profiles = GameProfileArgumentType.getProfileArgument(context, "player");
        for(GameProfile p : profiles) {
            EventPlayerData pd = EventData.getInstance().getPlayerData(p.getId());
            if(pd != null) pd.isPlayer = isPlayer;

            MinecraftServer server = context.getSource().getServer();
            ServerPlayerEntity player = (ServerPlayerEntity) server.getOverworld().getEntity(p.getId());
            if(player != null) {
                EventManager.getInstance().onPlayerJoin(player);
                player.sendMessage(MutableText.of(new LiteralTextContent(isPlayer ? "You are now a player" : "You are no longer a player")));
            }

            if(player == null || (context.getSource().getPlayer() != null && !player.getUuid().equals(context.getSource().getPlayer().getUuid()))) {
                context.getSource().sendFeedback(MutableText.of(new LiteralTextContent(p.getName() + (isPlayer ? " is now a player" : " is no longer a player"))), true);
            }
        }
        return profiles.size();
    }
}
