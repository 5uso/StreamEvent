package suso.event_base.custom.render.hud.elements;

import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.PlayerSkinDrawer;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import suso.event_base.EvtBaseConstants;
import suso.event_base.util.MiscUtil;

import java.util.UUID;

public class FeedMessage implements HudRenderCallback {
    private final boolean singlePlayer;
    private final Identifier face1;
    private final Identifier face2;
    private final Identifier action;

    private float timer = 0.0f;

    public FeedMessage(UUID player1, Identifier actionTexture, UUID player2) {
        singlePlayer = player2.equals(EvtBaseConstants.NULL_UUID);

        Identifier tface1, tface2;
        tface1 = tface2 = Identifier.of("suso", "textures/unknown_player.png");

        MinecraftClient client = MinecraftClient.getInstance();
        if(client.world != null) {
            PlayerEntity p1 = client.world.getPlayerByUuid(player1);
            if(p1 != null) tface1 = client.getSkinProvider().getSkinTextures(p1.getGameProfile()).texture();

            PlayerEntity p2 = client.world.getPlayerByUuid(player2);
            if(p2 != null) tface2 = client.getSkinProvider().getSkinTextures(p2.getGameProfile()).texture();
        }

        face1 = tface1;
        face2 = tface2;
        action = actionTexture;
    }

    @Override
    public void onHudRender(DrawContext ctx, RenderTickCounter tickCounter) {
        MinecraftClient client = MinecraftClient.getInstance();
        int height = client.getWindow().getScaledHeight();
        double hr = height / 1080.0;

        float lastFrame = tickCounter.getLastFrameDuration() * 50.0f;
        timer += lastFrame / 1000.0f;

        ctx.getMatrices().push();
        double x_offset = 0.0;
        if(timer < 0.2) x_offset = MiscUtil.smoothStep(1.0, 0.0, timer / 0.2);
        if(timer > 5.8) x_offset = MiscUtil.smoothStep(0.0, 1.0, (timer - 5.8) / 0.2);
        ctx.getMatrices().translate(x_offset * 164.0 * hr, 0.0, 0.0);

        ctx.fill((int) ((singlePlayer ? -87.0 : -125.0) * hr), 0, 0, (int) (48.0 * hr), 0x7F000000);
        if(!singlePlayer) {
            PlayerSkinDrawer.draw(ctx, face1, (int) (-119.0 * hr), (int) (8.0 * hr), (int) (32.0 * hr));
        }
        PlayerSkinDrawer.draw(ctx, singlePlayer ? face1 : face2, (int) (-38.0 * hr), (int) (8.0 * hr), (int) (32.0 * hr));
        ctx.drawTexture(action, (int) (-81.0 * hr), (int) (6.0 * hr), 0.0f, 0.0f, (int) (37.0 * hr), (int) (37.0 * hr), (int) (37.0 * hr), (int) (37.0 * hr));

        ctx.getMatrices().pop();
    }

    public float getTimer() {
        return timer;
    }
}
