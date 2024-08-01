package suso.event_base.custom.network.payloads;


import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;
import suso.event_base.custom.network.packets.UpdateFadeVolumePacket;

public class UpdateFadeVolumePayload extends UpdateFadeVolumePacket implements CustomPayload {
    public static final Id<UpdateFadeVolumePayload> ID = new Id<>(Identifier.of("suso", "update_fade_volume"));
    public static final PacketCodec<PacketByteBuf, UpdateFadeVolumePayload> CODEC = CustomPayload.codecOf(UpdateFadeVolumePayload::write, UpdateFadeVolumePayload::new);

    public UpdateFadeVolumePayload(Identifier id, float targetVolume, int fadeLengthTicks) {
        super(id, targetVolume, fadeLengthTicks);
    }

    public UpdateFadeVolumePayload(final PacketByteBuf buf) {
        super(buf);
    }

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
