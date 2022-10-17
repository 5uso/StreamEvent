package suso.event_manage.util;

import net.minecraft.network.packet.s2c.play.ParticleS2CPacket;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.scoreboard.AbstractTeam;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.Vec3f;
import suso.event_manage.EventManager;

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

    public static <T extends ParticleEffect> void forceParticle(T particle, double x, double y, double z, int count, double deltaX, double deltaY, double deltaZ, double speed) {
        ParticleS2CPacket p = new ParticleS2CPacket(particle, true, x, y, z, (float)deltaX, (float)deltaY, (float)deltaZ, (float)speed, count);
        for(ServerPlayerEntity player : EventManager.getInstance().getServer().getOverworld().getPlayers()) player.networkHandler.sendPacket(p);
    }
}
