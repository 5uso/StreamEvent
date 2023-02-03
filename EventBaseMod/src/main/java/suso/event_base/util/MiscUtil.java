package suso.event_base.util;

import net.minecraft.util.math.MathHelper;

public class MiscUtil {
    public static double smoothStep(double from, double to, double x) {
        x = MathHelper.clamp((x - from) / (to - from), 0.0, 1.0);
        return x * x * x * (x * (x * 6.0 - 15.0) + 10.0);
    }
}