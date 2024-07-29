package suso.event_manage.custom.network.packets;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;

public class EntityUpdatePacket {
    private final int entityId;
    private final NbtCompound nbt;

    public EntityUpdatePacket(Entity entity, NbtCompound nbt) {
        this.entityId = entity.getId();
        this.nbt = nbt;
    }

    public EntityUpdatePacket(PacketByteBuf buf) {
        this.entityId = buf.readInt();
        this.nbt = buf.readNbt();
    }

    public void write(PacketByteBuf buf) {
        buf.writeInt(entityId);
        buf.writeNbt(nbt);
    }
}
