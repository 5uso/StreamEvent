package suso.event_base.custom.network.payloads;


import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;
import suso.event_base.custom.network.packets.SetShaderUniformPacket;

public class SetShaderUniformPayload extends SetShaderUniformPacket implements CustomPayload {
    public static final Id<SetShaderUniformPayload> ID = new Id<>(Identifier.of("suso", "set_shader_uniform"));
    public static final PacketCodec<PacketByteBuf, SetShaderUniformPayload> CODEC = CustomPayload.codecOf(SetShaderUniformPayload::write, SetShaderUniformPayload::new);

    public SetShaderUniformPayload(String name, float... values) {
        super(name, values);
    }

    public SetShaderUniformPayload(String name, int... values) {
        super(name, values);
    }

    public SetShaderUniformPayload(final PacketByteBuf buf) {
        super(buf);
    }

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
