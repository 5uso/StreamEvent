package suso.event_manage.custom.blocks.entity;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.Packet;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;
import suso.event_manage.custom.blocks.CustomBlocks;

public class PrimaticaDoorBlockEntity extends BlockEntity {
    private boolean open = false;
    private int color = 0xFFFFFFFF;
    private boolean diagonal = false;

    public PrimaticaDoorBlockEntity(BlockPos pos, BlockState state) {
        super(CustomBlocks.PRIMATICA_DOOR_ENTITY, pos, state);
    }

    @Override
    public void writeNbt(NbtCompound nbt) {
        nbt.putBoolean("open", open);
        nbt.putInt("color", color);
        nbt.putBoolean("diagonal", diagonal);
        super.writeNbt(nbt);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
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
    public NbtCompound toInitialChunkDataNbt() {
        return createNbt();
    }
}
