package suso.event_base.mixin.client;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.GlUniform;
import net.minecraft.client.gl.JsonEffectShaderProgram;
import net.minecraft.client.gl.PostEffectPass;
import net.minecraft.client.gl.Uniform;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import suso.event_base.client.ModCheck;
import suso.event_base.client.shader.CustomUniformStore;
import suso.event_base.client.shader.IGlUniformUtil;

import java.util.Map;

@Mixin(PostEffectPass.class) @Environment(EnvType.CLIENT)
public class PostProcessMixin {
    @Shadow @Final private JsonEffectShaderProgram program;

    @Inject(
            method = "render",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gl/JsonEffectShaderProgram;enable()V"
            )
    )
    private void injectUniforms(float time, CallbackInfo ci) {
        for (Map.Entry<String, float[]> uniform : CustomUniformStore.floatEntries()) {
            Uniform u = this.program.getUniformByNameOrDummy(uniform.getKey());
            if (u instanceof GlUniform glu && uniform.getValue().length == glu.getCount() && glu.getDataType() == 4) {
                u.set(uniform.getValue());
            }
        }
        for (Map.Entry<String, int[]> uniform : CustomUniformStore.intEntries()) {
            Uniform u = this.program.getUniformByNameOrDummy(uniform.getKey());
            if (u instanceof GlUniform glu && uniform.getValue().length == glu.getCount() && glu.getDataType() == 0) {
                ((IGlUniformUtil) glu).set(uniform.getValue());
            }
        }

        this.program.getUniformByNameOrDummy("GameTime").set(RenderSystem.getShaderGameTime());
        this.program.getUniformByNameOrDummy("SysTime").set((int)ModCheck.getTime() % 0x80000000);

        MinecraftClient client = MinecraftClient.getInstance();

        double fov = client.gameRenderer.getFov(client.gameRenderer.getCamera(), client.getRenderTickCounter().getTickDelta(false), true);
        this.program.getUniformByNameOrDummy("FOV").set((float) fov);
    }
}
