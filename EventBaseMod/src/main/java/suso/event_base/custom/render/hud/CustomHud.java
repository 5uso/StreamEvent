package suso.event_base.custom.render.hud;

import com.google.common.collect.ImmutableMap;
import com.mojang.authlib.GameProfile;
import io.netty.buffer.ByteBuf;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import suso.event_base.EvtBaseConstants;

import java.util.Map;
import java.util.UUID;
import java.util.function.Supplier;

public class CustomHud implements HudRenderCallback {
    public enum DataTypes {
        STATE, TIMER, FEED, AGILITY, PRIMATICA_SCORE, INFO
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

        currentStateHud.onHudRender(matrixStack, tickDelta);
    }

    public void setCurrentStateHud(StateHud hud) {
        currentStateHud = hud;
    }

    public void onHudData(ByteBuf msg) {
        DataTypes type = DataTypes.values()[msg.readInt()];
        switch(type) {
            case STATE -> {
                EvtBaseConstants.States state = EvtBaseConstants.States.values()[msg.readInt()];
                setCurrentStateHud(stateFactories.get(state).get());
            }
            default -> currentStateHud.onHudMessage(type, msg);
        }
    }
}
