package suso.event_base.client.sound;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.Identifier;
import suso.event_base.EvtBaseConstants;
import suso.event_base.mixin.client.SoundManagerAccess;

import java.util.LinkedList;
import java.util.Queue;

@Environment(EnvType.CLIENT)
public class SoundNetworking {
    private static final Queue<FadeSoundInstance> buffered = new LinkedList<>();

    public static void registerPacketListeners() {
        ClientPlayNetworking.registerGlobalReceiver(EvtBaseConstants.PLAY_FADE_SOUND, SoundNetworking::playFadeSoundHandler);
        ClientPlayNetworking.registerGlobalReceiver(EvtBaseConstants.UPDATE_FADE_VOLUME, SoundNetworking::updateFadeVolumeHandler);
        ClientPlayNetworking.registerGlobalReceiver(EvtBaseConstants.UPDATE_FADE_PITCH, SoundNetworking::updateFadePitchHandler);
    }

    private static void playFadeSoundHandler(MinecraftClient client, ClientPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
        Identifier id = new Identifier(buf.readString());
        float startVolume = buf.readFloat();
        float startPitch = buf.readFloat();
        boolean loop = buf.readBoolean();
        SoundCategory category = SoundCategory.values()[buf.readByte()];

        buffered.add(new FadeSoundInstance(id, startVolume, startPitch, loop, category));

        boolean apply = buf.readBoolean();
        if(apply) client.execute(() -> { while(!buffered.isEmpty()) client.getSoundManager().play(buffered.poll()); });
    }

    private static void updateFadeVolumeHandler(MinecraftClient client, ClientPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
        Identifier id = new Identifier(buf.readString());
        float target = buf.readFloat();
        int fadeLengthTicks = buf.readInt();

        client.execute(() -> {
            ISoundSystemUtil system = (ISoundSystemUtil) ((SoundManagerAccess) client.getSoundManager()).getSoundSystem();
            system.sendFadeVolume(id, target, fadeLengthTicks);
        });
    }

    private static void updateFadePitchHandler(MinecraftClient client, ClientPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
        Identifier id = new Identifier(buf.readString());
        float target = buf.readFloat();
        int fadeLengthTicks = buf.readInt();

        client.execute(() -> {
            ISoundSystemUtil system = (ISoundSystemUtil) ((SoundManagerAccess) client.getSoundManager()).getSoundSystem();
            system.sendFadePitch(id, target, fadeLengthTicks);
        });
    }
}
