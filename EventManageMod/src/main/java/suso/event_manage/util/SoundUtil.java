package suso.event_manage.util;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.s2c.play.PlaySoundS2CPacket;
import net.minecraft.network.packet.s2c.play.StopSoundS2CPacket;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import suso.event_manage.EvtBaseConstants;

import java.util.List;
import java.util.Random;

public class SoundUtil {
    private static final Random r = new Random();

    public static void playFadeSound(ServerPlayerEntity player, Identifier id, float startingVolume, float startingPitch, boolean loop, SoundCategory category, boolean apply) {
        PacketByteBuf p = PacketByteBufs.create();

        p.writeString(id.toString());
        p.writeFloat(startingVolume);
        p.writeFloat(startingPitch);
        p.writeBoolean(loop);
        p.writeByte(category.ordinal());
        p.writeBoolean(apply);

        ServerPlayNetworking.send(player, EvtBaseConstants.PLAY_FADE_SOUND, p);
    }

    public static void updateFadeVolume(ServerPlayerEntity player, Identifier id, float targetVolume, int fadeLengthTicks) {
        PacketByteBuf p = PacketByteBufs.create();

        p.writeString(id.toString());
        p.writeFloat(targetVolume);
        p.writeInt(fadeLengthTicks);

        ServerPlayNetworking.send(player, EvtBaseConstants.UPDATE_FADE_VOLUME, p);
    }

    public static void updateFadePitch(ServerPlayerEntity player, Identifier id, float targetPitch, int fadeLengthTicks) {
        PacketByteBuf p = PacketByteBufs.create();

        p.writeString(id.toString());
        p.writeFloat(targetPitch);
        p.writeInt(fadeLengthTicks);

        ServerPlayNetworking.send(player, EvtBaseConstants.UPDATE_FADE_PITCH, p);
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
