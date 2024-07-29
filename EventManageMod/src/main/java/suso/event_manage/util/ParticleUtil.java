package suso.event_manage.util;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.s2c.play.ParticleS2CPacket;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.scoreboard.AbstractTeam;
import net.minecraft.server.network.ServerPlayerEntity;
import org.joml.Vector3f;
import suso.event_manage.EventManager;
import suso.event_manage.EvtBaseConstants;
import suso.event_manage.custom.network.payloads.FireworkParticlePayload;

import java.awt.*;
import java.util.List;

public class ParticleUtil {
    public static Vector3f teamColor(AbstractTeam team) {
        if(team == null) return new Vector3f(1.0f, 1.0f, 1.0f);

        Integer rgb = team.getColor().getColorValue();
        Color color = new Color(rgb == null ? 0xFFFFFF : rgb);

        Vector3f v_rgb = new Vector3f(color.getRed(), color.getGreen(), color.getBlue());
        v_rgb.mul(1.0f / 255.0f);

        return v_rgb;
    }

    public static <T extends ParticleEffect> void forceParticle(T particle, double x, double y, double z, int count, double deltaX, double deltaY, double deltaZ, double speed) {
        ParticleS2CPacket p = new ParticleS2CPacket(particle, true, x, y, z, (float)deltaX, (float)deltaY, (float)deltaZ, (float)speed, count);
        for(ServerPlayerEntity player : EventManager.getInstance().getServer().getOverworld().getPlayers()) player.networkHandler.sendPacket(p);
    }

    public static void fireworkParticle(List<ServerPlayerEntity> players, double x, double y, double z, double vx, double vy, double vz, NbtCompound firework) {
        players.forEach(player -> ServerPlayNetworking.send(player, new FireworkParticlePayload(x, y, z, vx, vy, vz, firework)));
    }
}
