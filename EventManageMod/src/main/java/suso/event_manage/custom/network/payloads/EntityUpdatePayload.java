package suso.event_manage.custom.network.payloads;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;
import suso.event_manage.custom.network.packets.EntityUpdatePacket;

public class EntityUpdatePayload extends EntityUpdatePacket implements CustomPayload {
    public static final Id<EntityUpdatePayload> ID = new Id<>(Identifier.of("suso", "entity_update"));
    public static final PacketCodec<PacketByteBuf, EntityUpdatePayload> CODEC = CustomPayload.codecOf(EntityUpdatePayload::write, EntityUpdatePayload::new);

    public EntityUpdatePayload(Entity entity, NbtCompound nbt) {
        super(entity, nbt);
    }

    public EntityUpdatePayload(final PacketByteBuf buf) {
        super(buf);
    }

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
