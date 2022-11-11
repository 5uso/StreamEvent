package suso.event_base.util;

import net.minecraft.util.math.MathHelper;

public class MiscUtil {
    public static float smoothStep(float from, float to, float x) {
        x = MathHelper.clamp((x - from) / (to - from), 0.0f, 1.0f);
        return x * x * x * (x * (x * 6.0f - 15.0f) + 10.0f);
    }
}