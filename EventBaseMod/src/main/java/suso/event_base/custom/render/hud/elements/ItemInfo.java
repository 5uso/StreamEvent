package suso.event_base.custom.render.hud.elements;

import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.util.Identifier;
import suso.event_base.util.MiscUtil;

public class ItemInfo implements HudRenderCallback {

    private boolean active = false;
    private double progress = 0.0;

    private Identifier current = Identifier.ofVanilla("textures/empty.png");
    private Identifier buffered = Identifier.ofVanilla("textures/empty.png");

    public void hide() {
        active = false;
    }

    public void show(Identifier texture) {
        buffered = texture;
        active = true;
    }

    @Override
    public void onHudRender(DrawContext ctx, RenderTickCounter tickCounter) {
        MinecraftClient client = MinecraftClient.getInstance();
        int width = client.getWindow().getScaledWidth();
        int height = client.getWindow().getScaledHeight();
        float lastFrame = tickCounter.getLastFrameDuration() * 50.0f;

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

        ctx.fill(startx, starty, startx + w, starty + h, 0x7F000000);

        ctx.drawTexture(current, startx, starty, 0, 0, w, h, w, h);
    }
}
