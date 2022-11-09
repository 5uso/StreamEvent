package suso.event_base.client;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.particle.FireworksSparkParticle;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import suso.event_base.EvtBaseConstants;
import suso.event_base.custom.entities.EventUpdatable;
import suso.event_base.custom.render.CustomRender;

public class MiscNetworking {
    public static void registerPacketListeners() {
        ClientPlayNetworking.registerGlobalReceiver(EvtBaseConstants.FIREWORK_PARTICLE, MiscNetworking::fireworkParticleHandler);
        ClientPlayNetworking.registerGlobalReceiver(EvtBaseConstants.ENTITY_UPDATE, MiscNetworking::entityUpdateHandler);
        ClientPlayNetworking.registerGlobalReceiver(EvtBaseConstants.HUD_DATA, MiscNetworking::hudDataHandler);
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

    private static void entityUpdateHandler(MinecraftClient client, ClientPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
        int id = buf.readInt();
        NbtCompound nbt = buf.readNbt();
        client.execute(() -> {
            if(client.world != null && client.world.getEntityById(id) instanceof EventUpdatable e) {
                e.customUpdate(nbt);
            }
        });
    }

    private static void hudDataHandler(MinecraftClient client, ClientPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
        client.execute(() -> CustomRender.CUSTOM_HUD.onHudData(buf));
    }
}
