package suso.event_common.custom.blocks.entity;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;

public class PrimaticaPowerupBlockEntity extends BlockEntity {
    public enum Powerups {
        AGILITY, BRIDGE, GRAVITY, EMP, ARROW, GUNK
    }

    public static BlockEntityType<PrimaticaPowerupBlockEntity> TYPE;

    protected boolean collected = false;
    protected Powerups type = Powerups.AGILITY;

    public PrimaticaPowerupBlockEntity(BlockEntityType<? extends PrimaticaPowerupBlockEntity> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    public PrimaticaPowerupBlockEntity(BlockPos pos, BlockState state) {
        this(TYPE, pos, state);
    }

    @Override
    public void writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup wrapper) {
        nbt.putBoolean("collected", collected);
        nbt.putByte("type", (byte) type.ordinal());
        super.writeNbt(nbt, wrapper);
    }

    @Override
    public void readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup wrapper) {
        super.readNbt(nbt, wrapper);
        collected = nbt.getBoolean("collected");
        type = Powerups.values()[nbt.getByte("type")];
    }

    public Powerups getPowerupType() {
        return type;
    }

    @Nullable @Override
    public Packet<ClientPlayPacketListener> toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }

    @Override
    public NbtCompound toInitialChunkDataNbt(RegistryWrapper.WrapperLookup wrapper) {
        return createNbt(wrapper);
    }
}
