package suso.event_base.mixin.client;

import com.mojang.blaze3d.platform.GlStateManager;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.Framebuffer;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Matrix4f;
import org.lwjgl.opengl.GL30;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import suso.event_base.client.shader.CustomUniformStore;

@Mixin(WorldRenderer.class) @Environment(EnvType.CLIENT)
public class WorldRendererMixin {
    @Shadow @Final private MinecraftClient client;

    @Inject(
            method = "render",
            at = @At("RETURN")
    )
    private void storeDepth(MatrixStack matrices, float tickDelta, long limitTime, boolean renderBlockOutline, Camera camera, GameRenderer gameRenderer, LightmapTextureManager lightmapTextureManager, Matrix4f positionMatrix, CallbackInfo ci) {
        if(!CustomUniformStore.overridingPost || !client.gameRenderer.shadersEnabled) return;

        Framebuffer clientBuffer = client.getFramebuffer();
        if(CustomUniformStore.aux.textureHeight != clientBuffer.textureHeight || CustomUniformStore.aux.textureWidth != clientBuffer.textureWidth) CustomUniformStore.aux.resize(clientBuffer.textureWidth, clientBuffer.textureHeight, false);

        int globID = CustomUniformStore.globID;
        int readID = CustomUniformStore.readID;
        int drawID = CustomUniformStore.drawID;

        GlStateManager._glBindFramebuffer(GL30.GL_READ_FRAMEBUFFER, clientBuffer.fbo);
        GlStateManager._glBindFramebuffer(GL30.GL_DRAW_FRAMEBUFFER, CustomUniformStore.aux.fbo);
        GlStateManager._glBlitFrameBuffer(0, 0, clientBuffer.textureWidth, clientBuffer.textureHeight, 0, 0, CustomUniformStore.aux.textureWidth, CustomUniformStore.aux.textureHeight, 256, 9728);
        GlStateManager._glBindFramebuffer(GL30.GL_FRAMEBUFFER, globID);
        GlStateManager._glBindFramebuffer(GL30.GL_READ_FRAMEBUFFER, readID);
        GlStateManager._glBindFramebuffer(GL30.GL_DRAW_FRAMEBUFFER, drawID);
    }
}
