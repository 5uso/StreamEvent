package suso.event_base.custom.render.hud;

import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import suso.event_common.custom.network.payloads.HudDataPayload;

public interface StateHud extends HudRenderCallback {
    void onHudMessage(HudDataPayload p);
}
