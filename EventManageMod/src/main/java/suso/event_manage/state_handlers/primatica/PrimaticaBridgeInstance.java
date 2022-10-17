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

        this.ticksLeft = 20;
    }

    @Override
    public boolean tick() {
        if(--ticksLeft < 0) return true;

        position = position.add(direction);
        BlockPos pos = new BlockPos(position);
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
}
