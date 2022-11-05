package suso.event_base.client;

import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientLoginNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientLoginNetworkHandler;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Util;
import suso.event_base.EvtBaseConstants;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

@Environment(EnvType.CLIENT)
public class ModCheck {
    public static void registerPacketListeners() {
        ClientLoginNetworking.registerGlobalReceiver(EvtBaseConstants.LOGIN_CHECK, ModCheck::handleLoginQuery);
    }

    private static CompletableFuture<PacketByteBuf> handleLoginQuery(MinecraftClient client, ClientLoginNetworkHandler handler, PacketByteBuf buf, Consumer<GenericFutureListener<? extends Future<? super Void>>> listenerAdder) {
        long serverTime = buf.readLong();
        offset = serverTime - Util.getMeasuringTimeMs();

        return CompletableFuture.completedFuture(buf);
    }

    private static long offset = 0;
    public static long getTime() {
        return Util.getMeasuringTimeMs() + offset;
    }
}
