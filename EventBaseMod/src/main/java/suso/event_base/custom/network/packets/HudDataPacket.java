package suso.event_base.custom.network.packets;


import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.network.PacketByteBuf;
import suso.event_base.custom.render.hud.CustomHud;

public class HudDataPacket {
    public final CustomHud.DataTypes type;
    public final PacketByteBuf buf;

    protected HudDataPacket(CustomHud.DataTypes type, PacketByteBuf buf) {
        this.type = type;
        this.buf = buf;
    }

    public HudDataPacket(PacketByteBuf buf) {
        this.type = CustomHud.DataTypes.values()[buf.readInt()];
        this.buf = PacketByteBufs.create();

        this.buf.writeBytes(buf);
    }

    public void write(PacketByteBuf buf) {
        buf.writeInt(type.ordinal());
        buf.writeBytes(this.buf);
    }
}
