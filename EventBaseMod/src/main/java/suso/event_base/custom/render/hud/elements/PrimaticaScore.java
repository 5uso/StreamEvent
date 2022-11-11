package suso.event_base.custom.render.hud.elements;

import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Formatting;

public class PrimaticaScore implements HudRenderCallback {
    private final Formatting teamColor;
    private int score;
    private int rank;

    private float x;
    private float y;
    private float targetx;

    public PrimaticaScore(Formatting teamColor, int rank) {
        this.teamColor = teamColor;
        this.rank = rank;
        this.score = 0;

        this.x = rank / 11.0f;
        this.y = 0.05f;
        this.targetx = this.x;
    }

    @Override
    public void onHudRender(MatrixStack matrixStack, float tickDelta) {
        if(teamColor.getColorValue() == null) return;

        MinecraftClient client = MinecraftClient.getInstance();
        int width = client.getWindow().getScaledWidth();
        int height = client.getWindow().getScaledHeight();
        float lastFrame = client.getLastFrameDuration() * 50.0f;

        matrixStack.push();

        x += (targetx - x) * 0.01f * lastFrame;

        float targety = 0.05f + targetx < x ? (x - targetx) * 0.1f : 0.0f;
        y += (targety - y) * 0.01f * lastFrame;

        matrixStack.translate(width / 2.0 - height / 10.0 + x * height / 5.0, y * height, 0.0);
        MinecraftClient.getInstance().textRenderer.draw(matrixStack, String.valueOf(score), 0, 0, teamColor.getColorValue());
        matrixStack.pop();
    }

    public void setScore(int score, int rank) {
        if(rank != this.rank) {
            this.rank = rank;
            targetx = rank / 11.0f;
        }

        if(score != this.score) {
            this.score = score;
        }
    }
}
