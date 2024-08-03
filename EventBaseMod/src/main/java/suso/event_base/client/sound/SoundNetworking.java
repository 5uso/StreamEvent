package suso.event_base.client.sound;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.minecraft.client.MinecraftClient;
import suso.event_base.custom.network.payloads.PlayFadeSoundPayload;
import suso.event_base.custom.network.payloads.UpdateFadePitchPayload;
import suso.event_base.custom.network.payloads.UpdateFadeVolumePayload;
import suso.event_base.mixin.client.SoundManagerAccess;

import java.util.LinkedList;
import java.util.Queue;

@Environment(EnvType.CLIENT)
public class SoundNetworking {
    private static final Queue<FadeSoundInstance> buffered = new LinkedList<>();

    public static void registerPacketListeners() {
        PayloadTypeRegistry.playS2C().register(PlayFadeSoundPayload.ID, PlayFadeSoundPayload.CODEC);
        PayloadTypeRegistry.playS2C().register(UpdateFadeVolumePayload.ID, UpdateFadeVolumePayload.CODEC);
        PayloadTypeRegistry.playS2C().register(UpdateFadePitchPayload.ID, UpdateFadePitchPayload.CODEC);

        ClientPlayNetworking.registerGlobalReceiver(PlayFadeSoundPayload.ID, SoundNetworking::playFadeSoundHandler);
        ClientPlayNetworking.registerGlobalReceiver(UpdateFadeVolumePayload.ID, SoundNetworking::updateFadeVolumeHandler);
        ClientPlayNetworking.registerGlobalReceiver(UpdateFadePitchPayload.ID, SoundNetworking::updateFadePitchHandler);
    }

    private static void playFadeSoundHandler(PlayFadeSoundPayload p, ClientPlayNetworking.Context ctx) {
        buffered.add(new FadeSoundInstance(p.id, p.startingVolume, p.startingPitch, p.loop, p.category));
        if(!p.apply) return;

        MinecraftClient client = ctx.client();
        client.execute(() -> {
            while(!buffered.isEmpty()) client.getSoundManager().play(buffered.poll());
        });
    }

    private static void updateFadeVolumeHandler(UpdateFadeVolumePayload p, ClientPlayNetworking.Context ctx) {
        MinecraftClient client = ctx.client();
        client.execute(() -> {
            ISoundSystemUtil system = (ISoundSystemUtil) ((SoundManagerAccess) client.getSoundManager()).getSoundSystem();
            system.sendFadeVolume(p.id, p.targetVolume, p.fadeLengthTicks);
        });
    }

    private static void updateFadePitchHandler(UpdateFadePitchPayload p, ClientPlayNetworking.Context ctx) {
        MinecraftClient client = ctx.client();
        client.execute(() -> {
            ISoundSystemUtil system = (ISoundSystemUtil) ((SoundManagerAccess) client.getSoundManager()).getSoundSystem();
            system.sendFadePitch(p.id, p.targetPitch, p.fadeLengthTicks);
        });
    }
}
