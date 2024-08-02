package suso.event_base.custom.render.hud;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.ShaderProgram;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.MathHelper;
import suso.event_base.custom.network.payloads.HudDataPayload;
import suso.event_base.custom.render.CustomRender;
import suso.event_base.custom.render.hud.elements.*;

public class PrimaticaIngameHud implements StateHud {
    private final Timer timer;
    private final Feed feed;
    private boolean agility;
    private float agilityProgress;
    private final PrimaticaScoreboard scoreboard;
    private final ItemInfo info;
    private final KillMessage killMessage;

    public PrimaticaIngameHud() {
        timer = new Timer();
        feed = new Feed();
        agility = false;
        agilityProgress = 0.0f;
        scoreboard = new PrimaticaScoreboard();
        info = new ItemInfo();
        killMessage = new KillMessage();
    }

    @Override
    public void onHudMessage(HudDataPayload p) {
        switch(p.type) {
            case TIMER -> timer.msEnd = p.buf.readLong();
            case FEED -> feed.addMessage(p.buf);
            case AGILITY -> agility = p.buf.readBoolean();
            case PRIMATICA_SCORE -> {
                int[] scores = new int[12];
                for(int i = 0; i < 12; i++) scores[i] = p.buf.readInt();

                int[] ranks = new int[12];
                for(int i = 0; i < 12; i++) ranks[i] = p.buf.readInt();

                scoreboard.setScores(scores, ranks);
            }
            case INFO -> {
                boolean info_active = p.buf.readBoolean();
                if(info_active) {
                    PacketByteBuf buf = PacketByteBufs.copy(p.buf);
                    info.show(buf.readIdentifier());
                }
                else info.hide();
            }
            case KILL -> {
                PacketByteBuf buf = PacketByteBufs.copy(p.buf);
                killMessage.display(buf.readString(), Formatting.values()[buf.readInt()]);
            }
        }
    }

    @Override
    public void onHudRender(DrawContext ctx, RenderTickCounter tickCounter) {
        MinecraftClient client = MinecraftClient.getInstance();
        int width = client.getWindow().getScaledWidth();
        int height = client.getWindow().getScaledHeight();
        float lastFrame = tickCounter.getLastFrameDuration() * 50.0f;

        agilityProgress = MathHelper.clamp(agilityProgress + lastFrame / 1000.0f * (agility ? 1.0f : -1.0f), 0.0f, 1.0f);
        if(agilityProgress > 0.0f) {
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();

            ShaderProgram agilityShader = CustomRender.getAgilityShader();
            agilityShader.getUniformOrDefault("Progress").set(agilityProgress);
            CustomRender.setCurrentDrawShader(agilityShader);

            ctx.fill(0, 0, width, height, 0);

            RenderSystem.disableBlend();
            CustomRender.setCurrentDrawShader(null);
        }

        ctx.getMatrices().push();
        ctx.getMatrices().translate(width - height * (0.04 + 0.0426 * 738.0 / 155.0), height * 0.02, 0.0);
        ctx.getMatrices().scale(0.0426f, 0.0426f, 1.0f);
        timer.onHudRender(ctx, tickCounter);
        ctx.getMatrices().pop();

        ctx.getMatrices().push();
        ctx.getMatrices().translate(width - height * 0.04 + 5.0 / 1080.0 * height, 101.0 / 1080.0 * height, 0.0);
        feed.onHudRender(ctx, tickCounter);
        ctx.getMatrices().pop();

        scoreboard.onHudRender(ctx, tickCounter);

        info.onHudRender(ctx, tickCounter);

        killMessage.onHudRender(ctx, tickCounter);
    }
}
