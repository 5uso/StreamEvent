package suso.event_base.custom.blocks;

import com.mojang.serialization.MapCodec;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import org.jetbrains.annotations.Nullable;
import suso.event_base.custom.blocks.entity.PrimaticaPowerupBlockEntity;

public class PrimaticaPowerupBlock extends BlockWithEntity {
    public static final MapCodec<PrimaticaPowerupBlock> CODEC = createCodec(PrimaticaPowerupBlock::new);
    protected PrimaticaPowerupBlock(Settings settings) {
        super(settings);
    }

    @Nullable @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new PrimaticaPowerupBlockEntity(pos, state);
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.ENTITYBLOCK_ANIMATED;
    }

    @SuppressWarnings("deprecation") @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return VoxelShapes.empty();
    }

    @Override
    protected MapCodec<PrimaticaPowerupBlock> getCodec() {
        return CODEC;
    }
}
