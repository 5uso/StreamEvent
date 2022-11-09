package suso.event_base.custom.render.hud;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.math.MatrixStack;
import suso.event_base.custom.render.hud.elements.Timer;

public class PrimaticaIngameHud implements StateHud {
    private final Timer timer;

    public PrimaticaIngameHud() {
        timer = new Timer();
    }

    @Override
    public void onHudMessage(CustomHud.DataTypes type, ByteBuf msg) {
        switch(type) {
            case TIMER -> timer.msEnd = msg.readLong();
            case FEED -> { /*TODO*/ }
        }
    }

    @Override
    public void onHudRender(MatrixStack matrixStack, float tickDelta) {
        MinecraftClient client = MinecraftClient.getInstance();
        int width = client.getWindow().getScaledWidth();
        int height = client.getWindow().getScaledHeight();

        matrixStack.push();
        matrixStack.translate(width - height * (0.05 + 0.0426 * 738.0 / 155.0), height * 0.05, 0.0);
        matrixStack.scale(0.0426f, 0.0426f, 1.0f);
        timer.onHudRender(matrixStack, tickDelta);
        matrixStack.pop();
    }
}
