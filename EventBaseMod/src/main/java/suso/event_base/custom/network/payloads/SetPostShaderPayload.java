package suso.event_base.custom.network.payloads;


import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;
import suso.event_base.custom.network.packets.SetPostShaderPacket;

public class SetPostShaderPayload extends SetPostShaderPacket implements CustomPayload {
    public static final Id<SetPostShaderPayload> ID = new Id<>(Identifier.of("suso", "set_post_shader"));
    public static final PacketCodec<PacketByteBuf, SetPostShaderPayload> CODEC = CustomPayload.codecOf(SetPostShaderPayload::write, SetPostShaderPayload::new);

    public SetPostShaderPayload(Identifier id) {
        super(id);
    }

    public SetPostShaderPayload(final PacketByteBuf buf) {
        super(buf);
    }

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
