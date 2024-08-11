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
import suso.event_common.custom.blocks.entity.PrimaticaRespawnBlockEntity;

public class PrimaticaRespawnBlock extends BlockWithEntity {
    public static final Settings SETTINGS = AbstractBlock.Settings.create()
            .strength(-1.0F, 3600000.0F)
            .dropsNothing()
            .nonOpaque()
            .noCollision();

    public static final MapCodec<PrimaticaRespawnBlock> CODEC = createCodec(PrimaticaRespawnBlock::new);

    private final BlockEntityType.BlockEntityFactory<? extends PrimaticaRespawnBlockEntity> blockEntityFactory;

    public PrimaticaRespawnBlock(Settings settings, BlockEntityType.BlockEntityFactory<? extends PrimaticaRespawnBlockEntity> factory) {
        super(settings);
        this.blockEntityFactory = factory;
    }

    private PrimaticaRespawnBlock(Settings settings) {
        this(settings, PrimaticaRespawnBlockEntity::new);
    }

    public PrimaticaRespawnBlock(BlockEntityType.BlockEntityFactory<? extends PrimaticaRespawnBlockEntity> factory) {
        this(SETTINGS, factory);
    }

    @Nullable @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new PrimaticaRespawnBlockEntity(pos, state);
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
    protected MapCodec<PrimaticaRespawnBlock> getCodec() {
        return CODEC;
    }
}
