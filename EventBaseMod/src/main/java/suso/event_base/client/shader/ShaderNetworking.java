package suso.event_base.client.shader;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.math.BlockPos;
import suso.event_base.custom.network.payloads.SetBlockColorPayload;
import suso.event_base.custom.network.payloads.SetPostShaderPayload;
import suso.event_base.custom.network.payloads.SetShaderUniformPayload;

import java.util.HashMap;
import java.util.Map;

@Environment(EnvType.CLIENT)
public class ShaderNetworking {
    public static void registerPacketListeners() {
        PayloadTypeRegistry.playS2C().register(SetShaderUniformPayload.ID, SetShaderUniformPayload.CODEC);
        PayloadTypeRegistry.playS2C().register(SetPostShaderPayload.ID, SetPostShaderPayload.CODEC);
        PayloadTypeRegistry.playS2C().register(SetBlockColorPayload.ID, SetBlockColorPayload.CODEC);

        ClientPlayNetworking.registerGlobalReceiver(SetShaderUniformPayload.ID, ShaderNetworking::setShaderUniformHandler);
        ClientPlayNetworking.registerGlobalReceiver(SetPostShaderPayload.ID, ShaderNetworking::setPostShaderHandler);
        ClientPlayNetworking.registerGlobalReceiver(SetBlockColorPayload.ID, ShaderNetworking::setBlockColorHandler);
    }

    private static void setShaderUniformHandler(SetShaderUniformPayload p, ClientPlayNetworking.Context ctx) {
        MinecraftClient client = ctx.client();
        client.execute(p.floating ?
                () -> CustomUniformStore.setUniform(p.name, p.valuesFloat) :
                () -> CustomUniformStore.setUniform(p.name, p.valuesInt)
        );
    }

    private static void setPostShaderHandler(SetPostShaderPayload p, ClientPlayNetworking.Context ctx) {
        CustomUniformStore.setPostOverride(p.id.toString());

        MinecraftClient client = ctx.client();
        client.execute(() -> client.gameRenderer.onCameraEntitySet(client.getCameraEntity()));
    }

    public static final Map<BlockPos, Integer> colors = new HashMap<>();
    private static void setBlockColorHandler(SetBlockColorPayload p, ClientPlayNetworking.Context ctx) {
        if(p.colorSet) {
            colors.put(p.pos, p.color);
            return;
        }

        colors.remove(p.pos);
    }
}
