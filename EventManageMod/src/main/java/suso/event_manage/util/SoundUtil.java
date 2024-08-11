package suso.event_manage.util;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.packet.s2c.play.PlaySoundS2CPacket;
import net.minecraft.network.packet.s2c.play.StopSoundS2CPacket;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import suso.event_common.custom.network.payloads.PlayFadeSoundPayload;
import suso.event_common.custom.network.payloads.UpdateFadePitchPayload;
import suso.event_common.custom.network.payloads.UpdateFadeVolumePayload;

import java.util.List;
import java.util.Random;

public class SoundUtil {
    private static final Random r = new Random();

    public static void playFadeSound(ServerPlayerEntity player, Identifier id, float startingVolume, float startingPitch, boolean loop, SoundCategory category, boolean apply) {
        ServerPlayNetworking.send(player, new PlayFadeSoundPayload(id, startingVolume, startingPitch, loop, category, apply));
    }

    public static void updateFadeVolume(ServerPlayerEntity player, Identifier id, float targetVolume, int fadeLengthTicks) {
        ServerPlayNetworking.send(player, new UpdateFadeVolumePayload(id, targetVolume, fadeLengthTicks));
    }

    public static void updateFadePitch(ServerPlayerEntity player, Identifier id, float targetPitch, int fadeLengthTicks) {
        ServerPlayNetworking.send(player, new UpdateFadePitchPayload(id, targetPitch, fadeLengthTicks));
    }

    public static void playSound(ServerPlayerEntity player, Identifier sound, SoundCategory category, Vec3d pos, float volume, float pitch) {
        RegistryEntry<SoundEvent> registryEntry = RegistryEntry.of(SoundEvent.of(sound));
        player.networkHandler.sendPacket(new PlaySoundS2CPacket(registryEntry, category, pos.x, pos.y, pos.z, volume, pitch, r.nextLong()));
    }

    public static void playSound(List<ServerPlayerEntity> players, Identifier sound, SoundCategory category, Vec3d pos, float volume, float pitch) {
        for(ServerPlayerEntity player : players) playSound(player, sound, category, pos, volume, pitch);
    }

    public static void stopSound(ServerPlayerEntity player, Identifier sound, SoundCategory category) {
        player.networkHandler.sendPacket(new StopSoundS2CPacket(sound, category));
    }
}
