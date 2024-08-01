package suso.event_base.custom.blocks.entity;

import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.renderer.GeoBlockRenderer;
import software.bernie.geckolib.util.Color;

public class PrimaticaRespawnRenderer extends GeoBlockRenderer<PrimaticaRespawnBlockEntity> {
    public PrimaticaRespawnRenderer() {
        super(new PrimaticaRespawnModel());
    }

    @Override
    public RenderLayer getRenderType(PrimaticaRespawnBlockEntity animatable, Identifier texture, @Nullable VertexConsumerProvider bufferSource, float partialTick) {
        return RenderLayer.getEntityTranslucent(getTextureLocation(animatable));
    }

    @Override
    public Color getRenderColor(PrimaticaRespawnBlockEntity animatable, float partialTick, int packedLight) {
        return animatable.getColor();
    }
}
