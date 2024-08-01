package suso.event_base.custom.network;

import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerLoginConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerLoginNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import suso.event_manage.ModCheck;
import suso.event_base.custom.network.
payloads.*;
import suso.event_manage.util.MiscUtil;

public class CustomPackets {
    public static void register() {
        // Particle packets
        PayloadTypeRegistry.playS2C().register(FireworkParticlePayload.ID, FireworkParticlePayload.CODEC);

        // Misc packets
        PayloadTypeRegistry.playS2C().register(EntityUpdatePayload.ID, EntityUpdatePayload.CODEC);
        PayloadTypeRegistry.playS2C().register(HudDataPayload.ID, HudDataPayload.CODEC);
        PayloadTypeRegistry.playC2S().register(JumpInputPayload.ID, JumpInputPayload.CODEC);
        PayloadTypeRegistry.configurationS2C().register(LoginCheckPayload.ID, LoginCheckPayload.CODEC);

        // Receivers
        ServerPlayNetworking.registerGlobalReceiver(JumpInputPayload.ID, MiscUtil::handleJumpInput);

        ServerLoginConnectionEvents.QUERY_START.register(ModCheck::handleConnection);
        ServerLoginNetworking.registerGlobalReceiver(LoginCheckPayload.ID.id(), ModCheck::handleResponse);
    }
}
