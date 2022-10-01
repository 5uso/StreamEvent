package suso.event_manage.game_info;

import net.minecraft.util.math.Vec3d;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class PrimaticaInfo {
    private static final Random r = new Random();

    private static final Vec3d[] orbLocations = {
            new Vec3d(-65.5, 91, -9.5),
            new Vec3d(-66.5, 95, -19.5),
            new Vec3d(-59.5, 96, -1.5),
            new Vec3d(-70.5, 86.5, -0.5),
            new Vec3d(-60.5, 105, -5.5),
            new Vec3d(-42.5, 89.5, -2.5),
    };

    private static final Set<Vec3d> orbLocationset = new HashSet<>(Arrays.asList(orbLocations));

    public static Vec3d randomOrbLocation() {
        return orbLocations[r.nextInt(orbLocations.length)];
    }

    public static Set<Vec3d> getOrbLocations() {
        return new HashSet<>(orbLocationset);
    }
}
