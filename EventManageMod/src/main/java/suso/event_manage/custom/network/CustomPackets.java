package suso.event_manage.custom.network;

import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import suso.event_manage.custom.network.payloads.PlayFadeSoundPayload;
import suso.event_manage.custom.network.payloads.UpdateFadePitchPayload;
import suso.event_manage.custom.network.payloads.UpdateFadeVolumePayload;

public class CustomPackets {
    public static void register() {
        PayloadTypeRegistry.playS2C().register(PlayFadeSoundPayload.ID, PlayFadeSoundPayload.CODEC);
        PayloadTypeRegistry.playS2C().register(UpdateFadeVolumePayload.ID, UpdateFadeVolumePayload.CODEC);
        PayloadTypeRegistry.playS2C().register(UpdateFadePitchPayload.ID, UpdateFadePitchPayload.CODEC);
    }
}
