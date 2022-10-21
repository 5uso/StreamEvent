package suso.event_base.client.shader;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import suso.event_base.EvtBaseConstants;
import suso.event_base.mixin.client.GameRendererAccess;

import java.util.HashMap;
import java.util.Map;

@Environment(EnvType.CLIENT)
public class ShaderNetworking {
    public static void registerPacketListeners() {
        ClientPlayNetworking.registerGlobalReceiver(EvtBaseConstants.SET_SHADER_UNIFORM, ShaderNetworking::setShaderUniformHandler);
        ClientPlayNetworking.registerGlobalReceiver(EvtBaseConstants.SET_POST_SHADER, ShaderNetworking::setPostShaderHandler);
        ClientPlayNetworking.registerGlobalReceiver(EvtBaseConstants.SET_BLOCK_COLOR, ShaderNetworking::setBlockColorHandler);
    }

    private static void setShaderUniformHandler(MinecraftClient client, ClientPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
        String name = buf.readString();
        boolean isFloat = buf.readBoolean();
        int count = buf.readInt();

        if(isFloat) {
            float[] values = new float[count];
            for(int i = 0; i < count; i++) values[i] = buf.readFloat();
            client.execute(() -> CustomUniformStore.setUniform(name, values));
        } else {
            int[] values = new int[count];
            for(int i = 0; i < count; i++) values[i] = buf.readInt();
            client.execute(() -> CustomUniformStore.setUniform(name, values));
        }
    }

    private static void setPostShaderHandler(MinecraftClient client, ClientPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
        String name = buf.readString();
        CustomUniformStore.setPostOverride(name);

        if(CustomUniformStore.overridingPost) {
            client.execute(() -> ((GameRendererAccess) client.gameRenderer).invokeLoadShader(new Identifier(name)));
            return;
        }

        client.execute(() -> client.gameRenderer.onCameraEntitySet(client.getCameraEntity()));
    }

    public static final Map<BlockPos, Integer> colors = new HashMap<>();
    private static void setBlockColorHandler(MinecraftClient client, ClientPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
        BlockPos pos = buf.readBlockPos();
        boolean set = buf.readBoolean();

        if(set) {
            int value = buf.readInt();
            colors.put(pos, value);
            return;
        }

        colors.remove(pos);
    }
}
