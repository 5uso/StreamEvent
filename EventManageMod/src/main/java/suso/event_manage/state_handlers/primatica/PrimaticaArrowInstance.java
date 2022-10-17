package suso.event_manage.state_handlers.primatica;

import net.minecraft.network.packet.s2c.play.EntityVelocityUpdateS2CPacket;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.scoreboard.AbstractTeam;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.explosion.Explosion;
import suso.event_manage.state_handlers.TickableInstance;
import suso.event_manage.util.ParticleUtil;

import java.util.ArrayList;
import java.util.List;

public class PrimaticaArrowInstance implements TickableInstance {
    private final ServerPlayerEntity owner;
    private final ServerWorld world;
    private final AbstractTeam team;

    private final Vec3d direction;
    private Vec3d position;

    public PrimaticaArrowInstance(ServerPlayerEntity owner) {
        this.owner = owner;
        this.world = owner.getWorld();
        this.team = owner.getScoreboardTeam();

        this.direction = Vec3d.fromPolar(owner.getPitch(), owner.getYaw()).multiply(0.1);
        this.position = owner.getEyePos();
    }

    @Override
    public boolean tick() {
        List<Box> playerHitboxes = new ArrayList<>(world.getServer().getCurrentPlayerCount());
        world.getPlayers().forEach(player -> {
            if(!player.isTeamPlayer(team)) playerHitboxes.add(player.getBoundingBox());
        });

        for(int steps = 0; steps < 2000; steps++) {
            BlockPos pos = new BlockPos(position);
            if(!world.getBlockState(pos).getCollisionShape(world, pos).isEmpty()) {
                explode();
                return true;
            }

            for(Box b : playerHitboxes) {
                if(b.contains(position)) {
                    explode();
                    return true;
                }
            }

            float speed = Math.max(0.01f, 1.0f - (steps * 0.005f));
            ParticleUtil.forceParticle(ParticleTypes.END_ROD, position.getX(), position.getY(), position.getZ(), 1, 0.0, 0.0, 0.0, speed);
            position = position.add(direction);
        }
        return true;
    }

    @Override
    public void remove() {

    }

    public void explode() {
        Vec3d pos = position.subtract(direction.multiply(2.0));
        world.createExplosion(owner, null, new PrimaticaExplosionBehavior(), pos.x, pos.y, pos.z, 3.0f, false, Explosion.DestructionType.DESTROY);

        Vec3d diff = owner.getPos().subtract(position);
        if(diff.length() < 5.0) {
            Vec3d motion = diff.normalize().multiply(5.0 - diff.length());
            owner.addVelocity(motion.x, motion.y, motion.z);
            owner.networkHandler.sendPacket(new EntityVelocityUpdateS2CPacket(owner));
        }
    }
}
