package suso.event_manage.state_handlers.primatica;

import net.minecraft.block.BlockState;
import net.minecraft.scoreboard.AbstractTeam;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import suso.event_manage.state_handlers.TickableInstance;
import suso.event_manage.util.SoundUtil;

public class PrimaticaBridgeInstance implements TickableInstance {
    private final Vec3d direction;
    private Vec3d position;

    private final World world;
    private final BlockState block;

    private int ticksLeft;

    public PrimaticaBridgeInstance(ServerPlayerEntity owner) {
        this.world = owner.getWorld();

        AbstractTeam team = owner.getScoreboardTeam();
        Identifier blockId = new Identifier(PrimaticaInfo.getCorrespondingBlock(team == null ? 7 :team.getColor().getColorIndex()));
        this.block = Registry.BLOCK.get(blockId).getDefaultState();

        this.direction = Vec3d.fromPolar(0.0f, owner.getYaw()).multiply(0.6);
        this.position = owner.getPos().add(0.0, Math.min(0.0, owner.getVelocity().y + 0.05) * 5.0 - 0.8, 0.0);
        this.position = new Vec3d(position.x, Math.floor(position.y) + 0.5, position.z);

        this.ticksLeft = 20;
    }

    @Override
    public boolean tick() {
        if(--ticksLeft < 0) return true;

        BlockPos prevPos = new BlockPos(position);
        position = position.add(direction);
        BlockPos pos = new BlockPos(position);

        if(prevPos.equals(pos)) return false;

        BlockPos change = pos.subtract(prevPos);
        if(change.getX() != 0 && change.getZ() != 0) {
            change = Math.abs(direction.x) > Math.abs(direction.z) ? new BlockPos(change.getX(), 0, 0) : new BlockPos(0, 0, change.getZ());
            BlockPos mid = prevPos.add(change);
            if(world.getBlockState(mid).isAir()) world.setBlockState(mid, block);
        }

        if(world.getBlockState(pos).isAir()) {
            world.setBlockState(pos, block);
            SoundUtil.playSound(((ServerWorld) world).getPlayers(), new Identifier("minecraft:block.scaffolding.place"), SoundCategory.MASTER, position, 1.0f, 1.0f);
        }

        return false;
    }

    @Override
    public void remove() {
        ticksLeft = 0;
    }

    public static boolean shouldSpawn(ServerPlayerEntity owner) {
        ServerWorld world = owner.getWorld();
        int count = 0;
        Vec3d direction = Vec3d.fromPolar(0.0f, owner.getYaw()).multiply(0.6);
        Vec3d position = owner.getPos().add(0.0, Math.min(0.0, owner.getVelocity().y + 0.05) * 5.0 - 0.8, 0.0);
        for(int i = 0; i < 7; i++) {
            if(world.getBlockState(new BlockPos(position)).isAir()) {
                if(++count > 1) return true;
            }
            position = position.add(direction);
        }

        return false;
    }
}
