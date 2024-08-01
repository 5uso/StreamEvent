package suso.event_base.custom.network.packets;


import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;

public class SetPostShaderPacket {
    public final Identifier id;

    public SetPostShaderPacket(Identifier id) {
        this.id = id;
    }

    public SetPostShaderPacket(PacketByteBuf buf) {
        this.id = buf.readIdentifier();
    }

    public void write(PacketByteBuf buf) {
        buf.writeString(id.toString());
    }
}
