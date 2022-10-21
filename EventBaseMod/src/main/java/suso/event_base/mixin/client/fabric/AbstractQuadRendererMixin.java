package suso.event_base.mixin.client.fabric;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.impl.client.indigo.renderer.mesh.MutableQuadViewImpl;
import net.fabricmc.fabric.impl.client.indigo.renderer.render.AbstractQuadRenderer;
import net.fabricmc.fabric.impl.client.indigo.renderer.render.BlockRenderInfo;
import net.minecraft.client.render.RenderLayer;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import suso.event_base.custom.blocks.EMPBlock;

@Mixin(AbstractQuadRenderer.class) @Environment(EnvType.CLIENT)
public abstract class AbstractQuadRendererMixin {
    @Shadow(remap = false) @Final protected BlockRenderInfo blockInfo;
    @Shadow(remap = false) protected abstract void colorizeQuad(MutableQuadViewImpl q, int blockColorIndex);
    @Shadow(remap = false) protected abstract void bufferQuad(MutableQuadViewImpl quad, RenderLayer renderLayer);

    @Inject(
            method = "tessellateSmooth",
            at = @At("HEAD"),
            cancellable = true
    )
    private void hijackSmoothQuadRender(MutableQuadViewImpl q, RenderLayer renderLayer, int blockColorIndex, CallbackInfo ci) {
        if(blockInfo.blockState.getBlock() instanceof EMPBlock) {
            colorizeQuad(q, blockColorIndex);
            bufferQuad(q, renderLayer);
            ci.cancel();
        }
    }

    @Inject(
            method = "tessellateFlat",
            at = @At("HEAD"),
            cancellable = true
    )
    private void hijackFlatQuadRender(MutableQuadViewImpl q, RenderLayer renderLayer, int blockColorIndex, CallbackInfo ci) {
        if(blockInfo.blockState.getBlock() instanceof EMPBlock) {
            colorizeQuad(q, blockColorIndex);
            bufferQuad(q, renderLayer);
            ci.cancel();
        }
    }
}
