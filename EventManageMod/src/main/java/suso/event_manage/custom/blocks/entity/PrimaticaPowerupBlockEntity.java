package suso.event_manage.custom.blocks.entity;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;
import suso.event_manage.custom.blocks.CustomBlocks;
import suso.event_manage.state_handlers.primatica.PrimaticaInfo;

public class PrimaticaPowerupBlockEntity extends BlockEntity {
    private boolean collected = false;
    private PrimaticaInfo.Powerups type = PrimaticaInfo.Powerups.AGILITY;

    public PrimaticaPowerupBlockEntity(BlockPos pos, BlockState state) {
        super(CustomBlocks.PRIMATICA_POWERUP_ENTITY, pos, state);
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
        type = PrimaticaInfo.Powerups.values()[nbt.getByte("type")];
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
