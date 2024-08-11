package suso.event_common.custom.blocks;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;

public class EMPBlock extends Block {
    public static final Settings SETTINGS = AbstractBlock.Settings.create()
            .strength(-1.0F, 3600000.0F)
            .dropsNothing()
            .nonOpaque()
            .noCollision();

    public EMPBlock(Settings settings) {
        super(settings);
    }

    public EMPBlock() {
        this(SETTINGS);
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return VoxelShapes.empty();
    }
}
