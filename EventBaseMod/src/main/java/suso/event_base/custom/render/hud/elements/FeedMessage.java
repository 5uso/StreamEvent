package suso.event_base.custom.render.hud.elements;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.PlayerSkinDrawer;
import net.minecraft.client.util.math.MatrixStack;
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
        tface1 = tface2 = new Identifier("suso:textures/unknown_player.png");

        MinecraftClient client = MinecraftClient.getInstance();
        if(client.world != null) {
            PlayerEntity p1 = client.world.getPlayerByUuid(player1);
            if(p1 != null) tface1 = client.getSkinProvider().loadSkin(p1.getGameProfile());

            PlayerEntity p2 = client.world.getPlayerByUuid(player2);
            if(p2 != null) tface2 = client.getSkinProvider().loadSkin(p2.getGameProfile());
        }

        face1 = tface1;
        face2 = tface2;
        action = actionTexture;
    }

    @Override
    public void onHudRender(MatrixStack matrixStack, float tickDelta) {
        MinecraftClient client = MinecraftClient.getInstance();
        int height = client.getWindow().getScaledHeight();
        double hr = height / 1080.0;

        float lastFrame = client.getLastFrameDuration() * 50.0f;
        timer += lastFrame / 1000.0f;

        matrixStack.push();
        double x_offset = 0.0;
        if(timer < 0.2) x_offset = MiscUtil.smoothStep(1.0, 0.0, timer / 0.2);
        if(timer > 5.8) x_offset = MiscUtil.smoothStep(0.0, 1.0, (timer - 5.8) / 0.2);
        matrixStack.translate(x_offset * 164.0 * hr, 0.0, 0.0);

        DrawableHelper.fill(matrixStack, (int) ((singlePlayer ? -87.0 : -125.0) * hr), 0, 0, (int) (48.0 * hr), 0x7F000000);
        if(!singlePlayer) {
            RenderSystem.setShaderTexture(0, face1);
            PlayerSkinDrawer.draw(matrixStack, (int) (-119.0 * hr), (int) (8.0 * hr), (int) (32.0 * hr));
        }
        RenderSystem.setShaderTexture(0, singlePlayer ? face1 : face2);
        PlayerSkinDrawer.draw(matrixStack, (int) (-38.0 * hr), (int) (8.0 * hr), (int) (32.0 * hr));
        RenderSystem.setShaderTexture(0, action);
        DrawableHelper.drawTexture(matrixStack, (int) (-81.0 * hr), (int) (6.0 * hr), 0.0f, 0.0f, (int) (37.0 * hr), (int) (37.0 * hr), (int) (37.0 * hr), (int) (37.0 * hr));

        matrixStack.pop();
    }

    public float getTimer() {
        return timer;
    }
}
