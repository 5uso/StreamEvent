package suso.event_manage.state_handlers.primatica;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.projectile.thrown.SnowballEntity;
import net.minecraft.network.packet.s2c.play.EntityVelocityUpdateS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import suso.event_manage.state_handlers.TickableInstance;

public class PrimaticaGravityInstance implements TickableInstance {
    private final SnowballEntity entity;
    private final ServerWorld world;

    private int time;

    public PrimaticaGravityInstance(ServerPlayerEntity owner) {
        world = owner.getWorld();

        Vec3d pos = owner.getEyePos();

        entity = new SnowballEntity(world, pos.x, pos.y, pos.z);
        entity.setNoGravity(true);
        entity.addScoreboardTag("primatica_gravity");
        entity.addScoreboardTag("volatile");
        entity.setVelocity(owner.getRotationVector().multiply(1.75));
        world.spawnEntity(entity);

        time = 0;
    }

    @Override
    public boolean tick() {
        if(entity.isRemoved() || ++time > 100) return true;

        if(time == 6) {
            entity.setVelocity(entity.getVelocity().multiply(0.15));
            entity.setGlowing(true);
        } else if(time > 6) {
            Vec3d pos = entity.getPos();
            for(ServerPlayerEntity player : world.getPlayers()) {
                if(player.isSpectator()) continue;

                Vec3d playerPos = player.getPos();
                double distance = playerPos.distanceTo(pos);
                if(distance < 10.0) {
                    Vec3d motion = player.getVelocity();
                    Vec3d gravityDirection = pos.subtract(playerPos).normalize();
                    double factor = distance / 5.0 + 0.5;
                    double magnitude = 1 / (factor * factor);

                    motion = motion.add(gravityDirection.multiply(magnitude));
                    player.setVelocity(motion);

                    player.networkHandler.sendPacket(new EntityVelocityUpdateS2CPacket(player));
                }
            }

            for(Entity e : world.getEntitiesByType(EntityType.CREEPER, e -> true)) {
                Vec3d playerPos = e.getPos();
                double distance = playerPos.distanceTo(pos);
                if(distance < 5.0) {
                    Vec3d motion = e.getVelocity();
                    Vec3d gravityDirection = pos.subtract(playerPos).normalize();
                    double factor = distance / 0.5 + 2.0;
                    double magnitude = 1 / (factor * factor);

                    motion = motion.add(gravityDirection.multiply(magnitude));
                    e.setVelocity(motion);
                }
            }
        }

        return false;
    }

    @Override
    public void remove() {
        entity.kill();
    }
}
