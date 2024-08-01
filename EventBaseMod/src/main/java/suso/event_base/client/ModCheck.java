package suso.event_base.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientLoginNetworking;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientLoginNetworkHandler;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.PacketCallbacks;
import net.minecraft.util.Util;
import suso.event_base.custom.network.payloads.LoginCheckPayload;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

@Environment(EnvType.CLIENT)
public class ModCheck {
    public static void registerPacketListeners() {
        PayloadTypeRegistry.configurationS2C().register(LoginCheckPayload.ID, LoginCheckPayload.CODEC);

        ClientLoginNetworking.registerGlobalReceiver(LoginCheckPayload.ID.id(), ModCheck::handleLoginQuery);
    }

    private static CompletableFuture<PacketByteBuf> handleLoginQuery(MinecraftClient client, ClientLoginNetworkHandler handler, PacketByteBuf buf, Consumer<PacketCallbacks> callbacksConsumer) {
        long serverTime = buf.readLong();
        offset = serverTime - Util.getMeasuringTimeMs();

        return CompletableFuture.completedFuture(buf);
    }

    private static long offset = 0;
    public static long getTime() {
        return Util.getMeasuringTimeMs() + offset;
    }
}
