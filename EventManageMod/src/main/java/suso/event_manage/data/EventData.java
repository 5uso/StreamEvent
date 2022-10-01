package suso.event_manage.data;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mojang.authlib.Agent;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.ProfileLookupCallback;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.scoreboard.Team;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.WhitelistEntry;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.LiteralTextContent;
import net.minecraft.text.MutableText;
import net.minecraft.util.Formatting;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class EventData {
    private static final String playerDataLocation = "./event/playerData/";
    private static final String playerListLocation = "./event/players.txt";
    private static final String teamDataLocation = "./event/teamData/";
    private static final String teamListLocation = "./event/teams.json";
    private static final String adminListLocation = "./event/admins.txt";

    private static EventData instance;

    private final Map<UUID, EventPlayerData> playerData;
    private final Set<String> playerList;

    private final Map<String, EventTeamData> teamData;

    private EventData() {
        playerData = new HashMap<>();
        playerList = new HashSet<>();

        teamData = new HashMap<>();
    }

    public void savePlayerData() {
        File dir = new File(playerDataLocation);
        if(!dir.exists() && !dir.mkdirs()) {
            System.err.println("Couldn't create player data folder at" + dir.getAbsolutePath());
            return;
        }

        for (Map.Entry<UUID, EventPlayerData> player : playerData.entrySet()) {
            try (FileOutputStream f = new FileOutputStream(playerDataLocation + player.getKey().toString()); ObjectOutputStream out = new ObjectOutputStream(f)) {
                out.writeObject(player.getValue());
            } catch (IOException e) {
                System.err.println("Error saving data for player with UUID " + player.getKey().toString() + ": " + e);
            }
        }
    }

    public void loadPlayerData() {
        playerData.clear();

        File dir = new File(playerDataLocation);
        File[] files = dir.listFiles(File::isFile);
        if (files == null) {
            System.err.println("Couldn't list player data. Defaulting to empty.");
            return;
        }

        for (File file : files) {
            try (FileInputStream f = new FileInputStream(file); ObjectInputStream in = new ObjectInputStream(f)) {
                UUID uuid = UUID.fromString(file.getName());
                EventPlayerData data = (EventPlayerData) in.readObject();
                playerData.put(uuid, data);
            } catch (IOException | ClassNotFoundException e) {
                System.err.println("Error loading data for player with UUID " + file.getName() + ": " + e);
            }
        }
    }

    public void loadPlayerList(MinecraftServer server) {
        playerList.clear();

        List<String> toWhitelist = new ArrayList<>(100);
        try(BufferedReader br = new BufferedReader(new FileReader(playerListLocation))) {
            String line;
            while((line = br.readLine()) != null) {
                line = line.strip();
                if(line.length() > 0) {
                    playerList.add(line);
                    toWhitelist.add(line);
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading player list: " + e);
        }

        server.getGameProfileRepo().findProfilesByNames(toWhitelist.toArray(new String[0]), Agent.MINECRAFT, new ProfileLookupCallback() {
            @Override
            public void onProfileLookupSucceeded(GameProfile profile) {
                server.getPlayerManager().getWhitelist().add(new WhitelistEntry(profile));
            }

            @Override
            public void onProfileLookupFailed(GameProfile profile, Exception exception) {
                System.err.println("Error adding player " + profile.getName() + " to whitelist: " + exception);
            }
        });
    }

    public boolean isInPlayerList(ServerPlayerEntity player) {
        return playerList.contains(player.getEntityName());
    }

    public void loadAdminList(MinecraftServer server) {
        List<String> toOp = new ArrayList<>(10);
        try(BufferedReader br = new BufferedReader(new FileReader(adminListLocation))) {
            String line;
            while((line = br.readLine()) != null) {
                line = line.strip();
                if(line.length() > 0) {
                    toOp.add(line);
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading admin list: " + e);
        }

        server.getGameProfileRepo().findProfilesByNames(toOp.toArray(new String[0]), Agent.MINECRAFT, new ProfileLookupCallback() {
            @Override
            public void onProfileLookupSucceeded(GameProfile profile) {
                server.getPlayerManager().addToOperators(profile);
            }

            @Override
            public void onProfileLookupFailed(GameProfile profile, Exception exception) {
                System.err.println("Error adding player " + profile.getName() + " to ops: " + exception);
            }
        });
    }

    public void saveTeamData() {
        File dir = new File(teamDataLocation);
        if(!dir.exists() && !dir.mkdirs()) {
            System.err.println("Couldn't create team data folder at" + dir.getAbsolutePath());
            return;
        }

        for (Map.Entry<String, EventTeamData> team : teamData.entrySet()) {
            try (FileOutputStream f = new FileOutputStream(teamDataLocation + team.getKey()); ObjectOutputStream out = new ObjectOutputStream(f)) {
                out.writeObject(team.getValue());
            } catch (IOException e) {
                System.err.println("Error saving data for player with name " + team.getKey() + ": " + e);
            }
        }
    }

    public void loadTeamData(MinecraftServer server) {
        Scoreboard s = server.getScoreboard();
        for(Team team : s.getTeams()) s.removeTeam(team);

        teamData.clear();

        File dir = new File(teamDataLocation);
        File[] files = dir.listFiles(File::isFile);
        if (files == null) {
            System.err.println("Couldn't list team data. Defaulting to empty.");
            return;
        }

        for (File file : files) {
            try (FileInputStream f = new FileInputStream(file); ObjectInputStream in = new ObjectInputStream(f)) {
                String name = file.getName();
                EventTeamData data = (EventTeamData) in.readObject();
                teamData.put(name, data);
            } catch (IOException | ClassNotFoundException e) {
                System.err.println("Error loading data for team with name " + file.getName() + ": " + e);
            }
        }

        try(Reader reader = Files.newBufferedReader(Paths.get(teamListLocation))) {
            List<EventTeamData> teams = new Gson().fromJson(reader, new TypeToken<List<EventTeamData>>() {}.getType());
            for(EventTeamData team : teams) {
                Team t = s.addTeam(team.name);
                t.setDisplayName(MutableText.of(new LiteralTextContent(team.display)));
                t.setColor(Formatting.byColorIndex(team.color));
                for(String player : team.members) s.addPlayerToTeam(player, t);

                if(teamData.containsKey(team.name)) {
                    EventTeamData prev = teamData.get(team.name);
                    prev.display = team.display;
                    prev.color = team.color;
                    prev.members = team.members;
                } else {
                    teamData.put(team.name, team);
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading team list: " + e);
        }
    }

    public void registerPlayer(ServerPlayerEntity player) {
        EventPlayerData data = new EventPlayerData();
        data.initialize(player);
        playerData.put(player.getUuid(), data);
    }

    public boolean isPlayerRegistered(ServerPlayerEntity player) {
        return playerData.containsKey(player.getUuid());
    }

    @Nullable
    public EventPlayerData getPlayerData(UUID id) {
        return playerData.get(id);
    }

    @Nullable
    public EventPlayerData getPlayerData(ServerPlayerEntity player) {
        return getPlayerData(player.getUuid());
    }

    public void saveData() {
        savePlayerData();
        saveTeamData();
    }

    public void loadData(MinecraftServer server) {
        loadPlayerData();
        loadPlayerList(server);
        loadTeamData(server);
        loadAdminList(server);
    }

    public static EventData getInstance() {
        if (instance == null) instance = new EventData();
        return instance;
    }
}
