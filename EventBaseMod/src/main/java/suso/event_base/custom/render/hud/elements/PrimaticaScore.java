package suso.event_base.custom.render.hud.elements;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.ShaderProgram;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.PlayerSkinDrawer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.scoreboard.Team;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import suso.event_base.EvtBaseConstants;
import suso.event_base.client.ModCheck;
import suso.event_base.custom.render.CustomRender;

import java.util.ArrayList;
import java.util.List;

public class PrimaticaScore implements HudRenderCallback {
    private final Formatting teamColor;
    private int score;
    private int rank;

    private float x;
    private float y;
    private float targetx;

    private final List<Identifier> skins;
    private long lastUpdate;

    private static final Identifier scoreTexture = new Identifier("suso:textures/hud/score.png");

    public PrimaticaScore(Formatting teamColor, int rank) {
        this.teamColor = teamColor;
        this.rank = rank;
        this.score = 0;

        this.x = rank / 11.0f;
        this.y = -1.0f;
        this.targetx = this.x;

        this.skins = new ArrayList<>(3);
        this.updatePlayers();
    }

    @Override
    public void onHudRender(MatrixStack matrixStack, float tickDelta) {
        if(teamColor.getColorValue() == null) return;

        if(ModCheck.getTime() - lastUpdate > 10000) updatePlayers();

        MinecraftClient client = MinecraftClient.getInstance();
        int width = client.getWindow().getScaledWidth();
        int height = client.getWindow().getScaledHeight();
        float lastFrame = client.getLastFrameDuration() * 50.0f;

        matrixStack.push();

        x += (targetx - x) * 0.01f * lastFrame;

        float targety = 0.05f + targetx < x ? (x - targetx) * 0.1f : 0.0f;
        y += (targety - y) * 0.01f * lastFrame;

        matrixStack.translate(width / 2.0 - 451.0 / 1080.0 * height + x * 451.0 * 2.0 / 1080.0 * height, y * height, 0.0);
        if(client.player != null && client.player.getTeamColorValue() == teamColor.getColorValue()) {
            matrixStack.scale(1.5f, 1.5f, 1.0f);
            matrixStack.translate(0.0, -12.0 / 1080.0 * height, 0.0);
        }
        matrixStack.scale(0.1f, 0.1f, 1.0f);
        height *= 10;

        DrawContext.fill(matrixStack, (int) (-33.0 / 1080.0 * height), (int) (16.0 / 1080.0 * height), (int) (33.0 / 1080.0 * height), (int) (76.0 / 1080.0 * height), 0x7F000000);

        for(int i = 0; i <3; i++) {
            RenderSystem.setShaderTexture(0, skins.get(i));
            PlayerSkinDrawer.draw(matrixStack, (int) Math.round((-33 + 6 + 19 * i) * height / 1080.0), (int) (22.0 / 1080.0 * height), (int) (16.0 / 1080.0 * height));
        }

        ShaderProgram scoreShader = CustomRender.getScoreShader();
        scoreShader.getUniformOrDefault("Score").set(score);
        CustomRender.setCurrentDrawShader(scoreShader);
        RenderSystem.setShaderTexture(0, scoreTexture);
        RenderSystem.setShaderColor((teamColor.getColorValue() >> 16 & 0xFF) / 255.0f, (teamColor.getColorValue() >> 8 & 0xFF) / 255.0f, (teamColor.getColorValue() & 0xFF) / 255.0f, 1.0f);
        DrawContext.drawTexture(matrixStack, (int) ((-33.0 + 14.0) / 1080.0 * height), (int) (41.0 / 1080.0 * height), 0.0f, 0.0f, (int) (38.0 / 1080.0 * height), (int) (32.0 / 1080.0 * height), (int) (38.0 / 1080.0 * height), (int) (32.0 / 1080.0 * height));
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
        CustomRender.setCurrentDrawShader(null);
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

    public void updatePlayers() {
        skins.clear();

        MinecraftClient client = MinecraftClient.getInstance();
        World w = client.world;
        if(w != null) {
            Team t = w.getScoreboard().getTeam(EvtBaseConstants.getTeamColor(teamColor.getColorIndex()));
            w.getPlayers().stream().filter(p -> p.isTeamPlayer(t)).limit(3).forEach(p -> skins.add(client.getSkinProvider().loadSkin(p.getGameProfile())));
        }

        for(int i = skins.size(); i <3; i++) skins.add(new Identifier("suso:textures/unknown_player.png"));

        lastUpdate = ModCheck.getTime();
    }
}
