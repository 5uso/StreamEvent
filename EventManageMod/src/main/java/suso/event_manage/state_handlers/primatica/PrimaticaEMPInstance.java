package suso.event_manage.state_handlers.primatica;

import net.minecraft.network.packet.s2c.play.EntityVelocityUpdateS2CPacket;
import net.minecraft.particle.DustParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.scoreboard.AbstractTeam;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.registry.Registry;
import suso.event_manage.state_handlers.TickableInstance;
import suso.event_manage.util.MiscUtil;
import suso.event_manage.util.ParticleUtil;
import suso.event_manage.util.ShaderUtil;
import suso.event_manage.util.SoundUtil;

public class PrimaticaEMPInstance implements TickableInstance {
    private static final int DURATION = 200;

    private final ServerWorld world;
    private final AbstractTeam team;
    private final Vec3d position;
    private final BlockPos pos;
    private final PrimaticaIngameHandler handler;

    private int ticksLeft;

    public PrimaticaEMPInstance(ServerPlayerEntity player, BlockPos pos, PrimaticaIngameHandler handler) {
        this.world = player.getWorld();
        this.team = player.getScoreboardTeam();
        this.position = new Vec3d(pos.getX(), pos.getY(), pos.getZ()).add(0.5, 0.5, 0.5);
        this.pos = pos;
        this.handler = handler;

        this.ticksLeft = DURATION;

        Identifier blockId = new Identifier(PrimaticaInfo.getCorrespondingEmp(team == null ? 7 :team.getColor().getColorIndex()));
        world.setBlockState(pos, Registry.BLOCK.get(blockId).getDefaultState());
        world.getPlayers().forEach(p -> ShaderUtil.setBlockColor(p, pos, (int)(player.world.getTime() % 24000)));

        SoundUtil.playSound(world.getPlayers(), new Identifier("minecraft:entity.bee.sting"), SoundCategory.BLOCKS, position, 2.0f, 0.5f);
        SoundUtil.playSound(world.getPlayers(), new Identifier("minecraft:block.beacon.activate"), SoundCategory.BLOCKS, position, 2.0f, 2.0f);
        SoundUtil.playSound(world.getPlayers(), new Identifier("minecraft:block.bell.resonate"), SoundCategory.BLOCKS, position, 2.0f, 2.0f);
        world.spawnParticles(ParticleTypes.FLASH, position.x, position.y, position.z, 1, 0.0, 0.0, 0.0, 0.0);
        Vec3d v = player.getEyePos();
        Vec3d d = position.subtract(v).normalize().multiply(0.2);
        for(int i = 0; i < 100; i++) {
            v = v.add(d);
            world.spawnParticles(new DustParticleEffect(ParticleUtil.teamColor(team), 1.0f), v.x, v.y, v.z, 1, 0.0, 0.0, 0.0, 0.05);
            if(v.distanceTo(position) < 0.3) break;
        }
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
            PrimaticaPlayerInfo info = handler.getPlayerInfo(player.getUuid());
            if(camDistance < 3.0) info.withinEMPNow = true;
            float volume = 1.0f - (float)(camDistance / 2.0 - 3.0);
            volume *= Math.min(1.0f, (float)ticksLeft / 20.0f);
            info.increaseHologramVolume(volume);
        }

        /*
        //Particle sphere
        for(float pitch = -90.0f; pitch < 91.0f; pitch += 30.0f) {
            for(float yaw = 0.0f; yaw < 360.0f; yaw += 30.0f) {
                Vec3d particlePos = position.add(Vec3d.fromPolar(pitch, yaw).multiply(3.0));
                world.spawnParticles(new DustParticleEffect(ParticleUtil.teamColor(team), 0.5f), particlePos.x, particlePos.y, particlePos.z, 1, 0.0, 0.0, 0.0, 0.0);
            }
        }
        */

        world.spawnParticles(new DustParticleEffect(ParticleUtil.teamColor(team), 2.0f), position.x, position.y, position.z, 1, 0.0, 0.0, 0.0, 0.0);

        return false;
    }

    @Override
    public void remove() {
        ticksLeft = 0;
        world.setBlockState(pos, Registry.BLOCK.get(new Identifier("minecraft:air")).getDefaultState());
        world.getPlayers().forEach(p -> ShaderUtil.unsetBlockColor(p, pos));
    }
}
