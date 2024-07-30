package suso.event_manage.util;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LightningEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import suso.event_manage.EventManager;
import suso.event_manage.custom.network.packets.JumpInputPacket;
import suso.event_manage.custom.network.payloads.EntityUpdatePayload;

public class MiscUtil {
    public static double distance(Box rect, Vec3d p) {
        double dx = Math.max(Math.max(rect.minX - p.x, 0.0), p.x - rect.maxX);
        double dy = Math.max(Math.max(rect.minY - p.y, 0.0), p.y - rect.maxY);
        double dz = Math.max(Math.max(rect.minZ - p.z, 0.0), p.z - rect.maxZ);
        return Math.sqrt(dx*dx + dy*dy + dz*dz);
    }

    public static void handleJumpInput(JumpInputPacket payload, ServerPlayNetworking.Context context) {
        context.player().setJumpPressed(payload.pressed);
    }

    public static double vec3Angle(Vec3d a, Vec3d b) {
        return Math.acos(a.normalize().dotProduct(b.normalize()));
    }

    public static BlockPos blockPosFrom3d(Vec3d v) {
        return new BlockPos((int) Math.floor(v.x), (int) Math.floor(v.y), (int) Math.floor(v.z));
    }

    public static void flashSky(double x, double z) {
        ServerWorld world = EventManager.getInstance().getServer().getOverworld();
        LightningEntity entity = new LightningEntity(EntityType.LIGHTNING_BOLT, world);
        entity.setPos(x, 3000.0, z);
        entity.setCosmetic(true);
        entity.setSilent(true);

        world.spawnEntity(entity);
    }

    public static void setBlockEntityNBT(BlockEntity be, NbtCompound nbt) {
        World w = be.getWorld();
        if(w == null) return;

        BlockState blockState = w.getBlockState(be.getPos());
        be.readNbt(nbt);
        be.markDirty();
        w.updateListeners(be.getPos(), blockState, blockState, 3);
    }

    public static void sendCustomEntityUpdate(Entity e, NbtCompound nbt) {
        if(e.getWorld() instanceof ServerWorld sw) {
            CustomPayload p = new EntityUpdatePayload(e, nbt);
            sw.getPlayers().forEach(player -> ServerPlayNetworking.send(player, p));
        }
    }
}
