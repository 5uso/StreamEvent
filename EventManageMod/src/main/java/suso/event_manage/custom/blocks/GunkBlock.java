package suso.event_manage.custom.blocks;

import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import suso.event_manage.custom.blocks.entity.GunkBlockEntity;

public class GunkBlock extends BlockWithEntity implements BlockEntityProvider {
    public GunkBlock(Settings settings) {
        super(settings);
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return validateTicker(type, CustomBlocks.GUNK_ENTITY, GunkBlockEntity::tick);
    }

    @Nullable @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new GunkBlockEntity(pos, state);
    }
}
