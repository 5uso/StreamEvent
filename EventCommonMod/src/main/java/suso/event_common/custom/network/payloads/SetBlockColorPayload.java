package suso.event_common.custom.network.payloads;


import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import suso.event_common.custom.network.packets.SetBlockColorPacket;

public class SetBlockColorPayload extends SetBlockColorPacket implements CustomPayload {
    public static final Id<SetBlockColorPayload> ID = new Id<>(Identifier.of("suso", "set_block_color"));
    public static final PacketCodec<PacketByteBuf, SetBlockColorPayload> CODEC = CustomPayload.codecOf(SetBlockColorPayload::write, SetBlockColorPayload::new);

    public SetBlockColorPayload(BlockPos pos, int color) {
        super(pos, color);
    }

    public SetBlockColorPayload(BlockPos pos) {
        super(pos);
    }

    public SetBlockColorPayload(final PacketByteBuf buf) {
        super(buf);
    }

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
