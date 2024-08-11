package suso.event_common.custom.network.packets;


import net.minecraft.network.PacketByteBuf;

public class LoginCheckPacket {
    public final long timestamp;

    public LoginCheckPacket(long timestamp) {
        this.timestamp = timestamp;
    }

    public LoginCheckPacket(PacketByteBuf buf) {
        this.timestamp = buf.readLong();
    }

    public void write(PacketByteBuf buf) {
        buf.writeLong(timestamp);
    }
}
