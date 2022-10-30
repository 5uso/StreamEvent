package suso.event_base.mixin;

import com.mojang.blaze3d.platform.GlStateManager;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.lwjgl.opengl.GL30;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import suso.event_base.client.shader.CustomUniformStore;

@Mixin(GlStateManager.class) @Environment(EnvType.CLIENT)
public class TestMixin {
    @Inject(
            method = "_glBindFramebuffer",
            at = @At("HEAD")
    )
    private static void what(int target, int framebuffer, CallbackInfo ci) {
        switch (target) {
            case GL30.GL_READ_FRAMEBUFFER -> CustomUniformStore.readID = framebuffer;
            case GL30.GL_DRAW_FRAMEBUFFER -> CustomUniformStore.drawID = framebuffer;
            case GL30.GL_FRAMEBUFFER -> CustomUniformStore.globID = framebuffer;
        }
    }
}
