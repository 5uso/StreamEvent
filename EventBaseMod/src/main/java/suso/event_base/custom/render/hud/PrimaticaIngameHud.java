package suso.event_base.custom.render.hud;

import com.mojang.blaze3d.systems.RenderSystem;
import io.netty.buffer.ByteBuf;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.render.Shader;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.math.MathHelper;
import suso.event_base.custom.render.CustomRender;
import suso.event_base.custom.render.hud.elements.Feed;
import suso.event_base.custom.render.hud.elements.ItemInfo;
import suso.event_base.custom.render.hud.elements.PrimaticaScoreboard;
import suso.event_base.custom.render.hud.elements.Timer;

public class PrimaticaIngameHud implements StateHud {
    private final Timer timer;
    private final Feed feed;
    private boolean agility;
    private float agilityProgress;
    private final PrimaticaScoreboard scoreboard;
    private final ItemInfo info;

    public PrimaticaIngameHud() {
        timer = new Timer();
        feed = new Feed();
        agility = false;
        agilityProgress = 0.0f;
        scoreboard = new PrimaticaScoreboard();
        info = new ItemInfo();
    }

    @Override
    public void onHudMessage(CustomHud.DataTypes type, ByteBuf msg) {
        switch(type) {
            case TIMER -> timer.msEnd = msg.readLong();
            case FEED -> feed.addMessage(msg);
            case AGILITY -> agility = msg.readBoolean();
            case PRIMATICA_SCORE -> {
                int[] scores = new int[12];
                for(int i = 0; i < 12; i++) scores[i] = msg.readInt();

                int[] ranks = new int[12];
                for(int i = 0; i < 12; i++) ranks[i] = msg.readInt();

                scoreboard.setScores(scores, ranks);
            }
            case INFO -> {
                boolean info_active = msg.readBoolean();
                if(info_active) {
                    PacketByteBuf buf = PacketByteBufs.copy(msg);
                    info.show(buf.readIdentifier());
                }
                else info.hide();
            }
        }
    }

    @Override
    public void onHudRender(MatrixStack matrixStack, float tickDelta) {
        MinecraftClient client = MinecraftClient.getInstance();
        int width = client.getWindow().getScaledWidth();
        int height = client.getWindow().getScaledHeight();
        float lastFrame = client.getLastFrameDuration() * 50.0f;

        agilityProgress = MathHelper.clamp(agilityProgress + lastFrame / 1000.0f * (agility ? 1.0f : -1.0f), 0.0f, 1.0f);
        if(agilityProgress > 0.0f) {
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();

            Shader agilityShader = CustomRender.getAgilityShader();
            agilityShader.getUniformOrDefault("Progress").set(agilityProgress);
            CustomRender.setCurrentDrawShader(agilityShader);

            DrawableHelper.fill(matrixStack, 0, 0, width, height, 0);

            RenderSystem.disableBlend();
            CustomRender.setCurrentDrawShader(null);
        }

        matrixStack.push();
        matrixStack.translate(width - height * (0.04 + 0.0426 * 738.0 / 155.0), height * 0.02, 0.0);
        matrixStack.scale(0.0426f, 0.0426f, 1.0f);
        timer.onHudRender(matrixStack, tickDelta);
        matrixStack.pop();

        matrixStack.push();
        matrixStack.translate(width - height * 0.04 + 5.0 / 1080.0 * height, 101.0 / 1080.0 * height, 0.0);
        feed.onHudRender(matrixStack, tickDelta);
        matrixStack.pop();

        scoreboard.onHudRender(matrixStack, tickDelta);

        info.onHudRender(matrixStack, tickDelta);
    }
}
