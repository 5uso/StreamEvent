package suso.event_base.custom.network.packets;


import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;

public class UpdateFadePitchPacket {
    public final Identifier id;
    public final float targetPitch;
    public final int fadeLengthTicks;

    public UpdateFadePitchPacket(Identifier id, float targetPitch, int fadeLengthTicks) {
        this.id = id;
        this.targetPitch = targetPitch;
        this.fadeLengthTicks = fadeLengthTicks;
    }

    public UpdateFadePitchPacket(PacketByteBuf buf) {
        this.id = buf.readIdentifier();
        this.targetPitch = buf.readFloat();
        this.fadeLengthTicks = buf.readInt();
    }

    public void write(PacketByteBuf buf) {
        buf.writeString(id.toString());
        buf.writeFloat(targetPitch);
        buf.writeInt(fadeLengthTicks);
    }
}
