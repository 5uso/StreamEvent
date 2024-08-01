package suso.event_base.custom.network.payloads;


import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;
import suso.event_base.custom.network.packets.LoginCheckPacket;

public class LoginCheckPayload extends LoginCheckPacket implements CustomPayload {
    public static final Id<LoginCheckPayload> ID = new Id<>(Identifier.of("suso", "login_check"));
    public static final PacketCodec<PacketByteBuf, LoginCheckPayload> CODEC = CustomPayload.codecOf(LoginCheckPayload::write, LoginCheckPayload::new);

    public LoginCheckPayload(long timestamp) {
        super(timestamp);
    }

    public LoginCheckPayload(final PacketByteBuf buf) {
        super(buf);
    }

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
