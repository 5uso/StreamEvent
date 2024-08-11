package suso.event_manage.util;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;
import suso.event_manage.EventManager;
import suso.event_manage.EvtBaseConstants;
import suso.event_common.custom.network.payloads.HudDataPayload;
import suso.event_manage.state_handlers.primatica.PrimaticaScore;

import java.util.UUID;

public class HudUtil {
    public static void setState(ServerPlayerEntity player, EvtBaseConstants.States state) {
        ServerPlayNetworking.send(player, HudDataPayload.ofState(state));
    }

    public static void setTimer(ServerPlayerEntity player, long time) {
        ServerPlayNetworking.send(player, HudDataPayload.ofTimer(time));
    }

    public static void setAgility(ServerPlayerEntity player, boolean active) {
        ServerPlayNetworking.send(player, HudDataPayload.ofAgility(active));
    }

    public static void setPrimaticaScore(ServerPlayerEntity player, PrimaticaScore score) {
        ServerPlayNetworking.send(player, HudDataPayload.ofPrimaticaScore(score.getScores(), score.getRanks()));
    }

    public static void broadcastFeedMessage(UUID player1, Identifier texture, @Nullable UUID player2) {
        CustomPayload p = HudDataPayload.ofFeed(player1, texture, player2);
        EventManager.getInstance().getServer().getPlayerManager().getPlayerList().forEach(player -> ServerPlayNetworking.send(player, p));
    }

    public static void setInfo(ServerPlayerEntity player, Identifier id) {
        ServerPlayNetworking.send(player, HudDataPayload.ofInfo(id));
    }

    public static void sendKill(ServerPlayerEntity player, ServerPlayerEntity victim) {
        ServerPlayNetworking.send(player, HudDataPayload.ofKill(victim));
    }
}
