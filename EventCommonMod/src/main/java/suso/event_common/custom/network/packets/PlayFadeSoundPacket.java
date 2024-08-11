package suso.event_common.custom.network.packets;


import net.minecraft.network.PacketByteBuf;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.Identifier;

public class PlayFadeSoundPacket {
    public final Identifier id;
    public final float startingVolume;
    public final float startingPitch;
    public final boolean loop;
    public final SoundCategory category;
    public final boolean apply;

    public PlayFadeSoundPacket(Identifier id, float startingVolume, float startingPitch, boolean loop, SoundCategory category, boolean apply) {
        this.id = id;
        this.startingVolume = startingVolume;
        this.startingPitch = startingPitch;
        this.loop = loop;
        this.category = category;
        this.apply = apply;
    }

    public PlayFadeSoundPacket(PacketByteBuf buf) {
        this.id = buf.readIdentifier();
        this.startingVolume = buf.readFloat();
        this.startingPitch = buf.readFloat();
        this.loop = buf.readBoolean();
        this.category = SoundCategory.values()[buf.readByte()];
        this.apply = buf.readBoolean();
    }

    public void write(PacketByteBuf buf) {
        buf.writeString(id.toString());
        buf.writeFloat(startingVolume);
        buf.writeFloat(startingPitch);
        buf.writeBoolean(loop);
        buf.writeByte(category.ordinal());
        buf.writeBoolean(apply);
    }
}
