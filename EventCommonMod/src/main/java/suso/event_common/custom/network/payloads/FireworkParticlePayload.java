package suso.event_common.custom.network.payloads;


import net.minecraft.component.type.FireworkExplosionComponent;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;
import suso.event_common.custom.network.packets.FireworkParticlePacket;

import java.util.List;

public class FireworkParticlePayload extends FireworkParticlePacket implements CustomPayload {
    public static final Id<FireworkParticlePayload> ID = new Id<>(Identifier.of("suso", "firework_particle"));
    public static final PacketCodec<PacketByteBuf, FireworkParticlePayload> CODEC = CustomPayload.codecOf(FireworkParticlePayload::write, FireworkParticlePayload::new);

    public FireworkParticlePayload(double x, double y, double z, double vx, double vy, double vz, List<FireworkExplosionComponent> fireworks) {
        super(x, y, z, vx, vy, vz, fireworks);
    }

    public FireworkParticlePayload(final PacketByteBuf buf) {
        super(buf);
    }

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
