package suso.event_manage.custom.network.packets;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

public class SetBlockColorPacket {
    private final BlockPos pos;
    private final boolean colorSet;
    private final int color;

    public SetBlockColorPacket(BlockPos pos, int color) {
        this.pos = pos;
        this.colorSet = true;
        this.color = color;
    }

    public SetBlockColorPacket(BlockPos pos) {
        this.pos = pos;
        this.colorSet = false;
        this.color = -1;
    }

    public SetBlockColorPacket(PacketByteBuf buf) {
        this.pos = buf.readBlockPos();
        this.colorSet = buf.readBoolean();
        this.color = colorSet ? buf.readInt() : 0;
    }

    public void write(PacketByteBuf buf) {
        buf.writeBlockPos(pos);
        buf.writeBoolean(colorSet);
        if(colorSet) {
            buf.writeInt(color);
        }
    }
}
