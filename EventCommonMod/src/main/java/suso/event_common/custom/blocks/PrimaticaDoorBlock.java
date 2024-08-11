package suso.event_common.custom.blocks;

import com.mojang.serialization.MapCodec;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import org.jetbrains.annotations.Nullable;
import suso.event_common.custom.blocks.entity.PrimaticaDoorBlockEntity;

public class PrimaticaDoorBlock extends BlockWithEntity {
    public static final Settings SETTINGS = AbstractBlock.Settings.create()
            .strength(-1.0F, 3600000.0F)
            .dropsNothing()
            .nonOpaque()
            .noCollision();

    public static final MapCodec<PrimaticaDoorBlock> CODEC = createCodec(PrimaticaDoorBlock::new);
    public static final DirectionProperty FACING = HorizontalFacingBlock.FACING;

    private final BlockEntityType.BlockEntityFactory<? extends PrimaticaDoorBlockEntity> blockEntityFactory;

    public PrimaticaDoorBlock(Settings settings, BlockEntityType.BlockEntityFactory<? extends PrimaticaDoorBlockEntity> factory) {
        super(settings);
        this.setDefaultState(this.stateManager.getDefaultState().with(FACING, Direction.NORTH));
        this.blockEntityFactory = factory;
    }

    private PrimaticaDoorBlock(Settings settings) {
        this(settings, PrimaticaDoorBlockEntity::new);
    }

    public PrimaticaDoorBlock(BlockEntityType.BlockEntityFactory<? extends PrimaticaDoorBlockEntity> factory) {
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
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Override
    protected MapCodec<PrimaticaDoorBlock> getCodec() {
        return CODEC;
    }
}
