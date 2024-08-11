package suso.event_common.custom.network.payloads;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.s2c.login.LoginQueryRequestPayload;
import net.minecraft.util.Identifier;

public record LoginCheckPayload(long timestamp) implements LoginQueryRequestPayload {
    public static Identifier ID = Identifier.of("suso", "login_check");

    @Override
    public Identifier id() {
        return ID;
    }

    @Override
    public void write(PacketByteBuf buf) {
        buf.writeLong(timestamp);
    }
}
