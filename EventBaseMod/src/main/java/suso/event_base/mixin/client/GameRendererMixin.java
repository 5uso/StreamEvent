package suso.event_base.mixin.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.Framebuffer;
import net.minecraft.client.gl.PostEffectProcessor;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import suso.event_base.client.shader.CustomUniformStore;
import suso.event_base.custom.render.CustomRender;

@Mixin(GameRenderer.class) @Environment(EnvType.CLIENT)
public abstract class GameRendererMixin {
    @Shadow protected abstract void loadShader(Identifier id);
    @Shadow @Nullable private PostEffectProcessor shader;

    @Shadow @Final private MinecraftClient client;

    @Inject(
            method = "onCameraEntitySet",
            at = @At("HEAD"),
            cancellable = true
    )
    private void keepCustomShader(Entity entity, CallbackInfo ci) {
        if(CustomUniformStore.overridingPost) ci.cancel();
    }

    @Inject(
            method = "reload",
            at = @At("TAIL")
    )
    private void setCustomShader(ResourceManager manager, CallbackInfo ci) {
        if(CustomUniformStore.overridingPost) this.loadShader(Identifier.of(CustomUniformStore.getPostOverride()));
    }

    @Inject(
            method = "render",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gl/ShaderEffect;render(F)V"
            )
    )
    private void setDepth(float tickDelta, long startTime, boolean tick, CallbackInfo ci) {
        if(shader == null) return;

        Framebuffer handBuffer = shader.getSecondaryTarget("hand");
        if(handBuffer != null) handBuffer.copyDepthFrom(client.getFramebuffer());
        shader.mainTarget.copyDepthFrom(CustomUniformStore.aux);
    }

    @Inject(
            method = "loadShaders",
            at = @At("TAIL")
    )
    private void loadCustomShaders(ResourceManager manager, CallbackInfo ci) {
        CustomRender.setupShaders(manager);
    }
}
