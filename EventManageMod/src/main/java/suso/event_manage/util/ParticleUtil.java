package suso.event_manage.util;

import net.minecraft.scoreboard.AbstractTeam;
import net.minecraft.util.math.Vec3f;

import java.awt.*;

public class ParticleUtil {
    public static Vec3f teamColor(AbstractTeam team) {
        if(team == null) return new Vec3f(1.0f, 1.0f, 1.0f);

        Integer rgb = team.getColor().getColorValue();
        Color color = new Color(rgb == null ? 0xFFFFFF : rgb);

        Vec3f v_rgb = new Vec3f(color.getRed(), color.getGreen(), color.getBlue());
        v_rgb.scale(1.0f / 255.0f);

        return v_rgb;
    }
}
