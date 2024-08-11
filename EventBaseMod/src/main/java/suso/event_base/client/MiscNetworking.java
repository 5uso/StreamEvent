package suso.event_base.client;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.particle.FireworksSparkParticle;
import suso.event_base.custom.entities.EventUpdatable;
import suso.event_common.custom.network.payloads.EntityUpdatePayload;
import suso.event_common.custom.network.payloads.FireworkParticlePayload;
import suso.event_common.custom.network.payloads.HudDataPayload;
import suso.event_common.custom.network.payloads.JumpInputPayload;
import suso.event_base.custom.render.CustomRender;

public class MiscNetworking {
    public static void registerPacketListeners() {
        PayloadTypeRegistry.playS2C().register(FireworkParticlePayload.ID, FireworkParticlePayload.CODEC);
        PayloadTypeRegistry.playS2C().register(EntityUpdatePayload.ID, EntityUpdatePayload.CODEC);
        PayloadTypeRegistry.playS2C().register(HudDataPayload.ID, HudDataPayload.CODEC);
        PayloadTypeRegistry.playC2S().register(JumpInputPayload.ID, JumpInputPayload.CODEC);

        ClientPlayNetworking.registerGlobalReceiver(FireworkParticlePayload.ID, MiscNetworking::fireworkParticleHandler);
        ClientPlayNetworking.registerGlobalReceiver(EntityUpdatePayload.ID, MiscNetworking::entityUpdateHandler);
        ClientPlayNetworking.registerGlobalReceiver(HudDataPayload.ID, MiscNetworking::hudDataHandler);
    }

    private static void fireworkParticleHandler(FireworkParticlePayload p, ClientPlayNetworking.Context ctx) {
        MinecraftClient client = ctx.client();
        client.execute(() -> client.particleManager.addParticle(
                new FireworksSparkParticle.FireworkParticle(client.world, p.x, p.y, p.z, p.vx, p.vy, p.vz, client.particleManager, p.fireworks)
        ));
    }

    private static void entityUpdateHandler(EntityUpdatePayload p, ClientPlayNetworking.Context ctx) {
        MinecraftClient client = ctx.client();
        client.execute(() -> {
            if (client.world != null && client.world.getEntityById(p.entityId) instanceof EventUpdatable e)
                e.customUpdate(p.nbt);
        });
    }

    private static void hudDataHandler(HudDataPayload p, ClientPlayNetworking.Context ctx) {
        CustomRender.CUSTOM_HUD.onHudData(p);
    }
}
