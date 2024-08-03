package suso.event_base.custom.render;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.color.block.BlockColors;
import net.minecraft.client.gl.ShaderProgram;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.resource.ResourceFactory;
import net.minecraft.resource.ResourceManager;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.InvalidHierarchicalFileException;
import suso.event_base.client.shader.ShaderNetworking;
import suso.event_base.custom.blocks.CustomBlocks;
import suso.event_base.custom.render.hud.CustomHud;
import suso.event_base.mixin.client.KeyboardInvoker;

import java.io.IOException;

@Environment(EnvType.CLIENT)
public class CustomRender {
    public static final CustomHud CUSTOM_HUD = new CustomHud();

    private static ShaderProgram TIMER_SHADER;
    private static ShaderProgram AGILITY_SHADER;
    private static ShaderProgram SCORE_SHADER;
    private static ShaderProgram KILL_SHADER;
    private static ShaderProgram KILL_BORDER_SHADER;
    private static ShaderProgram DEATH_SHADER;

    private static ShaderProgram currentDrawShader;

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

    public static void setupShaders(ResourceFactory factory) {
        System.out.println("Loading custom shaders...");
        try {
            TIMER_SHADER = new ShaderProgram(factory, "suso_timer", VertexFormats.POSITION_TEXTURE);
            AGILITY_SHADER = new ShaderProgram(factory, "suso_agility", VertexFormats.POSITION_COLOR);
            SCORE_SHADER = new ShaderProgram(factory, "suso_score", VertexFormats.POSITION_TEXTURE);
            KILL_SHADER = new ShaderProgram(factory, "suso_kill", VertexFormats.POSITION_COLOR);
            KILL_BORDER_SHADER = new ShaderProgram(factory, "suso_kill_border", VertexFormats.POSITION_COLOR);
            DEATH_SHADER = new ShaderProgram(factory, "suso_death", VertexFormats.POSITION_COLOR);
        } catch (IOException e) {
            printShaderException(e);
        }
    }

    public static void registerHud() {
        HudRenderCallback.EVENT.register(CUSTOM_HUD);
    }

    public static ShaderProgram getCurrentDrawShader() {
        return currentDrawShader;
    }

    private static void setCurrentDrawShader(ShaderProgram shader) {
        currentDrawShader = shader;
    }

    public static ShaderProgram getTimerShader() {
        return TIMER_SHADER;
    }

    public static ShaderProgram getAgilityShader() {
        return AGILITY_SHADER;
    }

    public static ShaderProgram getScoreShader() {
        return SCORE_SHADER;
    }

    public static ShaderProgram getKillShader() {
        return KILL_SHADER;
    }

    public static ShaderProgram getKillBorderShader() {
        return KILL_BORDER_SHADER;
    }

    public static ShaderProgram getDeathShader() {
        return DEATH_SHADER;
    }

    // Print a shader exception in chat.
    private static void printShaderException(Exception exception) {
        MinecraftClient client = MinecraftClient.getInstance();
        Throwable throwable = exception;
        while (!(throwable instanceof InvalidHierarchicalFileException)) {
            Throwable cause = throwable.getCause();
            if (cause != null) throwable = cause;
            else {
                String translationKey = "debug.reload_shaders.unknown_error";
                ((KeyboardInvoker) client.keyboard).invokeDebugError(translationKey);
                throwable.printStackTrace();
                return;
            }
        }
        String translationKey = "debug.reload_shaders.error";
        ((KeyboardInvoker) client.keyboard).invokeDebugError(translationKey);
        client.inGameHud.getChatHud().addMessage(Text.literal(throwable.getMessage()).formatted(Formatting.GRAY));
    }

    public static void withShader(ShaderProgram shader, Runnable lambda) {
        setCurrentDrawShader(shader);
        lambda.run();
        setCurrentDrawShader(null);
    }
}
