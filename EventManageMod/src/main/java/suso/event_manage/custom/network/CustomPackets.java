package suso.event_manage.custom.network;

import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerLoginConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import suso.event_manage.ModCheck;
import suso.event_common.custom.network.payloads.*;
import suso.event_manage.util.MiscUtil;

public class CustomPackets {
    public static void register() {
        // Sound packets
        PayloadTypeRegistry.playS2C().register(PlayFadeSoundPayload.ID, PlayFadeSoundPayload.CODEC);
        PayloadTypeRegistry.playS2C().register(UpdateFadeVolumePayload.ID, UpdateFadeVolumePayload.CODEC);
        PayloadTypeRegistry.playS2C().register(UpdateFadePitchPayload.ID, UpdateFadePitchPayload.CODEC);

        // Shader packets
        PayloadTypeRegistry.playS2C().register(SetShaderUniformPayload.ID, SetShaderUniformPayload.CODEC);
        PayloadTypeRegistry.playS2C().register(SetPostShaderPayload.ID, SetPostShaderPayload.CODEC);
        PayloadTypeRegistry.playS2C().register(SetBlockColorPayload.ID, SetBlockColorPayload.CODEC);

        // Particle packets
        PayloadTypeRegistry.playS2C().register(FireworkParticlePayload.ID, FireworkParticlePayload.CODEC);

        // Misc packets
        PayloadTypeRegistry.playS2C().register(EntityUpdatePayload.ID, EntityUpdatePayload.CODEC);
        PayloadTypeRegistry.playS2C().register(HudDataPayload.ID, HudDataPayload.CODEC);
        PayloadTypeRegistry.playC2S().register(JumpInputPayload.ID, JumpInputPayload.CODEC);

        // Receivers
        ServerPlayNetworking.registerGlobalReceiver(JumpInputPayload.ID, MiscUtil::handleJumpInput);

        ServerLoginConnectionEvents.QUERY_START.register(ModCheck::handleConnection);
    }
}
