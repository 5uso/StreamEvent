package suso.event_base.custom.render.hud.elements;

import io.netty.buffer.ByteBuf;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;
import suso.event_base.util.MiscUtil;

import java.util.LinkedList;
import java.util.UUID;

public class Feed implements HudRenderCallback {
    private final LinkedList<FeedMessage> messages = new LinkedList<>();

    @Override
    public void onHudRender(MatrixStack matrixStack, float tickDelta) {
        MinecraftClient client = MinecraftClient.getInstance();
        int height = client.getWindow().getScaledHeight();

        matrixStack.push();
        messages.removeIf(m -> {
            m.onHudRender(matrixStack, tickDelta);
            double y_offset = 54.0;
            double t = m.getTimer();
            if(t < 0.2) y_offset *= MiscUtil.smoothStep(0.0, 1.0, t / 0.2);
            if(t > 5.8) y_offset *= MiscUtil.smoothStep(1.0, 0.0, (t - 5.8) / 0.2);

            matrixStack.translate(0.0, y_offset / 1080.0 * height, 0.0);

            return t > 6.0;
        });
        matrixStack.pop();
    }

    public void addMessage(ByteBuf msg) {
        PacketByteBuf buf = PacketByteBufs.copy(msg);

        UUID player1 = buf.readUuid();
        Identifier texture = buf.readIdentifier();
        UUID player2 = buf.readUuid();

        messages.addFirst(new FeedMessage(player1, texture, player2));
    }
}
