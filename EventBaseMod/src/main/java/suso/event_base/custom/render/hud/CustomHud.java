package suso.event_base.custom.render.hud;

import com.google.common.collect.ImmutableMap;
import com.mojang.authlib.GameProfile;
import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.PlayerSkinDrawer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import suso.event_base.EvtBaseConstants;

import java.util.Map;
import java.util.UUID;
import java.util.function.Supplier;

public class CustomHud implements HudRenderCallback {
    public enum DataTypes {
        STATE, TIMER, FEED
    }

    private static final Map<EvtBaseConstants.States, Supplier<StateHud>> stateFactories = ImmutableMap.<EvtBaseConstants.States, Supplier<StateHud>>builder()
            .put(EvtBaseConstants.States.IDLE, IdleHud::new)
            .put(EvtBaseConstants.States.PRIMATICA_INGAME, PrimaticaIngameHud::new)
            .build();

    private StateHud currentStateHud;
    private Identifier textureId = null;

    public CustomHud() {
        currentStateHud = new IdleHud();
    }

    @Override
    public void onHudRender(MatrixStack matrixStack, float tickDelta) {
        MinecraftClient client = MinecraftClient.getInstance();

        if(textureId  == null) {
            GameProfile pro = client.getSessionService().fillProfileProperties(new GameProfile(new UUID(-5599802556614032055L, -8045781136487407554L), "Asometric"), false);
            textureId = client.getSkinProvider().loadSkin(pro);
        }

        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, textureId);
        PlayerSkinDrawer.draw(matrixStack, 0, 100, 32);

        matrixStack.push();
        matrixStack.scale(5.0f, 5.0f, 5.0f);
        float x = client.getWindow().getScaledWidth() / 10.0f - MinecraftClient.getInstance().textRenderer.getWidth("haha") / 2.0f;
        float y = client.getWindow().getScaledHeight() / 10.0f;
        MinecraftClient.getInstance().textRenderer.draw(matrixStack, "haha", x, y, Formatting.GOLD.getColorValue());
        matrixStack.pop();

        currentStateHud.onHudRender(matrixStack, tickDelta);
    }

    public void setCurrentStateHud(StateHud hud) {
        currentStateHud = hud;
    }

    public void onHudData(PacketByteBuf msg) {
        DataTypes type = DataTypes.values()[msg.getInt(0)];
        switch(type) {
            case STATE -> {
                EvtBaseConstants.States state = EvtBaseConstants.States.values()[msg.getInt(1)];
                setCurrentStateHud(stateFactories.get(state).get());
            }
            default -> currentStateHud.onHudMessage(msg);
        }
    }
}
