package suso.event_manage.custom.network.packets;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;

public class SetShaderUniformPacket {
    private final String name;
    private final boolean floating;
    private final float[] valuesFloat;
    private final int[] valuesInt;

    public SetShaderUniformPacket(String name, float... values) {
        this.name = name;
        this.floating = true;
        this.valuesFloat = values;
        this.valuesInt = null;
    }

    public SetShaderUniformPacket(String name, int... values) {
        this.name = name;
        this.floating = true;
        this.valuesFloat = null;
        this.valuesInt = values;
    }

    public SetShaderUniformPacket(PacketByteBuf buf) {
        this.name = buf.readString();
        this.floating = buf.readBoolean();
        int length = buf.readInt();
        this.valuesFloat = floating ? new float[length] : null;
        this.valuesInt = floating ? null : new int[length];

        if(floating) {
            for(int i = 0; i < length; i++) valuesFloat[i] = buf.readFloat();
        } else {
            for(int i = 0; i < length; i++) valuesInt[i] = buf.readInt();
        }
    }

    public void write(PacketByteBuf buf) {
        buf.writeString(name);
        buf.writeBoolean(floating);
        buf.writeInt(floating ? valuesFloat.length : valuesInt.length);

        if(floating) {
            for(float f : valuesFloat) buf.writeFloat(f);
        } else {
            for(int i : valuesInt) buf.writeInt(i);
        }
    }
}
