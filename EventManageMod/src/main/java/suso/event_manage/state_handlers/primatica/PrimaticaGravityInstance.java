package suso.event_manage.state_handlers.primatica;

import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.projectile.thrown.SnowballEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtInt;
import net.minecraft.network.packet.s2c.play.EntityVelocityUpdateS2CPacket;
import net.minecraft.particle.BlockStateParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import suso.event_manage.state_handlers.TickableInstance;
import suso.event_manage.util.MiscUtil;
import suso.event_manage.util.SoundUtil;

import java.util.List;

public class PrimaticaGravityInstance implements TickableInstance {
    private SnowballEntity entity;
    private final ServerWorld world;
    private final PrimaticaIngameHandler handler;
    private final ServerPlayerEntity owner;

    private int time;

    public PrimaticaGravityInstance(ServerPlayerEntity owner, PrimaticaIngameHandler handler) {
        this.world = owner.getWorld();
        this.handler = handler;
        this.owner = owner;

        Vec3d pos = owner.getEyePos();

        entity = new SnowballEntity(world, pos.x, pos.y - 0.125, pos.z);
        entity.setNoGravity(true);
        entity.addScoreboardTag("primatica_gravity");
        entity.addScoreboardTag("volatile");
        entity.setVelocity(owner.getRotationVector().multiply(1.75));
        entity.setItem(getItem(0, false));

        world.spawnEntity(entity);

        time = 0;

        List<ServerPlayerEntity> players = world.getPlayers();
        SoundUtil.playSound(players, new Identifier("minecraft:entity.firework_rocket.shoot"), SoundCategory.PLAYERS, pos, 2.0f, 0.5f);
    }

    @Override
    public boolean tick() {
        if(++time > 115) return true;
        if(entity.isRemoved() && respawnEntity()) return true;

        if(time < 6) {
            world.spawnParticles(ParticleTypes.ASH, entity.getX(), entity.getY(), entity.getZ(), 3, 0.01, 0.01, 0.01, 0.1);
        } else if(time == 6) {
            List<ServerPlayerEntity> players = world.getPlayers();
            entity.setVelocity(entity.getVelocity().multiply(0.15));

            SoundUtil.playSound(players, new Identifier("minecraft:entity.firework_rocket.large_blast"), SoundCategory.PLAYERS, entity.getPos(), 4.0f, 2.0f);
            SoundUtil.playSound(players, new Identifier("minecraft:entity.illusioner.prepare_mirror"), SoundCategory.PLAYERS, entity.getPos(), 4.0f, 2.0f);
            world.spawnParticles(ParticleTypes.CRIT, entity.getX(), entity.getY(), entity.getZ(), 15, 0.0, 0.0, 0.0, 2.0);
        } else if(time < 100) {
            Vec3d pos = entity.getPos();
            List<ServerPlayerEntity> players = world.getPlayers();
            if(time == 7) players.forEach(p -> p.networkHandler.sendPacket(new EntityVelocityUpdateS2CPacket(entity.getId(), entity.getVelocity())));
            for(ServerPlayerEntity player : players) {
                PrimaticaPlayerInfo info = handler.getPlayerInfo(player.getUuid());
                Vec3d playerPos = player.getPos();
                double distance = playerPos.distanceTo(pos);
                info.increaseGravityVolume(2.0f - (float)distance / 16.0f);
                if(player.isSpectator()) continue;

                if(player.getUuid().equals(owner.getUuid())) ownerPhysics(player, playerPos, pos, distance, info);
                else otherPhysics(player, playerPos, pos, distance, info);
            }
            if(time == 91) SoundUtil.playSound(players, new Identifier("eniah:sfx.riser"), SoundCategory.PLAYERS, entity.getPos(), 3.0f, 2.0f);
        } else if(time == 110) {
            List<ServerPlayerEntity> players = world.getPlayers();
            SoundUtil.playSound(players, new Identifier("minecraft:entity.firework_rocket.large_blast"), SoundCategory.PLAYERS, entity.getPos(), 4.0f, 2.0f);
            SoundUtil.playSound(players, new Identifier("minecraft:entity.illusioner.prepare_mirror"), SoundCategory.PLAYERS, entity.getPos(), 4.0f, 1.0f);
            SoundUtil.playSound(players, new Identifier("minecraft:entity.firework_rocket.twinkle"), SoundCategory.PLAYERS, entity.getPos(), 4.0f, 0.5f);

            Vec3d pos = entity.getPos();
            for(ServerPlayerEntity player : players) {
                if(player.isSpectator()) continue;
                Vec3d playerPos = player.getPos();
                double distance = playerPos.distanceTo(pos);
                if(distance < 10.0 && !handler.getPlayerInfo(player.getUuid()).withinEMPPrev) {
                    distance /= 10.0;
                    Vec3d motion = playerPos.subtract(pos).normalize().multiply((1.0 - Math.pow(distance, 4.0)) * 3.0);
                    player.addVelocity(motion.x, motion.y, motion.z);
                    player.networkHandler.sendPacket(new EntityVelocityUpdateS2CPacket(player));

                    player.damage(DamageSource.explosion(player), (float)(1.0 - distance) * 4.0f + 4.0f);
                }
            }
        }

        return false;
    }

