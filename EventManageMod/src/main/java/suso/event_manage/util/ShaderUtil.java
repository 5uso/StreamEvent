package suso.event_manage.util;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import suso.event_common.custom.network.payloads.SetBlockColorPayload;
import suso.event_common.custom.network.payloads.SetPostShaderPayload;
import suso.event_common.custom.network.payloads.SetShaderUniformPayload;

public class ShaderUtil {
    public static void setShaderUniform(ServerPlayerEntity player, String name, float... values) {
        ServerPlayNetworking.send(player, new SetShaderUniformPayload(name, values));
    }

    public static void setShaderUniform(ServerPlayerEntity player, String name, int... values) {
        ServerPlayNetworking.send(player, new SetShaderUniformPayload(name, values));
    }

    public static void setPostShader(ServerPlayerEntity player, Identifier id) {
        ServerPlayNetworking.send(player, new SetPostShaderPayload(id));
    }

    public static void setBlockColor(ServerPlayerEntity player, BlockPos pos, int color) {
        ServerPlayNetworking.send(player, new SetBlockColorPayload(pos, color));
    }

    public static void unsetBlockColor(ServerPlayerEntity player, BlockPos pos) {
        ServerPlayNetworking.send(player, new SetBlockColorPayload(pos));
    }
}
