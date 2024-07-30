package suso.event_manage.state_handlers.primatica;

import net.minecraft.block.BlockState;
import net.minecraft.particle.DustParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.Registries;
import net.minecraft.scoreboard.AbstractTeam;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import org.joml.Vector3f;
import suso.event_manage.state_handlers.TickableInstance;
import suso.event_manage.util.MiscUtil;
import suso.event_manage.util.ParticleUtil;
import suso.event_manage.util.SoundUtil;

import java.util.List;

public class PrimaticaBridgeInstance implements TickableInstance {
    private final Vec3d direction;
    private Vec3d position;

    private final ServerWorld world;
    private final BlockState block;
    private final Vector3f color;

    private int ticksLeft;

    public PrimaticaBridgeInstance(ServerPlayerEntity owner) {
        this.world = owner.getServerWorld();

        AbstractTeam team = owner.getScoreboardTeam();
        Identifier blockId = Identifier.of(PrimaticaInfo.getCorrespondingBlock(team == null ? 7 :team.getColor().getColorIndex()));
        this.block = Registries.BLOCK.get(blockId).getDefaultState();
        this.color = team == null ? new Vector3f().zero() : ParticleUtil.teamColor(team);

        this.direction = Vec3d.fromPolar(0.0f, owner.getYaw()).multiply(0.6);
        this.position = owner.getPos().add(0.0, Math.min(0.0, owner.getVelocity().y + 0.05) * 5.0 - 0.8, 0.0);
        this.position = new Vec3d(position.x, Math.floor(position.y) + 0.5, position.z);

        this.ticksLeft = 20;

        List<ServerPlayerEntity> players = world.getPlayers();
        world.spawnParticles(ParticleTypes.SCULK_CHARGE_POP, owner.getX(), owner.getY() + 1.0, owner.getZ(), 50, 0.3, 0.5, 0.3, 0.3);
        SoundUtil.playSound(players, Identifier.ofVanilla("block.grindstone.use"), SoundCategory.BLOCKS, position, 1.0f, 0.5f);
        SoundUtil.playSound(players, Identifier.ofVanilla("block.conduit.activate"), SoundCategory.BLOCKS, position, 1.0f, 0.5f);
        SoundUtil.playSound(players, Identifier.ofVanilla("item.trident.riptide_3"), SoundCategory.BLOCKS, position, 1.0f, 0.5f);
    }

    @Override
    public boolean tick() {
        if(--ticksLeft < 0) return true;

        BlockPos prevPos = MiscUtil.blockPosFrom3d(position);
        position = position.add(direction);
        BlockPos pos = MiscUtil.blockPosFrom3d(position);

        world.spawnParticles(ParticleTypes.DOLPHIN, position.x, position.y, position.z, 20, 1.0, 0.7, 1.0, 0.5);
        world.spawnParticles(ParticleTypes.SCULK_CHARGE_POP, position.x, position.y, position.z, 1, 0.5, 0.5, 0.5, 0.1);
        world.spawnParticles(new DustParticleEffect(color, 1.0f), position.x, position.y + 1.0, position.z, 1, 0.0, 0.0, 0.0, 0.0);
        if(prevPos.equals(pos)) return false;

        BlockPos change = pos.subtract(prevPos);
        if(change.getX() != 0 && change.getZ() != 0) {
            change = Math.abs(direction.x) > Math.abs(direction.z) ? new BlockPos(change.getX(), 0, 0) : new BlockPos(0, 0, change.getZ());
            BlockPos mid = prevPos.add(change);
            if(world.getBlockState(mid).isAir()) world.setBlockState(mid, block);
        }

        if(world.getBlockState(pos).isAir()) {
            world.setBlockState(pos, block);
            List<ServerPlayerEntity> players = world.getPlayers();
            SoundUtil.playSound(players, Identifier.ofVanilla("block.scaffolding.place"), SoundCategory.BLOCKS, position, 1.0f, 1.0f);
            SoundUtil.playSound(players, Identifier.ofVanilla("block.amethyst_block.place"), SoundCategory.BLOCKS, position, 1.0f, 1.0f);
        }

        return false;
    }

    @Override
    public void remove() {
        ticksLeft = 0;
    }

    public static boolean shouldSpawn(ServerPlayerEntity owner) {
        ServerWorld world = owner.getServerWorld();
        int count = 0;
        Vec3d direction = Vec3d.fromPolar(0.0f, owner.getYaw()).multiply(0.6);
        Vec3d position = owner.getPos().add(0.0, Math.min(0.0, owner.getVelocity().y + 0.05) * 5.0 - 0.8, 0.0);
        for(int i = 0; i < 7; i++) {
            if(world.getBlockState(MiscUtil.blockPosFrom3d(position)).isAir()) {
                if(++count > 1) return true;
            }
            position = position.add(direction);
        }

        return false;
    }
}
