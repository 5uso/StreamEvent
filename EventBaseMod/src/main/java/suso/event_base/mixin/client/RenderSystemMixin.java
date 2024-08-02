package suso.event_base.mixin.client;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gl.ShaderProgram;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import suso.event_base.custom.render.CustomRender;

@Mixin(RenderSystem.class) @Environment(EnvType.CLIENT)
public class RenderSystemMixin {
    @Inject(
            method = "getShader",
            at = @At(value = "RETURN"),
            cancellable = true
    )
    private static void replaceShader(CallbackInfoReturnable<ShaderProgram> cir) {
        if(CustomRender.getCurrentDrawShader() != null) cir.setReturnValue(CustomRender.getCurrentDrawShader());
    }
}
