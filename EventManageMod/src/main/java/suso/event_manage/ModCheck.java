package suso.event_manage;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerLoginNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerLoginNetworkHandler;
import net.minecraft.text.LiteralTextContent;
import net.minecraft.text.MutableText;
import net.minecraft.util.Formatting;

public class ModCheck {
    public static void handleConnection(ServerLoginNetworkHandler handler, MinecraftServer server, PacketSender sender, ServerLoginNetworking.LoginSynchronizer synchronizer) {
        PacketByteBuf p = PacketByteBufs.create();
        sender.sendPacket(EvtBaseConstants.LOGIN_CHECK, p);
    }

    public static void handleResponse(MinecraftServer server, ServerLoginNetworkHandler handler, boolean understood, PacketByteBuf buf, ServerLoginNetworking.LoginSynchronizer synchronizer, PacketSender responseSender) {
        if(!understood) handler.disconnect(MutableText.of(new LiteralTextContent("A custom client is required to join the server.\n Please contact Suso or Adri.")).formatted(Formatting.BOLD));
    }
}
