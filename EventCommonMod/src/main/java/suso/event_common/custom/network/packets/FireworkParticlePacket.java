package suso.event_common.custom.network.packets;


import net.minecraft.component.type.FireworkExplosionComponent;
import net.minecraft.network.PacketByteBuf;

import java.util.ArrayList;
import java.util.List;

public class FireworkParticlePacket {
    public final double x, y, z, vx, vy, vz;
    public final List<FireworkExplosionComponent> fireworks;

    public FireworkParticlePacket(double x, double y, double z, double vx, double vy, double vz, List<FireworkExplosionComponent> fireworks) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.vx = vx;
        this.vy = vy;
        this.vz = vz;
        this.fireworks = fireworks;
    }

    public FireworkParticlePacket(PacketByteBuf buf) {
        this.x = buf.readDouble();
        this.y = buf.readDouble();
        this.z = buf.readDouble();
        this.vx = buf.readDouble();
        this.vy = buf.readDouble();
        this.vz = buf.readDouble();

        int length = buf.readInt();
        this.fireworks = new ArrayList<>(length);
        for(int i = 0; i < length; i++) {
            fireworks.add(FireworkExplosionComponent.PACKET_CODEC.decode(buf));
        }
    }

    public void write(PacketByteBuf buf) {
        buf.writeDouble(x);
        buf.writeDouble(y);
        buf.writeDouble(z);
        buf.writeDouble(vx);
        buf.writeDouble(vy);
        buf.writeDouble(vz);

        buf.writeInt(fireworks.size());
        fireworks.forEach(component -> FireworkExplosionComponent.PACKET_CODEC.encode(buf, component));
    }
}
