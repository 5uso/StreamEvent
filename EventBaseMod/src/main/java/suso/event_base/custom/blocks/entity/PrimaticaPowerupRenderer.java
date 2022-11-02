package suso.event_base.custom.blocks.entity;

import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.renderers.geo.GeoBlockRenderer;

public class PrimaticaPowerupRenderer extends GeoBlockRenderer<PrimaticaPowerupBlockEntity> {
    public PrimaticaPowerupRenderer() {
        super(new PrimaticaPowerupModel());
    }

    @Override
    public RenderLayer getRenderType(PrimaticaPowerupBlockEntity animatable, float partialTick, MatrixStack poseStack, @Nullable VertexConsumerProvider bufferSource, @Nullable VertexConsumer buffer, int packedLight, Identifier texture) {
        return RenderLayer.getEntityTranslucent(getTextureLocation(animatable));
    }
}
