package suso.event_base.mixin.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.GlUniform;
import net.minecraft.client.gl.Uniform;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.Shader;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import suso.event_base.client.shader.CustomUniformStore;
import suso.event_base.client.shader.IGlUniformUtil;

import java.util.Map;

@Mixin(Shader.class) @Environment(EnvType.CLIENT)
public abstract class CoreMixin {
    @Shadow public abstract Uniform getUniformOrDefault(String name);

    @Inject(
            method = "bind",
            at = @At("HEAD")
    )
    private void injectUniforms(CallbackInfo ci) {
        for (Map.Entry<String, float[]> uniform : CustomUniformStore.floatEntries()) {
            Uniform u = this.getUniformOrDefault(uniform.getKey());
            if (u instanceof GlUniform glu && uniform.getValue().length == glu.getCount() && glu.getDataType() == 4) {
                u.set(uniform.getValue());
            }
        }
        for (Map.Entry<String, int[]> uniform : CustomUniformStore.intEntries()) {
            Uniform u = this.getUniformOrDefault(uniform.getKey());
            if (u instanceof GlUniform glu && uniform.getValue().length == glu.getCount() && glu.getDataType() == 0) {
                ((IGlUniformUtil) glu).set(uniform.getValue());
            }
        }

        MinecraftClient client = MinecraftClient.getInstance();
        if(client.player != null) {
            this.getUniformOrDefault("PlayerPos").set(new Vec3f((float) client.player.getX(), (float) client.player.getY(), (float) client.player.getZ()));
        }
    }
}
