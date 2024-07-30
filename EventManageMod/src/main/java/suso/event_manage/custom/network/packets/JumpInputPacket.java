package suso.event_manage.custom.network.packets;

import net.minecraft.network.PacketByteBuf;

public class JumpInputPacket {
    public final boolean pressed;

    public JumpInputPacket(boolean pressed) {
        this.pressed = pressed;
    }

    public JumpInputPacket(PacketByteBuf buf) {
        this.pressed = buf.readBoolean();
    }

    public void write(PacketByteBuf buf) {
        buf.writeBoolean(pressed);
    }
}
