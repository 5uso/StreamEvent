package suso.event_base.custom.render.hud.elements;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.ShaderProgram;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.util.Identifier;
import suso.event_base.client.ModCheck;
import suso.event_base.custom.render.CustomRender;

public class Timer implements HudRenderCallback {
    private static final Identifier timerTexture = Identifier.of("suso", "textures/hud/timer.png");

    public long msEnd = 0;

    @Override
    public void onHudRender(DrawContext ctx, RenderTickCounter tickCounter) {
        MinecraftClient client = MinecraftClient.getInstance();
        int height = client.getWindow().getScaledHeight();
        int width = height * 738 / 155;

        ctx.fill(-height/10, -height/10, width + height/10 + 2, height + height/5, 0x7F000000);

        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();

        ShaderProgram timerShader = CustomRender.getTimerShader();
        int displayedMs = (int)(Math.max(0, msEnd - ModCheck.getTime()));
        timerShader.getUniformOrDefault("Timer").set(displayedMs);
        CustomRender.setCurrentDrawShader(timerShader);

        ctx.drawTexture(timerTexture, 0, 0, 0.0f, 0.0f, width, height, width, height);

        RenderSystem.disableBlend();
        CustomRender.setCurrentDrawShader(null);
    }
}
