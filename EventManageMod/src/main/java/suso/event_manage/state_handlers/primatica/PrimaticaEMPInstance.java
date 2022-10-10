package suso.event_manage.state_handlers.primatica;

import net.minecraft.network.packet.s2c.play.EntityVelocityUpdateS2CPacket;
import net.minecraft.particle.DustParticleEffect;
import net.minecraft.scoreboard.AbstractTeam;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import suso.event_manage.state_handlers.TickableInstance;
import suso.event_manage.util.MiscUtil;
import suso.event_manage.util.ParticleUtil;

public class PrimaticaEMPInstance implements TickableInstance {
    private final ServerWorld world;
    private final AbstractTeam team;
    private final Vec3d position;
    private final PrimaticaIngameHandler handler;

    private int ticksLeft;

    public PrimaticaEMPInstance(ServerPlayerEntity player, BlockPos pos, PrimaticaIngameHandler handler) {
        world = player.getWorld();
        team = player.getScoreboardTeam();
        position = new Vec3d(pos.getX(), pos.getY(), pos.getZ()).add(0.5, 0.5, 0.5);
        this.handler = handler;

        ticksLeft = 120;
    }

    @Override
    public boolean tick() {
        if(--ticksLeft < 0) return true;

        for(ServerPlayerEntity player : world.getPlayers()) {
            if(player.isSpectator()) continue;

            if(!player.isTeamPlayer(team)) {
                double distance = MiscUtil.distance(player.getBoundingBox(), position);

                if(distance < 3.0) {
                    Vec3d motion = player.getVelocity();
                    Vec3d pushDirection = player.getPos().subtract(position).normalize();
                    double magnitude = 1.0 - distance / 3.0;

                    motion = motion.add(pushDirection.multiply(magnitude));
                    player.setVelocity(motion);

                    player.networkHandler.sendPacket(new EntityVelocityUpdateS2CPacket(player));
                }
            }

            Vec3d playerCamPos = player.getEyePos();
            double camDistance = playerCamPos.distanceTo(position);
            if(camDistance < 3.0) {
                PrimaticaPlayerInfo info = handler.getPlayerInfo(player.getUuid());
                info.withinEMPNow = true;
            }
        }

        for(float pitch = -90.0f; pitch < 91.0f; pitch += 30.0f) {
            for(float yaw = 0.0f; yaw < 360.0f; yaw += 30.0f) {
                Vec3d particlePos = position.add(Vec3d.fromPolar(pitch, yaw).multiply(3.0));
                world.spawnParticles(new DustParticleEffect(ParticleUtil.teamColor(team), 0.5f), particlePos.x, particlePos.y, particlePos.z, 1, 0.0, 0.0, 0.0, 0.0);
            }
        }
        world.spawnParticles(new DustParticleEffect(ParticleUtil.teamColor(team), 2.0f), position.x, position.y, position.z, 1, 0.0, 0.0, 0.0, 0.0);

        return false;
    }

    @Override
    public void remove() {
        ticksLeft = 0;
    }
}
