package suso.event_base.custom.render.hud;

import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.network.PacketByteBuf;

public interface StateHud extends HudRenderCallback {
    void onHudMessage(PacketByteBuf msg);
}
