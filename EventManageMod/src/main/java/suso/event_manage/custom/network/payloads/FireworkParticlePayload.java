package suso.event_manage.custom.network.payloads;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;
import suso.event_manage.custom.network.packets.FireworkParticlePacket;

public class FireworkParticlePayload extends FireworkParticlePacket implements CustomPayload {
    public static final Id<FireworkParticlePayload> ID = new Id<>(Identifier.of("suso", "firework_particle"));
    public static final PacketCodec<PacketByteBuf, FireworkParticlePayload> CODEC = CustomPayload.codecOf(FireworkParticlePayload::write, FireworkParticlePayload::new);

    public FireworkParticlePayload(double x, double y, double z, double vx, double vy, double vz, NbtCompound firework) {
        super(x, y, z, vx, vy, vz, firework);
    }

    public FireworkParticlePayload(final PacketByteBuf buf) {
        super(buf);
    }

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
