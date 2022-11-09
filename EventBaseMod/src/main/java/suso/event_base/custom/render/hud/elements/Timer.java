package suso.event_base.custom.render.hud.elements;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.render.Shader;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import suso.event_base.custom.render.CustomRender;

public class Timer implements HudRenderCallback {
    private static final Identifier timerTexture = new Identifier("suso:textures/hud/timer.png");

    public int msToDisplay = 0;

    @Override
    public void onHudRender(MatrixStack matrixStack, float tickDelta) {
        MinecraftClient client = MinecraftClient.getInstance();

        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();

        Shader timerShader = CustomRender.getTimerShader();
        timerShader.getUniformOrDefault("Timer").set(msToDisplay);
        CustomRender.setCurrentDrawShader(timerShader);

        RenderSystem.setShaderTexture(0, timerTexture);

        int height = client.getWindow().getScaledHeight();
        int width = height * 738 / 155;
        DrawableHelper.drawTexture(matrixStack, 0, 0, 0.0f, 0.0f, width, height, width, height);

        RenderSystem.disableBlend();
        CustomRender.setCurrentDrawShader(null);
    }
}
