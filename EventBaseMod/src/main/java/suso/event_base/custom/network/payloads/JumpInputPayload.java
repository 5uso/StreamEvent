package suso.event_base.custom.network.payloads;


import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;
import suso.event_base.custom.network.packets.JumpInputPacket;

public class JumpInputPayload extends JumpInputPacket implements CustomPayload {
    public static final Id<JumpInputPayload> ID = new Id<>(Identifier.of("suso", "jump_input"));
    public static final PacketCodec<PacketByteBuf, JumpInputPayload> CODEC = CustomPayload.codecOf(JumpInputPayload::write, JumpInputPayload::new);

    public JumpInputPayload(boolean pressed) {
        super(pressed);
    }

    public JumpInputPayload(final PacketByteBuf buf) {
        super(buf);
    }

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
