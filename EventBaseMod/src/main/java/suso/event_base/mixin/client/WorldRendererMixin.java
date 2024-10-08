package suso.event_base.mixin.client;

import com.mojang.blaze3d.platform.GlStateManager;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.Framebuffer;
import net.minecraft.client.render.*;
import net.minecraft.client.world.ClientWorld;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL30;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import suso.event_base.client.shader.CustomUniformStore;

@Mixin(WorldRenderer.class) @Environment(EnvType.CLIENT)
public class WorldRendererMixin {
    @Shadow @Final private MinecraftClient client;

    @Shadow @Nullable private ClientWorld world;

    @Inject(
            method = "render",
            at = @At("RETURN")
    )
    private void storeDepth(RenderTickCounter tickCounter, boolean renderBlockOutline, Camera camera, GameRenderer gameRenderer, LightmapTextureManager lightmapTextureManager, Matrix4f matrix4f, Matrix4f matrix4f2, CallbackInfo ci) {
        if(!CustomUniformStore.overridingPost || !client.gameRenderer.postProcessorEnabled) return;

        Framebuffer clientBuffer = client.getFramebuffer();
        if(CustomUniformStore.aux.textureHeight != clientBuffer.textureHeight || CustomUniformStore.aux.textureWidth != clientBuffer.textureWidth) CustomUniformStore.aux.resize(clientBuffer.textureWidth, clientBuffer.textureHeight, false);

        int globID = 1; //CustomUniformStore.globID;
        int readID = 2; //CustomUniformStore.readID;
        int drawID = 1; //CustomUniformStore.drawID;

        GlStateManager._glBindFramebuffer(GL30.GL_READ_FRAMEBUFFER, clientBuffer.fbo);
        GlStateManager._glBindFramebuffer(GL30.GL_DRAW_FRAMEBUFFER, CustomUniformStore.aux.fbo);
        GlStateManager._glBlitFrameBuffer(0, 0, clientBuffer.textureWidth, clientBuffer.textureHeight, 0, 0, CustomUniformStore.aux.textureWidth, CustomUniformStore.aux.textureHeight, 256, 9728);
        GlStateManager._glBindFramebuffer(GL30.GL_FRAMEBUFFER, globID);
        GlStateManager._glBindFramebuffer(GL30.GL_READ_FRAMEBUFFER, readID);
        GlStateManager._glBindFramebuffer(GL30.GL_DRAW_FRAMEBUFFER, drawID);
    }

    @Redirect(
            method = "renderSky",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/render/BufferRenderer;drawWithGlobalProgram(Lnet/minecraft/client/render/BuiltBuffer;)V",
                    ordinal = 1
            )
    )
    private void controlSunRender(BuiltBuffer buffer) {
        if(this.world == null) return;

        float angle = this.world.getSkyAngle(0.0f) * 360.0F;
        if(angle > 120.0f && angle < 240.0f) {
            buffer.close();
            return;
        }

        BufferRenderer.drawWithGlobalProgram(buffer);
    }

    @Redirect(
            method = "renderSky",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/render/BufferRenderer;drawWithGlobalProgram(Lnet/minecraft/client/render/BuiltBuffer;)V",
                    ordinal = 2
            )
    )
    private void controlMoonRender(BuiltBuffer buffer) {
        if(this.world == null) return;

        float angle = this.world.getSkyAngle(0.0f) * 360.0F;
        if(angle > 300.0f || angle < 60.0f) {
            buffer.close();
            return;
        }

        BufferRenderer.drawWithGlobalProgram(buffer);
    }
}
