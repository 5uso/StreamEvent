package suso.event_base.custom.network.packets;


import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;

public class FireworkParticlePacket {
    private final double x, y, z, vx, vy, vz;
    private final NbtCompound firework;

    public FireworkParticlePacket(double x, double y, double z, double vx, double vy, double vz, NbtCompound firework) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.vx = vx;
        this.vy = vy;
        this.vz = vz;
        this.firework = firework;
    }

    public FireworkParticlePacket(PacketByteBuf buf) {
        this.x = buf.readDouble();
        this.y = buf.readDouble();
        this.z = buf.readDouble();
        this.vx = buf.readDouble();
        this.vy = buf.readDouble();
        this.vz = buf.readDouble();
        this.firework = buf.readNbt();
    }

    public void write(PacketByteBuf buf) {
        buf.writeDouble(x);
        buf.writeDouble(y);
        buf.writeDouble(z);
        buf.writeDouble(vx);
        buf.writeDouble(vy);
        buf.writeDouble(vz);
        buf.writeNbt(firework);
    }
}
