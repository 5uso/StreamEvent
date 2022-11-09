package suso.event_base.custom.render.hud;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.util.math.MatrixStack;

public class IdleHud implements StateHud {
    @Override
    public void onHudRender(MatrixStack matrixStack, float tickDelta) {
    }

    @Override
    public void onHudMessage(CustomHud.DataTypes type, ByteBuf msg) {

    }
}
