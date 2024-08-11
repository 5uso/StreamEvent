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

public class PrimaticaDoorBlockEntity extends BlockEntity {
    public static BlockEntityType<PrimaticaDoorBlockEntity> TYPE;

    protected boolean open = false;
    protected int color = 0xFFFFFFFF;
    protected boolean diagonal = false;

    public PrimaticaDoorBlockEntity(BlockEntityType<? extends PrimaticaDoorBlockEntity> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    public PrimaticaDoorBlockEntity(BlockPos pos, BlockState state) {
        this(TYPE, pos, state);
    }

    @Override
    public void writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup wrapper) {
        nbt.putBoolean("open", open);
        nbt.putInt("color", color);
        nbt.putBoolean("diagonal", diagonal);
        super.writeNbt(nbt, wrapper);
    }

    @Override
    public void readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup wrapper) {
        super.readNbt(nbt, wrapper);
        open = nbt.getBoolean("open");
        color = nbt.getInt("color");
        diagonal = nbt.getBoolean("diagonal");
    }

    @Nullable
    @Override
    public Packet<ClientPlayPacketListener> toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }

    @Override
    public NbtCompound toInitialChunkDataNbt(RegistryWrapper.WrapperLookup wrapper) {
        return createNbt(wrapper);
    }

    public boolean isDiagonal() {
        return diagonal;
    }

    public int getColor() {
        return color;
    }
}
