package suso.event_base.custom.render;

import com.mojang.authlib.GameProfile;
import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.PlayerSkinDrawer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

import java.util.UUID;

public class HudTest implements HudRenderCallback {
    Identifier textureId = null;

    @Override
    public void onHudRender(MatrixStack matrixStack, float tickDelta) {
        MinecraftClient client = MinecraftClient.getInstance();

        CustomRender.setCurrentDrawShader(CustomRender.getFeedShader());
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);

        if(textureId == null) {
            GameProfile pro = client.getSessionService().fillProfileProperties(new GameProfile(new UUID(-5599802556614032055L, -8045781136487407554L), "Asometric"), false);
            textureId = client.getSkinProvider().loadSkin(pro);
        }
        RenderSystem.setShaderTexture(0, textureId);

        PlayerSkinDrawer.draw(matrixStack, 0, 100, 32);

        matrixStack.push();
        matrixStack.scale(5.0f, 5.0f, 5.0f);
        float x = client.getWindow().getScaledWidth() / 10.0f - MinecraftClient.getInstance().textRenderer.getWidth("haha") / 2.0f;
        float y = client.getWindow().getScaledHeight() / 10.0f;
        MinecraftClient.getInstance().textRenderer.draw(matrixStack, "haha", x, y, Formatting.GOLD.getColorValue());
        matrixStack.pop();

        RenderSystem.disableBlend();
        CustomRender.setCurrentDrawShader(null);
    }
}
