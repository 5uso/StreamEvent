package suso.event_manage.state_handlers.primatica;

import it.unimi.dsi.fastutil.ints.IntList;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.component.type.FireworkExplosionComponent;
import net.minecraft.entity.projectile.thrown.SnowballEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtInt;
import net.minecraft.particle.DustParticleEffect;
import net.minecraft.particle.ItemStackParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.Registries;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.scoreboard.AbstractTeam;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import suso.event_manage.custom.blocks.entity.GunkBlockEntity;
import suso.event_manage.state_handlers.TickableInstance;
import suso.event_manage.util.MiscUtil;
import suso.event_manage.util.ParticleUtil;
import suso.event_manage.util.SoundUtil;

import java.util.List;
import java.util.Random;

public class PrimaticaGunkInstance implements TickableInstance {
    public static BlockState previous;
    public static int sendTicksLeft;

    private final ServerWorld world;
    private final SnowballEntity entity;
    private final BlockState block;
    private final ItemStackParticleEffect particle;
    private final AbstractTeam team;

    public PrimaticaGunkInstance(ServerPlayerEntity owner) {
        this.world = owner.getServerWorld();
        this.team = owner.getScoreboardTeam();

        AbstractTeam team = owner.getScoreboardTeam();
        Identifier blockId = Identifier.of(PrimaticaInfo.getCorrespondingGunk(team == null ? 7 :team.getColor().getColorIndex()));
        this.block = Registries.BLOCK.get(blockId).getDefaultState();

        Vec3d pos = owner.getEyePos().add(owner.getRotationVector());
        entity = new SnowballEntity(world, pos.x, pos.y, pos.z);
        entity.addCommandTag("primatica_gunk");
        entity.addCommandTag("volatile");
        entity.setVelocity(owner.getRotationVector().multiply(1.75));

        NbtCompound nbt = PrimaticaInfo.GUNK_DISPLAY.copy();
        int color = team == null ? 0 : team.getColor().getColorValue() == null ? 0 : team.getColor().getColorValue();
        nbt.getCompound("components").getCompound("dyed_color").put("rgb", NbtInt.of(color));
        entity.setItem(ItemStack.fromNbt(world.getRegistryManager(), nbt).orElse(ItemStack.EMPTY));

        world.spawnEntity(entity);

        NbtCompound bow_nbt = PrimaticaInfo.BOW.copy();
        int customModelData = team == null ? 7 : team.getColor().getColorIndex();
        bow_nbt.getCompound("tag").put("CustomModelData", NbtInt.of(customModelData));
        this.particle = new ItemStackParticleEffect(ParticleTypes.ITEM, ItemStack.fromNbt(world.getRegistryManager(), bow_nbt).orElse(ItemStack.EMPTY));

        SoundUtil.playSound(owner, Identifier.ofVanilla("item.glow_ink_sac.use"), SoundCategory.PLAYERS, owner.getPos(), 1.0f, 1.0f);
        SoundUtil.playSound(owner, Identifier.ofVanilla("item.glow_ink_sac.use"), SoundCategory.PLAYERS, owner.getPos(), 1.0f, 0.5f);
        SoundUtil.playSound(owner, Identifier.ofVanilla("item.dye.use"), SoundCategory.PLAYERS, owner.getPos(), 1.0f, 1.0f);
    }

    @Override
    public boolean tick() {
        if(!entity.isRemoved()) {
            world.spawnParticles(particle, entity.getX(), entity.getY() + 0.125, entity.getZ(), 3, 0.1, 0.1, 0.1, 0.15);
            return false;
        }

        BlockPos impactPos = entity.getBlockPos();
        TagKey<Block> safeTag = TagKey.of(Registries.BLOCK.getKey(), Identifier.of("suso", "gunk_safe"));

        Vec3d center = new Vec3d(impactPos.getX(), impactPos.getY(), impactPos.getZ()).add(0.5, 0.5, 0.5);
        for(double a = 0.0; a < 2.0 * Math.PI; a += 0.025 * Math.PI) {
            Vec3d temp = center;
            Vec3d direction = Vec3d.fromPolar(0.0F, (float) (a / (2.0 * Math.PI) * 360.0)).multiply(0.1);
            double b = (a + (new Random()).nextDouble() * 2.0 * Math.PI) % (2.0 * Math.PI);
            double shape = 1.0 + Math.sin(b * 2.0) * 4.0;
            for(double dist = 0.0; dist < shape; dist += 0.1) {
                temp = temp.add(direction);
                BlockPos gaming = searchY(MiscUtil.blockPosFrom3d(temp));
                if(gaming != null) placeGunk(gaming, 250 - (int)(dist * 10.0), safeTag);
            }
        }

        List<ServerPlayerEntity> players = world.getPlayers();
        SoundUtil.playSound(players, Identifier.ofVanilla("entity.player.splash.high_speed"), SoundCategory.PLAYERS, entity.getPos(), 2.0f, 2.0f);
        SoundUtil.playSound(players, Identifier.ofVanilla("item.trident.hit"), SoundCategory.PLAYERS, entity.getPos(), 2.0f, 0.5f);
        SoundUtil.playSound(players, Identifier.ofVanilla("entity.slime.squish"), SoundCategory.PLAYERS, entity.getPos(), 2.2f, 0.5f);
        world.spawnParticles(new DustParticleEffect(ParticleUtil.teamColor(team), 0.5f), entity.prevX, entity.prevY, entity.prevZ, 100, 1.5, 0.05, 1.5, 0.0);

        Integer color = team.getColor().getColorValue();
        ParticleUtil.fireworkParticle(players, entity.getX(), entity.getY(), entity.getZ(), 0.0, 0.0, 0.0, List.of(
                new FireworkExplosionComponent(
                        FireworkExplosionComponent.Type.STAR,
                        IntList.of(color == null ? 0 : color),
                        IntList.of(), false, false
                )
        ));

        return true;
    }

    @Override
    public void remove() {
        entity.kill();
    }

    private void placeGunk(BlockPos pos, int length, TagKey<Block> cantReplace) {
        sendTicksLeft = length;
        previous = world.getBlockState(pos);

        if(previous.hasBlockEntity() || previous.isIn(cantReplace)) {
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
