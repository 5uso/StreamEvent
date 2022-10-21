package suso.event_base.mixin.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import suso.event_base.client.shader.CustomUniformStore;

@Mixin(GameRenderer.class) @Environment(EnvType.CLIENT)
public abstract class GameRendererMixin {
    @Shadow protected abstract void loadShader(Identifier id);

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
        if(CustomUniformStore.overridingPost) this.loadShader(new Identifier(CustomUniformStore.getPostOverride()));
    }
}
