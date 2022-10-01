package suso.event_manage.util;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import suso.event_manage.EvtBaseConstants;

public class ShaderUtil {
    public static void setShaderUniform(ServerPlayerEntity player, String name, float... values) {
        PacketByteBuf p = PacketByteBufs.create();

        p.writeString(name);
        p.writeBoolean(true);
        p.writeInt(values.length);
        for(float f : values) p.writeFloat(f);

        ServerPlayNetworking.send(player, EvtBaseConstants.SET_SHADER_UNIFORM, p);
    }

    public static void setShaderUniform(ServerPlayerEntity player, String name, int... values) {
        PacketByteBuf p = PacketByteBufs.create();

        p.writeString(name);
        p.writeBoolean(true);
        p.writeInt(values.length);
        for(int i : values) p.writeInt(i);

        ServerPlayNetworking.send(player, EvtBaseConstants.SET_SHADER_UNIFORM, p);
    }

    public static void setPostShader(ServerPlayerEntity player, Identifier id) {
        PacketByteBuf p = PacketByteBufs.create();

        p.writeString(id.toString());

        ServerPlayNetworking.send(player, EvtBaseConstants.SET_POST_SHADER, p);
    }
}
