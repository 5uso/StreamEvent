package suso.event_base.custom.render;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.minecraft.client.color.block.BlockColors;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.util.math.BlockPos;
import suso.event_base.client.shader.ShaderNetworking;
import suso.event_base.custom.blocks.CustomBlocks;

import java.util.HashMap;
import java.util.Map;

@Environment(EnvType.CLIENT)
public class CustomRender {
    public static void setupRenderLayers() {
        BlockRenderLayerMap rl = BlockRenderLayerMap.INSTANCE;
        rl.putBlock(CustomBlocks.GRAY_HOLOBLOCK, RenderLayer.getTranslucent());
        rl.putBlock(CustomBlocks.WHITE_HOLOBLOCK, RenderLayer.getTranslucent());
        rl.putBlock(CustomBlocks.PINK_HOLOBLOCK, RenderLayer.getTranslucent());
        rl.putBlock(CustomBlocks.PURPLE_HOLOBLOCK, RenderLayer.getTranslucent());
        rl.putBlock(CustomBlocks.BLUE_HOLOBLOCK, RenderLayer.getTranslucent());
        rl.putBlock(CustomBlocks.CYAN_HOLOBLOCK, RenderLayer.getTranslucent());
        rl.putBlock(CustomBlocks.LIGHT_BLUE_HOLOBLOCK, RenderLayer.getTranslucent());
        rl.putBlock(CustomBlocks.GREEN_HOLOBLOCK, RenderLayer.getTranslucent());
        rl.putBlock(CustomBlocks.LIME_HOLOBLOCK, RenderLayer.getTranslucent());
        rl.putBlock(CustomBlocks.YELLOW_HOLOBLOCK, RenderLayer.getTranslucent());
        rl.putBlock(CustomBlocks.ORANGE_HOLOBLOCK, RenderLayer.getTranslucent());
        rl.putBlock(CustomBlocks.RED_HOLOBLOCK, RenderLayer.getTranslucent());

        rl.putBlock(CustomBlocks.GRAY_EMP, RenderLayer.getTranslucent());
        rl.putBlock(CustomBlocks.WHITE_EMP, RenderLayer.getTranslucent());
        rl.putBlock(CustomBlocks.PINK_EMP, RenderLayer.getTranslucent());
        rl.putBlock(CustomBlocks.PURPLE_EMP, RenderLayer.getTranslucent());
        rl.putBlock(CustomBlocks.BLUE_EMP, RenderLayer.getTranslucent());
        rl.putBlock(CustomBlocks.CYAN_EMP, RenderLayer.getTranslucent());
        rl.putBlock(CustomBlocks.LIGHT_BLUE_EMP, RenderLayer.getTranslucent());
        rl.putBlock(CustomBlocks.GREEN_EMP, RenderLayer.getTranslucent());
        rl.putBlock(CustomBlocks.LIME_EMP, RenderLayer.getTranslucent());
        rl.putBlock(CustomBlocks.YELLOW_EMP, RenderLayer.getTranslucent());
        rl.putBlock(CustomBlocks.ORANGE_EMP, RenderLayer.getTranslucent());
        rl.putBlock(CustomBlocks.RED_EMP, RenderLayer.getTranslucent());
    }

}
