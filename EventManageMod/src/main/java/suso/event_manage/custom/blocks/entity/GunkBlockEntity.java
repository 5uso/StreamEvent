package suso.event_manage.custom.blocks.entity;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtOps;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import suso.event_manage.custom.blocks.CustomBlocks;
import suso.event_manage.state_handlers.primatica.PrimaticaGunkInstance;

public class GunkBlockEntity extends BlockEntity {
    private BlockState previous;
    private int ticksLeft;

    public GunkBlockEntity(BlockPos pos, BlockState state) {
        super(CustomBlocks.GUNK_ENTITY, pos, state);

        previous = PrimaticaGunkInstance.previous == null ? Registry.BLOCK.get(new Identifier("air")).getDefaultState() : PrimaticaGunkInstance.previous;
        ticksLeft = PrimaticaGunkInstance.sendTicksLeft;
    }

    public void restore(World world) {
        world.setBlockState(pos, previous);
    }

    public BlockState getPrevious() {
        return previous;
    }

    public static void tick(World world, BlockPos pos, BlockState state, GunkBlockEntity be) {
        if(--be.ticksLeft < 0) be.restore(world);
    }

    @Override
    public void writeNbt(NbtCompound nbt) {
        if(previous != null) {
            NbtElement prev_nbt = BlockState.CODEC.encodeStart(NbtOps.INSTANCE, previous).getOrThrow(false, s -> {});
            nbt.put("previous", prev_nbt);
        }

        super.writeNbt(nbt);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);

        NbtElement prev_nbt = nbt.get("previous");
        if(prev_nbt != null) {
            previous = BlockState.CODEC.decode(NbtOps.INSTANCE, prev_nbt).getOrThrow(false, s -> {}).getFirst();
        } else {
            previous = Registry.BLOCK.get(new Identifier("air")).getDefaultState();
        }
    }
}
