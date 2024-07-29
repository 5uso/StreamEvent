package suso.event_manage.custom.network.packets;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;

public class UpdateFadePitchPacket {
    private final Identifier id;
    private final float targetPitch;
    private final int fadeLengthTicks;

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
