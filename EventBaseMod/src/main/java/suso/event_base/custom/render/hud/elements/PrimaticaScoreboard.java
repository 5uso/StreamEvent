package suso.event_base.custom.render.hud.elements;

import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderTickCounter;
import suso.event_base.EvtBaseConstants;

public class PrimaticaScoreboard implements HudRenderCallback {
    private final PrimaticaScore[] scores;

    public PrimaticaScoreboard() {
        scores = new PrimaticaScore[12];
        for(int i = 0; i < 12; i++) scores[i] = new PrimaticaScore(EvtBaseConstants.formattingCorrespondence.get(i), i);
    }

    @Override
    public void onHudRender(DrawContext ctx, RenderTickCounter tickCounter) {
        for(int i = 0; i < 12; i++) scores[i].onHudRender(ctx, tickCounter);
    }

    public void setScores(int[] scores, int[] ranks) {
        for(int i = 0; i < 12; i++) this.scores[i].setScore(scores[i], ranks[i]);
    }
}
