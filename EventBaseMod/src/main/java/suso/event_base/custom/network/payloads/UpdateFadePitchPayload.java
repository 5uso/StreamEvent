package suso.event_base.custom.network.payloads;


import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;
import suso.event_base.custom.network.packets.UpdateFadePitchPacket;

public class UpdateFadePitchPayload extends UpdateFadePitchPacket implements CustomPayload {
    public static final Id<UpdateFadePitchPayload> ID = new Id<>(Identifier.of("suso", "update_fade_pitch"));
    public static final PacketCodec<PacketByteBuf, UpdateFadePitchPayload> CODEC = CustomPayload.codecOf(UpdateFadePitchPayload::write, UpdateFadePitchPayload::new);

    public UpdateFadePitchPayload(Identifier id, float targetPitch, int fadeLengthTicks) {
        super(id, targetPitch, fadeLengthTicks);
    }

    public UpdateFadePitchPayload(final PacketByteBuf buf) {
        super(buf);
    }

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
