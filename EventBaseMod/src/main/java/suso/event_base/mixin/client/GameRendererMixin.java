package suso.event_base.mixin.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.Framebuffer;
import net.minecraft.client.gl.PostEffectProcessor;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.entity.Entity;
import net.minecraft.resource.ResourceFactory;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import suso.event_base.client.shader.CustomUniformStore;
import suso.event_base.custom.render.CustomRender;

@Mixin(GameRenderer.class) @Environment(EnvType.CLIENT)
public abstract class GameRendererMixin {
    @Shadow @Nullable PostEffectProcessor postProcessor;
    @Shadow @Final MinecraftClient client;

    @Inject(
            method = "onCameraEntitySet",
            at = @At("HEAD"),
            cancellable = true
    )
    private void keepCustomShader(Entity entity, CallbackInfo ci) {
        if(CustomUniformStore.overridingPost) ci.cancel();
    }

    @ModifyVariable(
            method = "loadPostProcessor",
            at = @At("HEAD"),
            ordinal = 0,
            argsOnly = true
    )
    private Identifier setCustomShader(Identifier original) {
        return CustomUniformStore.overridingPost ? Identifier.of(CustomUniformStore.getPostOverride()) : original;
    }

    @Inject(
            method = "render",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gl/PostEffectProcessor;render(F)V"
            )
    )
    private void setDepth(RenderTickCounter tickCounter, boolean tick, CallbackInfo ci) {
        if(postProcessor == null) return;

        Framebuffer handBuffer = postProcessor.getSecondaryTarget("hand");
        if(handBuffer != null) handBuffer.copyDepthFrom(client.getFramebuffer());
        postProcessor.mainTarget.copyDepthFrom(CustomUniformStore.aux);
    }

    @Inject(
            method = "loadPrograms",
            at = @At("TAIL")
    )
    private void loadCustomShaders(ResourceFactory factory, CallbackInfo ci) {
        CustomRender.setupShaders(factory);
    }
}
