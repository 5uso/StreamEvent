package suso.event_manage.util;

import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;

public class MiscUtil {
    public static double distance(Box rect, Vec3d p) {
        double dx = Math.max(Math.max(rect.minX - p.x, 0.0), p.x - rect.maxX);
        double dy = Math.max(Math.max(rect.minY - p.y, 0.0), p.y - rect.maxY);
        double dz = Math.max(Math.max(rect.minZ - p.z, 0.0), p.z - rect.maxZ);
        return Math.sqrt(dx*dx + dy*dy + dz*dz);
    }
}
