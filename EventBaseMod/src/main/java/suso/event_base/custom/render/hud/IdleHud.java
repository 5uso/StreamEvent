package suso.event_base.custom.render.hud;

import net.minecraft.client.util.math.MatrixStack;
import suso.event_base.custom.network.payloads.HudDataPayload;

public class IdleHud implements StateHud {
    @Override
    public void onHudRender(MatrixStack matrixStack, float tickDelta) {
    }

    @Override
    public void onHudMessage(HudDataPayload p) {

    }
}
