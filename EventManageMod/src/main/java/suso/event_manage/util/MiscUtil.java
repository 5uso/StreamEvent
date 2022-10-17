package suso.event_manage.util;

import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import suso.event_manage.injected_interfaces.ServerPlayerEntityExtended;

public class MiscUtil {
    public static double distance(Box rect, Vec3d p) {
        double dx = Math.max(Math.max(rect.minX - p.x, 0.0), p.x - rect.maxX);
        double dy = Math.max(Math.max(rect.minY - p.y, 0.0), p.y - rect.maxY);
        double dz = Math.max(Math.max(rect.minZ - p.z, 0.0), p.z - rect.maxZ);
        return Math.sqrt(dx*dx + dy*dy + dz*dz);
    }

    public static void handleJumpInput(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
        ((ServerPlayerEntityExtended) player).setJumpPressed(buf.readBoolean());
    }
}
