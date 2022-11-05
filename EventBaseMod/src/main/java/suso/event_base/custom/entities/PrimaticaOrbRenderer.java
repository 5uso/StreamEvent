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

    @Override
    public RenderLayer getRenderType(PrimaticaOrbEntity animatable, float partialTick, MatrixStack poseStack, @Nullable VertexConsumerProvider bufferSource, @Nullable VertexConsumer buffer, int packedLight, Identifier texture) {
        return RenderLayer.getEntityTranslucent(getTextureLocation(animatable));
    }
}
