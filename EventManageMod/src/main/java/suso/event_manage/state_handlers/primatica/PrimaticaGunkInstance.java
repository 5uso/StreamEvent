package suso.event_manage.state_handlers.primatica;

import net.minecraft.block.BlockState;
import net.minecraft.entity.projectile.thrown.SnowballEntity;
import net.minecraft.scoreboard.AbstractTeam;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.registry.Registry;
import suso.event_manage.custom.blocks.GunkBlockEntity;
import suso.event_manage.state_handlers.TickableInstance;

import java.util.Random;

public class PrimaticaGunkInstance implements TickableInstance {
    public static BlockState previous;
    public static int sendTicksLeft;

    private final ServerWorld world;
    private final SnowballEntity entity;
    private final BlockState block;

    public PrimaticaGunkInstance(ServerPlayerEntity owner) {
        world = owner.getWorld();

        AbstractTeam team = owner.getScoreboardTeam();
        Identifier blockId = new Identifier(PrimaticaInfo.getCorrespondingGunk(team == null ? 7 :team.getColor().getColorIndex()));
        this.block = Registry.BLOCK.get(blockId).getDefaultState();

        Vec3d pos = owner.getEyePos().add(owner.getRotationVector());
        entity = new SnowballEntity(world, pos.x, pos.y, pos.z);
        entity.addScoreboardTag("primatica_gunk");
        entity.addScoreboardTag("volatile");
        entity.setVelocity(owner.getRotationVector().multiply(1.75));
        world.spawnEntity(entity);
    }

    @Override
    public boolean tick() {
        if(!entity.isRemoved()) return false;

        BlockPos impactPos = entity.getBlockPos();

        Vec3d center = new Vec3d(impactPos.getX(), impactPos.getY(), impactPos.getZ()).add(0.5, 0.5, 0.5);
        for(double a = 0.0; a < 2.0 * Math.PI; a += 0.025 * Math.PI) {
            Vec3d temp = center;
            Vec3d direction = Vec3d.fromPolar(0.0F, (float) (a / (2.0 * Math.PI) * 360.0)).multiply(0.1);
            double b = (a + (new Random()).nextDouble() * 2.0 * Math.PI) % (2.0 * Math.PI);
            double shape = 1.0 + Math.sin(b * 2.0) * 4.0;
            for(double dist = 0.0; dist < shape; dist += 0.1) {
                temp = temp.add(direction);
                BlockPos gaming = searchY(new BlockPos(temp));
                if(gaming != null) placeGunk(gaming, 250 - (int)(dist * 10.0));
            }
        }

        return true;
    }

    @Override
    public void remove() {
        entity.kill();
    }

    private void placeGunk(BlockPos pos, int length) {
        sendTicksLeft = length;
        previous = world.getBlockState(pos);

        if(previous.hasBlockEntity()) {
            if(world.getBlockEntity(pos) instanceof GunkBlockEntity be) {
                previous = be.getPrevious();
            } else return;
        }

        world.setBlockState(pos, block);
    }

    private BlockPos searchY(BlockPos pos) {
        for(int i = 0; i < 3 && world.getBlockState(pos).isAir(); i++) pos = pos.down();
        if(world.getBlockState(pos).isAir()) return null;

        BlockPos above = pos.up();
        for(int i = 0; i < 3 && !world.getBlockState(above).isAir(); i++) {
            pos = above;
            above = above.up();
        }
        if(!world.getBlockState(above).isAir()) return null;

        return pos;
    }
}
