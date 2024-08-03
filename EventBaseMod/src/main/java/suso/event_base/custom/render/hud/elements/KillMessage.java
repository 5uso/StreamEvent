package suso.event_base.custom.render.hud.elements;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.ShaderProgram;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.MathHelper;
import suso.event_base.custom.render.CustomRender;
import suso.event_base.util.MiscUtil;

public class KillMessage implements HudRenderCallback {

    private String displayedName = "";
    private int displayedColor = 0xFFFFFF;
    private double progress = 1.0;

    public void display(String name, Formatting color) {
        displayedName = name;
        displayedColor = color.getColorValue() == null ? 0xFFFFFF : color.getColorValue();
        progress = 0.0;
    }

    @Override
    public void onHudRender(DrawContext ctx, RenderTickCounter tickCounter) {
        MinecraftClient client = MinecraftClient.getInstance();
        int width = client.getWindow().getScaledWidth();
        int height = client.getWindow().getScaledHeight();
        float lastFrame = tickCounter.getLastFrameDuration() * 50.0f;

        if(progress < 1.0) {
            progress += 0.001 * lastFrame;

            double in_progress = MiscUtil.smoothStep(0.0, 1.0, MathHelper.clamp(progress * 5.0, 0.0, 1.0));
            double out_progress = MiscUtil.smoothStep(0.0, 1.0, MathHelper.clamp((progress - 0.77) * 3.0, 0.0, 1.0));

            float a_scale = Math.max(2.0f - (float) in_progress, 1.0f + (float) out_progress * 0.6f);
            int alpha = (int) (0xFF * Math.min(in_progress, 1.0 - out_progress));

            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();

            RenderSystem.setShaderColor((displayedColor >> 16 & 0xFF) / 255.0f, (displayedColor >> 8 & 0xFF) / 255.0f, (displayedColor & 0xFF) / 255.0f, 1.0f);

            ShaderProgram borderShader = CustomRender.getKillBorderShader();
            borderShader.getUniformOrDefault("Progress").set((float) progress);
            CustomRender.withShader(borderShader, () -> ctx.fill(0, 0, width, height, 0));

            ctx.getMatrices().push();

            ctx.getMatrices().translate(width / 2.0, height * 5.0 / 9.0, 0.0);
            float s = a_scale * 3.0f * height / 1080.0f;
            ctx.getMatrices().scale(s, s, 1.0f);
            ctx.drawCenteredTextWithShadow(client.textRenderer, displayedName, 0, 0, (alpha << 24) | displayedColor);

            RenderSystem.setShaderColor((displayedColor >> 16 & 0xFF) / 255.0f, (displayedColor >> 8 & 0xFF) / 255.0f, (displayedColor & 0xFF) / 255.0f, 1.0f);

            ShaderProgram killShader = CustomRender.getKillShader();
            killShader.getUniformOrDefault("Progress").set((float) progress);
            CustomRender.withShader(killShader, () -> {
                int text_width = client.textRenderer.getWidth(displayedName);
                int border_scale = 6;
                ctx.fill(-(text_width / 2 + text_width * border_scale / 2), -5 * border_scale, text_width / 2 + text_width * border_scale / 2, 10 + 5 * border_scale, 0);
            });

            ctx.getMatrices().pop();
            RenderSystem.disableBlend();
        } else displayedName = "";
    }
}
