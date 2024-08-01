package suso.event_base.custom.blocks.entity;

import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.renderer.GeoBlockRenderer;
import software.bernie.geckolib.util.Color;

public class PrimaticaDoorRenderer  extends GeoBlockRenderer<PrimaticaDoorBlockEntity> {
    public PrimaticaDoorRenderer() {
        super(new PrimaticaDoorModel());
    }

    @Override
    public RenderLayer getRenderType(PrimaticaDoorBlockEntity animatable, Identifier texture, @Nullable VertexConsumerProvider bufferSource, float partialTick) {
        return RenderLayer.getEntityTranslucent(getTextureLocation(animatable));
    }

    @Override
    public Color getRenderColor(PrimaticaDoorBlockEntity animatable, float partialTick, int packedLight) {
        return animatable.getColor();
    }
}
