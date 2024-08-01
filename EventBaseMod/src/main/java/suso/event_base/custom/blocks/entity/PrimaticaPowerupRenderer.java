package suso.event_base.custom.blocks.entity;

import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.renderer.GeoBlockRenderer;

public class PrimaticaPowerupRenderer extends GeoBlockRenderer<PrimaticaPowerupBlockEntity> {
    public PrimaticaPowerupRenderer() {
        super(new PrimaticaPowerupModel());
    }

    @Override
    public RenderLayer getRenderType(PrimaticaPowerupBlockEntity animatable, Identifier texture, @Nullable VertexConsumerProvider bufferSource, float partialTick) {
        return RenderLayer.getEntityTranslucent(getTextureLocation(animatable));
    }
}
