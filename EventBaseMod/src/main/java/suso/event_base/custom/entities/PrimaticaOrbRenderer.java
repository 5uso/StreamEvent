package suso.event_base.custom.entities;

import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.renderer.GeoEntityRenderer;
import software.bernie.geckolib.util.Color;

public class PrimaticaOrbRenderer extends GeoEntityRenderer<PrimaticaOrbEntity> {
    public PrimaticaOrbRenderer(EntityRendererFactory.Context renderManager) {
        super(renderManager, new PrimaticaOrbModel());
    }

    private static final int TRANSITION_DURATION = 300;

    @Override
    public RenderLayer getRenderType(PrimaticaOrbEntity animatable, Identifier texture, @Nullable VertexConsumerProvider bufferSource, float partialTick) {
        return RenderLayer.getEntityTranslucent(getTextureLocation(animatable));
    }

    @Override
    public Color getRenderColor(PrimaticaOrbEntity animatable, float partialTick, int packedLight) {
        Color current = Color.ofOpaque(animatable.getTeamColorValue());
        if(!animatable.transitioningColor && !animatable.previousColor.equals(current)) {
            animatable.transitionStartMs = Util.getMeasuringTimeMs();
            animatable.transitioningColor = true;
        }

        if(animatable.transitioningColor) {
            double progress = (float)(Util.getMeasuringTimeMs() - animatable.transitionStartMs) / TRANSITION_DURATION;
            if(progress >= 1.0) {
                animatable.transitioningColor = false;
                animatable.previousColor = current;
                return current;
            }

            double prev = 1.0 - progress;
            current = Color.ofRGB((int)(current.getRed() * progress + animatable.previousColor.getRed() * prev),
                                  (int)(current.getGreen() * progress + animatable.previousColor.getGreen() * prev),
                                  (int)(current.getBlue() * progress + animatable.previousColor.getBlue() * prev));
        }

        return current;
    }
}
