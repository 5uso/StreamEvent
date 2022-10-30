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
import net.minecraft.command.argument.TeamArgumentType;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.scoreboard.Team;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.LiteralTextContent;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import suso.event_manage.EventManager;
import suso.event_manage.data.EventData;
import suso.event_manage.data.EventPlayerData;
import suso.event_manage.data.EventTeamData;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

public class CommandUtil {
    public static void sendCommandUpdate(MinecraftServer server) {
        PlayerManager pm = server.getPlayerManager();
        pm.getPlayerList().forEach(pm::sendCommandTree);
    }

    public static void disableCommand(MinecraftServer server, String cmd) {
        CommandDispatcher<ServerCommandSource> dispatcher = server.getCommandManager().getDispatcher();
        ((ICommandNodeUtil) dispatcher.getRoot()).removeChild(cmd);
    }

    public static void disableCommand(CommandDispatcher<ServerCommandSource> dispatcher, String cmd) {
        ((ICommandNodeUtil) dispatcher.getRoot()).removeChild(cmd);
    }

    public static void registerGlobalCommands(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess registryAccess, CommandManager.RegistrationEnvironment environment) {
        dispatcher.register(EVENTPLAYER_CMD);
        dispatcher.register(EVENTTEAM_CMD);

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
        ServerCommandSource source = context.getSource();
        MinecraftServer server = source.getServer();

        Collection<GameProfile> profiles = GameProfileArgumentType.getProfileArgument(context, "player");

        for(GameProfile p : profiles) {
            EventPlayerData pd = EventData.getInstance().getPlayerData(p.getId());
            if(pd != null) pd.isPlayer = isPlayer;

            ServerPlayerEntity player = (ServerPlayerEntity) server.getOverworld().getEntity(p.getId());
            if(player != null) {
                EventManager.getInstance().onPlayerJoin(player);
                player.sendMessage(MutableText.of(new LiteralTextContent(isPlayer ? "You are now a player" : "You are no longer a player")));
            }

            if(player == null || (source.getPlayer() != null && !player.getUuid().equals(source.getPlayer().getUuid()))) {
                source.sendFeedback(MutableText.of(new LiteralTextContent(p.getName() + (isPlayer ? " is now a player" : " is no longer a player"))), true);
            }
        }
        return profiles.size();
    }


    // /eventteam <add|remove> <team> <player>
    private static final LiteralArgumentBuilder<ServerCommandSource> EVENTTEAM_CMD = LiteralArgumentBuilder.literal("eventteam");
    static {
        LiteralArgumentBuilder<ServerCommandSource> add = LiteralArgumentBuilder.literal("add");
        RequiredArgumentBuilder<ServerCommandSource, String> add_team = CommandManager.argument("team", TeamArgumentType.team());
        RequiredArgumentBuilder<ServerCommandSource, GameProfileArgument> add_player = CommandManager.argument("player", GameProfileArgumentType.gameProfile());
        add_player.executes(CommandUtil::executeEventTeamAdd);
        add_team.then(add_player);
        add.then(add_team);
        EVENTTEAM_CMD.then(add);

        LiteralArgumentBuilder<ServerCommandSource> remove = LiteralArgumentBuilder.literal("remove");
        RequiredArgumentBuilder<ServerCommandSource, String> remove_team = CommandManager.argument("team", TeamArgumentType.team());
        RequiredArgumentBuilder<ServerCommandSource, GameProfileArgument> remove_player = CommandManager.argument("player", GameProfileArgumentType.gameProfile());
        remove_team.then(remove_player);
        remove.then(remove_team);
        EVENTTEAM_CMD.then(remove);
    }
    static private int executeEventTeamAdd(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        ServerCommandSource source = context.getSource();
        MinecraftServer server = source.getServer();
        Scoreboard s = server.getScoreboard();
        EventData d = EventData.getInstance();

        Collection<GameProfile> profiles = GameProfileArgumentType.getProfileArgument(context, "player");
        Team team = TeamArgumentType.getTeam(context, "team");
        Text teamName = team.getDisplayName();
        EventTeamData td = d.getTeamData(team);

        if(td == null) {
            source.sendError(MutableText.of(new LiteralTextContent("Error getting team data")));
            return 0;
        }

        for(GameProfile p : profiles) {
            s.addPlayerToTeam(p.getName(), team);
            ArrayList<String> memberList = new ArrayList<>(Arrays.asList(td.members));
            memberList.add(p.getName());
            td.members = memberList.toArray(new String[0]);

            ServerPlayerEntity player = (ServerPlayerEntity) server.getOverworld().getEntity(p.getId());
            if(player != null) {
                EventManager.getInstance().onPlayerJoin(player);
                player.sendMessage(MutableText.of(new LiteralTextContent("You are now part of team ")).append(teamName));
            }

            if(player == null || (source.getPlayer() != null && !player.getUuid().equals(source.getPlayer().getUuid()))) {
                source.sendFeedback(MutableText.of(new LiteralTextContent(p.getName() + (" is now part of team "))).append(teamName), true);
            }
        }
        return profiles.size();
    }
    static private int executeEventTeamRemove(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        ServerCommandSource source = context.getSource();
        MinecraftServer server = source.getServer();
        Scoreboard s = server.getScoreboard();
        EventData d = EventData.getInstance();

        Collection<GameProfile> profiles = GameProfileArgumentType.getProfileArgument(context, "player");
        Team team = TeamArgumentType.getTeam(context, "team");
        Text teamName = team.getDisplayName();
        EventTeamData td = d.getTeamData(team);

        if(td == null) {
            source.sendError(MutableText.of(new LiteralTextContent("Error getting team data")));
            return 0;
        }

        for(GameProfile p : profiles) {
            s.removePlayerFromTeam(p.getName(), team);
            ArrayList<String> memberList = new ArrayList<>(Arrays.asList(td.members));
            memberList.removeIf(name -> name.equals(p.getName()));
            td.members = memberList.toArray(new String[0]);

            ServerPlayerEntity player = (ServerPlayerEntity) server.getOverworld().getEntity(p.getId());
            if(player != null) {
                EventManager.getInstance().onPlayerJoin(player);
                player.sendMessage(MutableText.of(new LiteralTextContent("You are no longer part of team ")).append(teamName));
            }

            if(player == null || (source.getPlayer() != null && !player.getUuid().equals(source.getPlayer().getUuid()))) {
                source.sendFeedback(MutableText.of(new LiteralTextContent(p.getName() + (" is no longer part of team "))).append(teamName), true);
            }
        }
        return profiles.size();
    }
}
