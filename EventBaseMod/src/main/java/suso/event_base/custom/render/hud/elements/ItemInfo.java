package suso.event_base.custom.render.hud.elements;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import suso.event_base.util.MiscUtil;

public class ItemInfo implements HudRenderCallback {

    private boolean active = false;
    private double progress = 0.0;

    private Identifier current = new Identifier("minecraft:textures/empty.png");
    private Identifier buffered = new Identifier("minecraft:textures/empty.png");

    public void hide() {
        active = false;
    }

    public void show(Identifier texture) {
        buffered = texture;
        active = true;
    }

    @Override
    public void onHudRender(MatrixStack matrixStack, float tickDelta) {
        MinecraftClient client = MinecraftClient.getInstance();
        int width = client.getWindow().getScaledWidth();
        int height = client.getWindow().getScaledHeight();
        float lastFrame = client.getLastFrameDuration() * 50.0f;

        if(progress <= 0.0) current = buffered;

        if(!active && progress > 0.0) {
            progress -= 0.01 * lastFrame;
        } else if(active && !current.equals(buffered)) {
            progress -= 0.03 * lastFrame;
        } else if(active && progress < 1.0) {
            progress += 0.01 * lastFrame;
        }

        float ratio = width / 1920.0f;
        int startx = (int)(28 * ratio);
        int starty = height - (int)(MiscUtil.smoothStep(0.0, 1.0, progress) * 140 * ratio);
        int w = (int)(540 * ratio);
        int h = (int)(140 * ratio);

        DrawableHelper.fill(matrixStack, startx, starty, startx + w, starty + h, 0x7F000000);

        RenderSystem.setShaderTexture(0, current);
        DrawableHelper.drawTexture(matrixStack, startx, starty, 0, 0, w, h, w, h);
    }
}
