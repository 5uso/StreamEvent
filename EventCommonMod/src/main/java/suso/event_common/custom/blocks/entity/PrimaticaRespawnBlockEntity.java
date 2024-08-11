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

public class PrimaticaRespawnBlockEntity extends BlockEntity {
    public static BlockEntityType<PrimaticaRespawnBlockEntity> TYPE;

    protected boolean open = false;
    protected int color = 0xFFFFFFFF;

    public PrimaticaRespawnBlockEntity(BlockEntityType<? extends PrimaticaRespawnBlockEntity> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    public PrimaticaRespawnBlockEntity(BlockPos pos, BlockState state) {
        this(TYPE, pos, state);
    }

    @Override
    public void writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup wrapper) {
        nbt.putBoolean("open", open);
        nbt.putInt("color", color);
        super.writeNbt(nbt, wrapper);
    }

    @Override
    public void readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup wrapper) {
        super.read(nbt, wrapper);
        open = nbt.getBoolean("open");
        color = nbt.getInt("color");
    }

    @Nullable @Override
    public Packet<ClientPlayPacketListener> toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }

    @Override
    public NbtCompound toInitialChunkDataNbt(RegistryWrapper.WrapperLookup wrapper) {
        return createNbt(wrapper);
    }

    public int getColor() {
        return color;
    }
}
