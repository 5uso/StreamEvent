package suso.event_base.custom.render.hud;

import io.netty.buffer.ByteBuf;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;

public interface StateHud extends HudRenderCallback {
    void onHudMessage(CustomHud.DataTypes type, ByteBuf msg);
}
