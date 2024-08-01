package suso.event_base.mixin.client;

import net.minecraft.block.*;
import net.minecraft.block.enums.DoubleBlockHalf;
import net.minecraft.client.color.block.BlockColors;
import net.minecraft.client.color.world.BiomeColors;
import net.minecraft.state.property.Property;
import net.minecraft.world.biome.FoliageColors;
import net.minecraft.world.biome.GrassColors;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import suso.event_base.custom.render.CustomRender;

@Mixin(BlockColors.class)
public abstract class BlockColorsMixin {
    @Inject(
            method = "create",
            at = @At("HEAD"),
            cancellable = true
    )
    private static void registerCustomColors(CallbackInfoReturnable<BlockColors> cir) {
        BlockColors blockColors = new BlockColors();

        blockColors.registerColorProvider((state, world, pos, tintIndex) -> 0xFF000000 | (world != null && pos != null ? BiomeColors.getGrassColor(world, state.get(TallPlantBlock.HALF) == DoubleBlockHalf.UPPER ? pos.down() : pos) : -1), Blocks.LARGE_FERN, Blocks.TALL_GRASS);
        blockColors.registerColorProperty(TallPlantBlock.HALF, Blocks.LARGE_FERN, Blocks.TALL_GRASS);

        blockColors.registerColorProvider((state, world, pos, tintIndex) ->  0xFF000000 | (world != null && pos != null ? BiomeColors.getGrassColor(world, pos) : GrassColors.getColor(0.5D, 1.0D)), Blocks.GRASS_BLOCK, Blocks.FERN, Blocks.SHORT_GRASS, Blocks.POTTED_FERN);

        blockColors.registerColorProvider((state, world, pos, tintIndex) -> 0xFF000000 | FoliageColors.getSpruceColor(), Blocks.SPRUCE_LEAVES);

        blockColors.registerColorProvider((state, world, pos, tintIndex) -> 0xFF000000 | FoliageColors.getBirchColor(), Blocks.BIRCH_LEAVES);

        blockColors.registerColorProvider((state, world, pos, tintIndex) -> 0xFF000000 | (world != null && pos != null ? BiomeColors.getFoliageColor(world, pos) : FoliageColors.getDefaultColor()), Blocks.OAK_LEAVES, Blocks.JUNGLE_LEAVES, Blocks.ACACIA_LEAVES, Blocks.DARK_OAK_LEAVES, Blocks.VINE, Blocks.MANGROVE_LEAVES);

        blockColors.registerColorProvider((state, world, pos, tintIndex) -> 0xFF000000 | (world != null && pos != null ? BiomeColors.getWaterColor(world, pos) : -1), Blocks.WATER, Blocks.BUBBLE_COLUMN, Blocks.WATER_CAULDRON);

        blockColors.registerColorProvider((state, world, pos, tintIndex) -> 0xFF000000 | RedstoneWireBlock.getWireColor(state.get(RedstoneWireBlock.POWER)), Blocks.REDSTONE_WIRE);
        blockColors.registerColorProperty(RedstoneWireBlock.POWER, Blocks.REDSTONE_WIRE);

        blockColors.registerColorProvider((state, world, pos, tintIndex) -> 0xFF000000 | (world != null && pos != null ? BiomeColors.getGrassColor(world, pos) : -1), Blocks.SUGAR_CANE);

        blockColors.registerColorProvider((state, world, pos, tintIndex) -> 0xFFE0C71C, Blocks.ATTACHED_MELON_STEM, Blocks.ATTACHED_PUMPKIN_STEM);

        blockColors.registerColorProvider((state, world, pos, tintIndex) -> {
            int i = state.get(StemBlock.AGE);
            int j = i * 32;
            int k = 255 - i * 8;
            int l = i * 4;
            return 0xFF000000 | (j << 16 | k << 8 | l);
        }, Blocks.MELON_STEM, Blocks.PUMPKIN_STEM);
        blockColors.registerColorProperty(StemBlock.AGE, Blocks.MELON_STEM, Blocks.PUMPKIN_STEM);

        blockColors.registerColorProvider((state, world, pos, tintIndex) -> world != null && pos != null ? 0xFF208030 : 0xFF71C35C, Blocks.LILY_PAD);

        CustomRender.setupCustomColors(blockColors);

        cir.setReturnValue(blockColors);
        cir.cancel();
    }
}
