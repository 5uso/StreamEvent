package suso.event_base.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import suso.event_base.client.shader.ShaderNetworking;
import suso.event_base.client.sound.SoundNetworking;
import suso.event_base.custom.render.CustomRender;

@Environment(EnvType.CLIENT)
public class Event_baseClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        SoundNetworking.registerPacketListeners();
        ShaderNetworking.registerPacketListeners();
        ModCheck.registerPacketListeners();

        CustomRender.setupRenderLayers();
    }
}
