package suso.event_base.mixin.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gl.ShaderProgram;
import net.minecraft.client.gui.DrawContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import suso.event_base.custom.render.CustomRender;

import java.util.function.Supplier;

@Mixin(DrawContext.class) @Environment(EnvType.CLIENT)
public class DrawableHelperMixin {
    @ModifyArg(
            method = "drawTexturedQuad",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/mojang/blaze3d/systems/RenderSystem;setShader(Ljava/util/function/Supplier;)V"
            )
    )
    private static Supplier<ShaderProgram> replaceShader(Supplier<ShaderProgram> shaderSupplier) {
        return CustomRender.getCurrentDrawShader() == null ? shaderSupplier : CustomRender::getCurrentDrawShader;
    }

    @ModifyArg(
            method = "fill(Lnet/minecraft/util/math/Matrix4f;IIIII)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/mojang/blaze3d/systems/RenderSystem;setShader(Ljava/util/function/Supplier;)V"
            )
    )
    private static Supplier<ShaderProgram> replaceFillShader(Supplier<ShaderProgram> shaderSupplier) {
        return CustomRender.getCurrentDrawShader() == null ? shaderSupplier : CustomRender::getCurrentDrawShader;
    }
}
