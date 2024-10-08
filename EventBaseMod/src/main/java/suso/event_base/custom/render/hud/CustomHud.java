package suso.event_base.custom.render.hud;

import com.google.common.collect.ImmutableMap;
import com.mojang.authlib.yggdrasil.ProfileResult;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.util.Identifier;
import suso.event_common.custom.network.payloads.HudDataPayload;
import suso.event_common.EventConstants;

import java.util.Map;
import java.util.UUID;
import java.util.function.Supplier;

public class CustomHud implements HudRenderCallback {
    private static final Map<EventConstants.States, Supplier<StateHud>> stateFactories = ImmutableMap.<EventConstants.States, Supplier<StateHud>>builder()
            .put(EventConstants.States.IDLE, IdleHud::new)
            .put(EventConstants.States.PRIMATICA_INGAME, PrimaticaIngameHud::new)
            .build();

    private StateHud currentStateHud;
    private Identifier textureId = null;

    public CustomHud() {
        currentStateHud = new IdleHud();
    }

    @Override
    public void onHudRender(DrawContext ctx, RenderTickCounter tickCounter) {
        MinecraftClient client = MinecraftClient.getInstance();

        if(textureId == null) {
            ProfileResult pro = client.getSessionService().fetchProfile(new UUID(-5599802556614032055L, -8045781136487407554L), false);
            if(pro != null) textureId = client.getSkinProvider().getSkinTextures(pro.profile()).texture();
        }

        currentStateHud.onHudRender(ctx, tickCounter);
    }

    public void setCurrentStateHud(StateHud hud) {
        currentStateHud = hud;
    }

    public void onHudData(HudDataPayload p) {
        switch(p.type) {
            case STATE -> {
                EventConstants.States state = EventConstants.States.values()[p.buf.readInt()];
                setCurrentStateHud(stateFactories.get(state).get());
            }
            default -> currentStateHud.onHudMessage(p);
        }
    }
}
