package suso.event_manage;

import net.fabricmc.fabric.api.networking.v1.LoginPacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerLoginNetworking;
import net.minecraft.network.packet.c2s.login.LoginQueryResponseC2SPacket;
import net.minecraft.network.packet.s2c.login.LoginQueryRequestS2CPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerLoginNetworkHandler;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Util;
import suso.event_common.custom.network.payloads.LoginCheckPayload;

public class ModCheck {
    public static void handleConnection(ServerLoginNetworkHandler handler, MinecraftServer server, LoginPacketSender sender, ServerLoginNetworking.LoginSynchronizer synchronizer) {
        sender.sendPacket(new LoginQueryRequestS2CPacket(2711, new LoginCheckPayload(getTime())));
    }

    public static void handleResponse(ServerLoginNetworkHandler handler, LoginQueryResponseC2SPacket packet) {
        if(packet.response() == null) handler.disconnect(Text
                .literal("A custom client is required to join the server.\n Please contact Suso or Adri.")
                .formatted(Formatting.BOLD));
    }

    public static long getTime() {
        return Util.getMeasuringTimeMs();
    }
}
