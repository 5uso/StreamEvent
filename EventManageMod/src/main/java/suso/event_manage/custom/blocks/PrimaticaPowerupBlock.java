package suso.event_manage.custom.blocks;

import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;
import suso.event_manage.custom.blocks.entity.PrimaticaPowerupBlockEntity;

public class PrimaticaPowerupBlock extends BlockWithEntity {
    public PrimaticaPowerupBlock(Settings settings) {
        super(settings);
    }

    @Nullable @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new PrimaticaPowerupBlockEntity(pos, state);
    }
}
