package suso.event_base.custom.network.packets;


import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.network.PacketByteBuf;

public class HudDataPacket {
    public enum DataTypes {
        STATE, TIMER, FEED, AGILITY, PRIMATICA_SCORE, INFO, KILL
    }

    public final DataTypes type;
    public final PacketByteBuf buf;

    protected HudDataPacket(DataTypes type, PacketByteBuf buf) {
        this.type = type;
        this.buf = buf;
    }

    public HudDataPacket(PacketByteBuf buf) {
        this.type = DataTypes.values()[buf.readInt()];
        this.buf = PacketByteBufs.create();

        this.buf.writeBytes(buf);
    }

    public void write(PacketByteBuf buf) {
        buf.writeInt(type.ordinal());
        buf.writeBytes(this.buf);
    }
}
