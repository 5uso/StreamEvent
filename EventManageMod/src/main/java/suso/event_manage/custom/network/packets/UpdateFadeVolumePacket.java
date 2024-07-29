package suso.event_manage.custom.network.packets;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.Identifier;

public class UpdateFadeVolumePacket {
    private final Identifier id;
    private final float targetVolume;
    private final int fadeLengthTicks;

    public UpdateFadeVolumePacket(Identifier id, float targetVolume, int fadeLengthTicks) {
        this.id = id;
        this.targetVolume = targetVolume;
        this.fadeLengthTicks = fadeLengthTicks;
    }

    public UpdateFadeVolumePacket(PacketByteBuf buf) {
        this.id = buf.readIdentifier();
        this.targetVolume = buf.readFloat();
        this.fadeLengthTicks = buf.readInt();
    }

    public void write(PacketByteBuf buf) {
        buf.writeString(id.toString());
        buf.writeFloat(targetVolume);
        buf.writeInt(fadeLengthTicks);
    }
}
