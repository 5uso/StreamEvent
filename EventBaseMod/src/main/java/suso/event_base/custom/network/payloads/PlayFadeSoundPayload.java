package suso.event_base.custom.network.payloads;


import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.Identifier;
import suso.event_base.custom.network.packets.PlayFadeSoundPacket;

public class PlayFadeSoundPayload extends PlayFadeSoundPacket implements CustomPayload {
    public static final CustomPayload.Id<PlayFadeSoundPayload> ID = new CustomPayload.Id<>(Identifier.of("suso", "play_fade_sound"));
    public static final PacketCodec<PacketByteBuf, PlayFadeSoundPayload> CODEC = CustomPayload.codecOf(PlayFadeSoundPayload::write, PlayFadeSoundPayload::new);

    public PlayFadeSoundPayload(Identifier id, float startingVolume, float startingPitch, boolean loop, SoundCategory category, boolean apply) {
        super(id, startingVolume, startingPitch, loop, category, apply);
    }

    public PlayFadeSoundPayload(final PacketByteBuf buf) {
        super(buf);
    }

    @Override
    public CustomPayload.Id<? extends CustomPayload> getId() {
        return ID;
    }
}
