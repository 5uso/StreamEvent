package suso.event_base.custom.render.hud;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderTickCounter;
import suso.event_common.custom.network.payloads.HudDataPayload;

public class IdleHud implements StateHud {
    @Override
    public void onHudRender(DrawContext ctx, RenderTickCounter tickCounter) {
    }

    @Override
    public void onHudMessage(HudDataPayload p) {

    }
}
