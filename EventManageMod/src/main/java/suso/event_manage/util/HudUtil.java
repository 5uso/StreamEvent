package suso.event_manage.util;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;
import suso.event_manage.EventManager;
import suso.event_manage.EvtBaseConstants;
import suso.event_manage.state_handlers.primatica.PrimaticaScore;

import java.util.UUID;

public class HudUtil {
    public enum DataTypes {
        STATE, TIMER, FEED, AGILITY, PRIMATICA_SCORE, INFO, KILL
    }

    public static void setState(ServerPlayerEntity player, EvtBaseConstants.States state) {
        PacketByteBuf p = PacketByteBufs.create();
        p.writeInt(DataTypes.STATE.ordinal());
        p.writeInt(state.ordinal());

        ServerPlayNetworking.send(player, EvtBaseConstants.HUD_DATA, p);
    }

    public static void setTimer(ServerPlayerEntity player, long time) {
        PacketByteBuf p = PacketByteBufs.create();
        p.writeInt(DataTypes.TIMER.ordinal());
        p.writeLong(time);

        ServerPlayNetworking.send(player, EvtBaseConstants.HUD_DATA, p);
    }

    public static void setAgility(ServerPlayerEntity player, boolean active) {
        PacketByteBuf p = PacketByteBufs.create();
        p.writeInt(DataTypes.AGILITY.ordinal());
        p.writeBoolean(active);

        ServerPlayNetworking.send(player, EvtBaseConstants.HUD_DATA, p);
    }

    public static void setPrimaticaScore(ServerPlayerEntity player, PrimaticaScore score) {
        PacketByteBuf p = PacketByteBufs.create();
        p.writeInt(DataTypes.PRIMATICA_SCORE.ordinal());

        int[] scores = score.getScores();
        for(int i = 0; i < 12; i++) p.writeInt(scores[i]);

        int[] ranks = score.getRanks();
        for(int i = 0; i < 12; i++) p.writeInt(ranks[i]);

        ServerPlayNetworking.send(player, EvtBaseConstants.HUD_DATA, p);
    }

    public static void broadcastFeedMessage(UUID player1, Identifier texture, @Nullable UUID player2) {
        PacketByteBuf p = PacketByteBufs.create();
        p.writeInt(DataTypes.FEED.ordinal());

        p.writeUuid(player1);
        p.writeIdentifier(texture);
        p.writeUuid(player2 == null ? EvtBaseConstants.NULL_UUID : player2);

        EventManager.getInstance().getServer().getPlayerManager().getPlayerList().forEach(player -> ServerPlayNetworking.send(player, EvtBaseConstants.HUD_DATA, p));
    }

    public static void setInfo(ServerPlayerEntity player, Identifier id) {
        PacketByteBuf p = PacketByteBufs.create();
        p.writeInt(DataTypes.INFO.ordinal());

        p.writeBoolean(id != null);
        if(id != null) p.writeIdentifier(id);

        ServerPlayNetworking.send(player, EvtBaseConstants.HUD_DATA, p);
    }

    public static void sendKill(ServerPlayerEntity player, ServerPlayerEntity victim) {
        PacketByteBuf p = PacketByteBufs.create();
        p.writeInt(DataTypes.KILL.ordinal());

        p.writeString(victim.getNameForScoreboard());
        p.writeInt(victim.getScoreboardTeam() == null ? Formatting.WHITE.ordinal() : victim.getScoreboardTeam().getColor().ordinal());

        ServerPlayNetworking.send(player, EvtBaseConstants.HUD_DATA, p);
    }
}
