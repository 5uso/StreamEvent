package suso.event_base.custom.blocks.entity;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import suso.event_base.custom.blocks.CustomBlocks;

public class GunkBlockEntityClient extends BlockEntity {
    public GunkBlockEntityClient(BlockPos pos, BlockState state) {
        super(CustomBlocks.GUNK_ENTITY, pos, state);
    }
}
