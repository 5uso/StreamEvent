package suso.event_manage.state_handlers.primatica;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.StringNbtReader;
import net.minecraft.network.packet.s2c.play.EntityVelocityUpdateS2CPacket;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.scoreboard.AbstractTeam;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.explosion.Explosion;
import suso.event_manage.state_handlers.TickableInstance;
import suso.event_manage.util.MiscUtil;
import suso.event_manage.util.ParticleUtil;
import suso.event_manage.util.SoundUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PrimaticaArrowInstance implements TickableInstance {
    private static final double STEP_SIZE = 0.1;

    private final ServerWorld world;
    private ServerPlayerEntity owner;
    private AbstractTeam team;

    private Vec3d direction;
    private Vec3d position;
    private Map.Entry<Vec3d, ServerPlayerEntity> closestEMP;

    public PrimaticaArrowInstance(ServerPlayerEntity owner) {
        this.owner = owner;
        this.world = owner.getServerWorld();
        this.team = owner.getScoreboardTeam();

        this.direction = Vec3d.fromPolar(owner.getPitch(), owner.getYaw()).multiply(STEP_SIZE);
        this.position = owner.getEyePos();

        List<ServerPlayerEntity> players = world.getPlayers();
        SoundUtil.playSound(players, new Identifier("minecraft:entity.arrow.hit"), SoundCategory.PLAYERS, position, 1.0f, 0.5f);
        SoundUtil.playSound(players, new Identifier("minecraft:entity.bee.sting"), SoundCategory.PLAYERS, position, 1.0f, 0.5f);
        SoundUtil.playSound(players, new Identifier("minecraft:entity.bee.sting"), SoundCategory.PLAYERS, position, 1.0f, 2.0f);
        SoundUtil.playSound(players, new Identifier("minecraft:block.beacon.deactivate"), SoundCategory.PLAYERS, position, 1.0f, 2.0f);
        SoundUtil.playSound(players, new Identifier("minecraft:item.trident.throw"), SoundCategory.PLAYERS, position, 1.0f, 2.0f);
        SoundUtil.playSound(players, new Identifier("suso:bow.swoosh"), SoundCategory.PLAYERS, position, 1.0f, 1.0f);

        MiscUtil.flashSky(0, 0);
    }

    @Override
    public boolean tick() {
        List<ServerPlayerEntity> players = world.getPlayers();
        List<Box> playerHitboxes = new ArrayList<>(players.size());
        players.forEach(player -> {
            if(!player.isTeamPlayer(team)) playerHitboxes.add(player.getBoundingBox());
        });

        double empDist = empDistance();

        for(int steps = 0; steps < 2000; steps++) {
            if(empDist < 3.1) {
                empDist = empDistance();
                if(empDist < 3.1) {
                    direction = position.subtract(closestEMP.getKey()).normalize().multiply(STEP_SIZE);
                    SoundUtil.playSound(players, new Identifier("minecraft:block.end_portal_frame.fill"), SoundCategory.BLOCKS, position, 3.0f, 0.7f);

                    if(closestEMP.getValue().getScoreboardTeam() != null) {
                        team = closestEMP.getValue().getScoreboardTeam();
                        owner = closestEMP.getValue();
                    }
                }
            }

            BlockPos pos = MiscUtil.blockPosFrom3d(position);
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
            empDist -= STEP_SIZE;
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

        try {
            NbtCompound firework = StringNbtReader.parse("{Explosions:[{Type:4,Trail:1b,Colors:[I;" + team.getColor().getColorValue() + "],FadeColors:[I;16777215]}]}");
            ParticleUtil.fireworkParticle(world.getPlayers(), pos.x, pos.y, pos.z, -direction.x, -direction.y, -direction.z, firework);
        } catch (CommandSyntaxException | NullPointerException e) {
            e.printStackTrace();
        }
    }

    private double empDistance() {
        double r = 500.0;
        for (Map.Entry<Vec3d, ServerPlayerEntity> p : PrimaticaEMPInstance.positions.entrySet()) {
            if(p.getValue().getScoreboardTeam() != null && p.getValue().getScoreboardTeam().equals(team)) continue;

            double d = p.getKey().distanceTo(position);
            if(d < r) {
                r = d;
                closestEMP = p;
            }
        }
        return r;
    }
}
