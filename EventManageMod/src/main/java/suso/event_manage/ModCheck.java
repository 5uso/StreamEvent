package suso.event_manage;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerLoginNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerLoginNetworkHandler;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Util;

public class ModCheck {
    public static void handleConnection(ServerLoginNetworkHandler handler, MinecraftServer server, PacketSender sender, ServerLoginNetworking.LoginSynchronizer synchronizer) {
        PacketByteBuf p = PacketByteBufs.create();
        p.writeLong(Util.getMeasuringTimeMs());

        sender.sendPacket(EvtBaseConstants.LOGIN_CHECK, p);
    }

    public static void handleResponse(MinecraftServer server, ServerLoginNetworkHandler handler, boolean understood, PacketByteBuf buf, ServerLoginNetworking.LoginSynchronizer synchronizer, PacketSender responseSender) {
        if(!understood) handler.disconnect(Text.literal("A custom client is required to join the server.\n Please contact Suso or Adri.").formatted(Formatting.BOLD));
    }

    public static long getTime() {
        return Util.getMeasuringTimeMs();
    }
}
