package suso.event_base.custom.entities;

import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.core.util.Color;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

public class PrimaticaOrbRenderer extends GeoEntityRenderer<PrimaticaOrbEntity> {
    public PrimaticaOrbRenderer(EntityRendererFactory.Context renderManager) {
        super(renderManager, new PrimaticaOrbModel());
    }

    private static final int TRANSITION_DURATION = 300;
    private Color previousColor = Color.WHITE;
    private boolean transitioningColor = false;
    private long transitionStartMs = 0;

    @Override
    public RenderLayer getRenderType(PrimaticaOrbEntity animatable, float partialTick, MatrixStack poseStack, @Nullable VertexConsumerProvider bufferSource, @Nullable VertexConsumer buffer, int packedLight, Identifier texture) {
        return RenderLayer.getEntityTranslucent(getTextureLocation(animatable));
    }

    @Override
    public Color getRenderColor(PrimaticaOrbEntity animatable, float partialTick, MatrixStack poseStack, @Nullable VertexConsumerProvider bufferSource, @Nullable VertexConsumer buffer, int packedLight) {
        Color current = Color.ofOpaque(animatable.getTeamColorValue());
        if(!transitioningColor && !previousColor.equals(current)) {
            transitionStartMs = Util.getMeasuringTimeMs();
            transitioningColor = true;
        }

        if(transitioningColor) {
            double progress = (float)(Util.getMeasuringTimeMs() - transitionStartMs) / TRANSITION_DURATION;
            if(progress >= 1.0) {
                transitioningColor = false;
                previousColor = current;
                return current;
            }

            double prev = 1.0 - progress;
            current = Color.ofRGB((int)(current.getRed() * progress + previousColor.getRed() * prev),
                                  (int)(current.getGreen() * progress + previousColor.getGreen() * prev),
                                  (int)(current.getBlue() * progress + previousColor.getBlue() * prev));
        }

        return current;
    }
}
