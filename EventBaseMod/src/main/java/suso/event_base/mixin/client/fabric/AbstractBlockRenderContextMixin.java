package suso.event_base.mixin.client.fabric;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.impl.client.indigo.renderer.mesh.MutableQuadViewImpl;
import net.fabricmc.fabric.impl.client.indigo.renderer.render.AbstractBlockRenderContext;
import net.fabricmc.fabric.impl.client.indigo.renderer.render.BlockRenderInfo;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import suso.event_base.client.shader.ShaderNetworking;
import suso.event_base.custom.render.CustomRender;

@Mixin(value = AbstractBlockRenderContext.class, remap = false) @Environment(EnvType.CLIENT)
public abstract class AbstractBlockRenderContextMixin {
    @Shadow @Final protected BlockRenderInfo blockInfo;

    @Shadow protected abstract void colorizeQuad(MutableQuadViewImpl quad, int colorIndex);

    @Redirect(
            method = "renderQuad",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/fabricmc/fabric/impl/client/indigo/renderer/render/AbstractBlockRenderContext;colorizeQuad(Lnet/fabricmc/fabric/impl/client/indigo/renderer/mesh/MutableQuadViewImpl;I)V"
            )
    )
    private void customColorize(AbstractBlockRenderContext ctx, MutableQuadViewImpl quad, int colorIndex) {
        if(CustomRender.colorizeQuad(blockInfo.blockPos, quad)) return;
        colorizeQuad(quad, colorIndex);
    }

    @Inject(
            method = "shadeQuad",
            at = @At(value = "HEAD"),
            cancellable = true
    )
    private void forceNoShade(MutableQuadViewImpl quad, boolean isVanilla, boolean ao, boolean emissive, CallbackInfo ci) {
        if(ShaderNetworking.colors.containsKey(blockInfo.blockPos)) ci.cancel();
    }
}