    @Override
    public void remove() {
        entity.kill();
    }

    private void ownerPhysics(ServerPlayerEntity player, Vec3d playerPos, Vec3d pos, double distance, PrimaticaPlayerInfo info) {
        if(distance < 10.0 && !info.withinEMPPrev) {
            info.withinGravityNow = true;
            info.changePitch(0.95f + (float)distance / 200.0f, 0);

            Vec3d motion = player.getVelocity();
            Vec3d gravityDirection = pos.subtract(playerPos).normalize();
            if(motion.length() > 1.0 && MiscUtil.vec3Angle(motion, new Vec3d(0.0, -1.0, 0.0)) > 0.55 && MiscUtil.vec3Angle(motion, gravityDirection) > 1.6) return;

            double factor = distance / 3.5 + 0.35;
            double magnitude = 1 / (factor * factor);

            motion = motion.add(gravityDirection.multiply(magnitude));
            player.setVelocity(motion);

            player.networkHandler.sendPacket(new EntityVelocityUpdateS2CPacket(player));
        }
    }

    private void otherPhysics(ServerPlayerEntity player, Vec3d playerPos, Vec3d pos, double distance, PrimaticaPlayerInfo info) {
        if(distance < 10.0 && !info.withinEMPPrev) {
            info.withinGravityNow = true;
            info.changePitch(0.95f + (float)distance / 200.0f, 0);

            Vec3d motion = player.getVelocity();
            if(motion.length() > 1.5) return;

            Vec3d gravityDirection = pos.subtract(playerPos).normalize();
            double factor = distance / 2.5 + 0.5;
            double magnitude = 1 / (factor * factor);

            motion = motion.add(gravityDirection.multiply(magnitude));
            player.setVelocity(motion);

            player.networkHandler.sendPacket(new EntityVelocityUpdateS2CPacket(player));
        }
    }

    private boolean respawnEntity() {
        Vec3d prevMotion = entity.getVelocity();
        SnowballEntity tempEntity =  new SnowballEntity(world, entity.prevX, entity.prevY, entity.prevZ);

        world.spawnParticles(new BlockStateParticleEffect(ParticleTypes.BLOCK, world.getBlockState(entity.getBlockPos())), entity.prevX, entity.prevY, entity.prevZ, 15, 0.05, 0.05, 0.05, 0.2);
        SoundUtil.playSound(world.getPlayers(), new Identifier("minecraft:block.end_portal_frame.fill"), SoundCategory.PLAYERS, entity.getPos(), 2.0f, 2.0f);

        if(time < 7) {
            tempEntity.setItem(getItem(0, true));
            tempEntity.setVelocity(prevMotion.multiply(-0.1));
            world.spawnEntity(tempEntity);
            return true;
        }

        world.spawnParticles(ParticleTypes.FLASH, entity.prevX - prevMotion.x * 0.1, entity.prevY - prevMotion.y * 0.1, entity.prevZ - prevMotion.z * 0.1, 1, 0.0, 0.0, 0.0, 1.0);
        if(time < 90) time = 90;

        entity = tempEntity;
        entity.setNoGravity(true);
        entity.addScoreboardTag("primatica_gravity");
        entity.addScoreboardTag("volatile");
        entity.setItem(getItem(-time, false));
        world.spawnEntity(entity);
        return false;
    }

    private ItemStack getItem(int offset, boolean inactive) {
        NbtCompound nbt = PrimaticaInfo.GRAVITY_DISPLAY.copy();
        int color = (int)((world.getTime() + offset) % 24000) | (owner.getScoreboardTeam() == null ? 7 : owner.getScoreboardTeam().getColor().getColorIndex()) << 16;
        if(inactive) color |= 0x800000;
        nbt.getCompound("tag").getCompound("display").put("color", NbtInt.of(color));
        return ItemStack.fromNbt(nbt);
    }
}
