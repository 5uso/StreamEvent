package suso.event_manage.custom.blocks.entity;

import com.mojang.datafixers.util.Pair;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtOps;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import suso.event_manage.custom.blocks.CustomBlocks;
import suso.event_manage.state_handlers.primatica.PrimaticaGunkInstance;

import java.util.Optional;

public class GunkBlockEntity extends BlockEntity {
    private BlockState previous;
    private int ticksLeft;

    public GunkBlockEntity(BlockPos pos, BlockState state) {
        super(CustomBlocks.GUNK_ENTITY, pos, state);

        previous = PrimaticaGunkInstance.previous == null ? Registries.BLOCK.get(Identifier.ofVanilla("air")).getDefaultState() : PrimaticaGunkInstance.previous;
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
    public void writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup wrapper) {
        if(previous != null) {
            Optional<NbtElement> prev_nbt = BlockState.CODEC.encodeStart(NbtOps.INSTANCE, previous).result();
            prev_nbt.ifPresent(nbtElement -> nbt.put("previous", nbtElement));
        }

        super.writeNbt(nbt, wrapper);
    }

    @Override
    public void readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup wrapper) {
        super.readNbt(nbt, wrapper);

        NbtElement prev_nbt = nbt.get("previous");
        if(prev_nbt != null) {
            Optional<Pair<BlockState, NbtElement>> prev_state = BlockState.CODEC.decode(NbtOps.INSTANCE, prev_nbt).result();
            prev_state.ifPresentOrElse(
                    pair -> previous = pair.getFirst(),
                    () -> previous = Registries.BLOCK.get(Identifier.ofVanilla("air")).getDefaultState()
            );
        } else {
            previous = Registries.BLOCK.get(Identifier.ofVanilla("air")).getDefaultState();
        }
    }
}
