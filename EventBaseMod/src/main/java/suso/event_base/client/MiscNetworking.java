package suso.event_base.client;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.particle.FireworksSparkParticle;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import suso.event_base.EvtBaseConstants;

public class MiscNetworking {
    public static void registerPacketListeners() {
        ClientPlayNetworking.registerGlobalReceiver(EvtBaseConstants.FIREWORK_PARTICLE, MiscNetworking::fireworkParticleHandler);
    }

    private static void fireworkParticleHandler(MinecraftClient client, ClientPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
        double x = buf.readDouble();
        double y = buf.readDouble();
        double z = buf.readDouble();
        double vx = buf.readDouble();
        double vy = buf.readDouble();
        double vz = buf.readDouble();
        NbtCompound firework = buf.readNbt();
        client.execute(() -> client.particleManager.addParticle(new FireworksSparkParticle.FireworkParticle(client.world, x, y, z, vx, vy, vz, client.particleManager, firework)));
    }
}
