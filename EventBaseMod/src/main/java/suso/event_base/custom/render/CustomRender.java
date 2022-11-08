package suso.event_base.custom.render;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.color.block.BlockColors;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.Shader;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.resource.ResourceManager;
import suso.event_base.client.shader.ShaderNetworking;
import suso.event_base.custom.blocks.CustomBlocks;

import java.io.IOException;

@Environment(EnvType.CLIENT)
public class CustomRender {
    private static Shader FEED_SHADER;

    private static Shader currentDrawShader;

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

    public static void setupCustomColors(BlockColors bc) {
        bc.registerColorProvider((state, world, pos, tintIndex) -> ShaderNetworking.colors.getOrDefault(pos, 0), CustomBlocks.GRAY_EMP, CustomBlocks.WHITE_EMP, CustomBlocks.PINK_EMP, CustomBlocks.PURPLE_EMP, CustomBlocks.BLUE_EMP, CustomBlocks.CYAN_EMP, CustomBlocks.LIGHT_BLUE_EMP, CustomBlocks.GREEN_EMP, CustomBlocks.LIME_EMP, CustomBlocks.YELLOW_EMP, CustomBlocks.ORANGE_EMP, CustomBlocks.RED_EMP);
    }

    public static void setupShaders(ResourceManager manager) {
        System.out.println("Loading custom shaders...");
        try {
            FEED_SHADER = new Shader(manager, "suso_feed", VertexFormats.POSITION_TEXTURE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void registerHud() {
        HudRenderCallback.EVENT.register(new HudTest());
    }

    public static Shader getFeedShader() {
        return FEED_SHADER;
    }

    public static Shader getCurrentDrawShader() {
        return currentDrawShader;
    }

    public static void setCurrentDrawShader(Shader shader) {
        currentDrawShader = shader;
    }
}
