package suso.event_common.custom.blocks;

import com.mojang.serialization.MapCodec;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import org.jetbrains.annotations.Nullable;
import suso.event_common.custom.blocks.entity.PrimaticaPowerupBlockEntity;

public class PrimaticaPowerupBlock extends BlockWithEntity {
    public static final Settings SETTINGS = AbstractBlock.Settings.create()
            .strength(-1.0F, 3600000.0F)
            .dropsNothing()
            .nonOpaque()
            .noCollision();

    public static final MapCodec<PrimaticaPowerupBlock> CODEC = createCodec(PrimaticaPowerupBlock::new);

    private final BlockEntityType.BlockEntityFactory<? extends PrimaticaPowerupBlockEntity> blockEntityFactory;

    public PrimaticaPowerupBlock(Settings settings, BlockEntityType.BlockEntityFactory<? extends PrimaticaPowerupBlockEntity> factory) {
        super(settings);
        this.blockEntityFactory = factory;
    }

    private PrimaticaPowerupBlock(Settings settings) {
        this(settings, PrimaticaPowerupBlockEntity::new);
    }

    public PrimaticaPowerupBlock(BlockEntityType.BlockEntityFactory<? extends PrimaticaPowerupBlockEntity> factory) {
        this(SETTINGS, factory);
    }

    @Nullable @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return blockEntityFactory.create(pos, state);
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.ENTITYBLOCK_ANIMATED;
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return VoxelShapes.empty();
    }

    @Override
    protected MapCodec<PrimaticaPowerupBlock> getCodec() {
        return CODEC;
    }
}
