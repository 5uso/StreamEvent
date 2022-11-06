package suso.event_base.custom.render;

import com.mojang.authlib.Agent;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.ProfileLookupCallback;
import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.PlayerSkinDrawer;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.server.WhitelistEntry;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

import java.util.UUID;

public class HudTest implements HudRenderCallback {
    Identifier textureId = null;

    @Override
    public void onHudRender(MatrixStack matrixStack, float tickDelta) {
        MinecraftClient client = MinecraftClient.getInstance();

        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);

        if(textureId == null) {
            GameProfile pro = client.getSessionService().fillProfileProperties(new GameProfile(new UUID(-5599802556614032055L, -8045781136487407554L), "Asometric"), false);
            textureId = client.getSkinProvider().loadSkin(pro);
            System.out.println(textureId.toString());
        }
        RenderSystem.setShaderTexture(0, textureId);

        PlayerSkinDrawer.draw(matrixStack, 0, 100, 16);
        DrawableHelper.drawTexture(matrixStack, 0, 100, 0, 0, 64, 64, 64, 64);
        DrawableHelper.drawCenteredText(matrixStack, MinecraftClient.getInstance().textRenderer, "haha", client.getWindow().getScaledWidth() / 2, 10, Formatting.GOLD.getColorValue());
    }
}
