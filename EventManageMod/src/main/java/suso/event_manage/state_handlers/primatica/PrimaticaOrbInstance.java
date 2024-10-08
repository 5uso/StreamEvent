package suso.event_manage.state_handlers.primatica;

import it.unimi.dsi.fastutil.ints.IntList;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.component.type.FireworkExplosionComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particle.DustParticleEffect;
import net.minecraft.particle.ItemStackParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.Registries;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.scoreboard.AbstractTeam;
import net.minecraft.scoreboard.Team;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import org.joml.Vector3f;
import suso.event_common.custom.entities.PrimaticaOrbEntity;
import suso.event_manage.state_handlers.TickableInstance;
import suso.event_manage.util.MiscUtil;
import suso.event_manage.util.ParticleUtil;
import suso.event_manage.util.SoundUtil;

import java.util.List;

public class PrimaticaOrbInstance implements TickableInstance {
    private final PrimaticaOrbEntity entity;
    private final Vec3d pos;
    private final PrimaticaIngameHandler handler;
    private final ServerWorld world;

    public PrimaticaOrbInstance(ServerWorld world, Vec3d pos, PrimaticaIngameHandler handler) {
        this.pos = pos;
        this.handler = handler;
        this.world = world;

        entity = new PrimaticaOrbEntity(world, pos);
        entity.setNoGravity(true);
        entity.setGlowing(true);
        entity.addCommandTag("primatica_objective");
        entity.addCommandTag("volatile");
        world.spawnEntity(entity);

        handler.orbLocations.add(pos);

        List<ServerPlayerEntity> players = world.getPlayers();
        clearSphere();
        SoundUtil.playSound(players, Identifier.ofVanilla("block.enchantment_table.use"), SoundCategory.NEUTRAL, pos, 2.0f, 0.5f);
        SoundUtil.playSound(players, Identifier.ofVanilla("entity.evoker.cast_spell"), SoundCategory.NEUTRAL, pos, 1.7f, 1.0f);
        world.spawnParticles(ParticleTypes.FLASH, pos.x, pos.y + 0.3, pos.z, 1, 0.0, 0.0, 0.0, 1.0);
    }

    @Override
    public boolean tick() {
        if(entity.isRemoved()) return true;

        world.spawnParticles(new DustParticleEffect(new Vector3f(1.0f, 1.0f, 1.0f), 1.0f), pos.x, pos.y + 0.3, pos.z, 1, 0.2, 0.2, 0.2, 0.0);

        PlayerEntity player = world.getClosestPlayer(entity, 20.0);
        MinecraftServer server = world.getServer();
        if(player instanceof ServerPlayerEntity sPlayer) {
            if(player.getScoreboardTeam() == null) return false;
            server.getScoreboard().addScoreHolderToTeam(entity.getUuidAsString(), (Team) player.getScoreboardTeam());

            if(player.getBoundingBox().intersects(entity.getBoundingBox().expand(0.1))) {
                collectOrb(sPlayer);
                return true;
            }
        } else {
            server.getScoreboard().clearTeam(entity.getUuidAsString());
        }

        return false;
    }

    @Override
    public void remove() {
        entity.kill();
        handler.orbLocations.remove(pos);
    }

    private void collectOrb(ServerPlayerEntity player) {
        AbstractTeam team = player.getScoreboardTeam();
        if(team == null) return;

        handler.score(team, player);

        List<ServerPlayerEntity> players = world.getPlayers();

        world.spawnParticles(new ItemStackParticleEffect(ParticleTypes.ITEM, new ItemStack(Items.GOLD_INGOT)), pos.x, pos.y + 0.3, pos.z, 100, 0.0, 0.0, 0.0, 1.0);
        world.spawnParticles(ParticleTypes.FLASH, pos.x, pos.y + 0.3, pos.z, 1, 0.0, 0.0, 0.0, 1.0);

        world.spawnParticles(new DustParticleEffect(ParticleUtil.teamColor(team), 2.0f), player.getX(), player.getY() + 0.3, player.getZ(), 15, 0.5, 0.5, 0.5, 2.0);
        world.spawnParticles(new ItemStackParticleEffect(ParticleTypes.ITEM, new ItemStack(Registries.ITEM.get(Identifier.of(PrimaticaInfo.getCorrespondingBlock(team.getColor().getColorIndex()))))), pos.x, pos.y + 0.3, pos.z, 70, 0.0, 0.0, 0.0, 0.6);

        Integer color = team.getColor().getColorValue();
        ParticleUtil.fireworkParticle(players, pos.x, pos.y + 0.3, pos.z, 0.0, 0.0, 0.0, List.of(
                new FireworkExplosionComponent(
                        FireworkExplosionComponent.Type.LARGE_BALL,
                        IntList.of(color == null ? 0 : color),
                        IntList.of(), false, false
                )
        ));

        SoundUtil.playSound(player, Identifier.ofVanilla("entity.player.levelup"), SoundCategory.PLAYERS, pos, 1.0f, 2.0f);
        SoundUtil.playSound(players, Identifier.ofVanilla("item.totem.use"), SoundCategory.NEUTRAL, pos, 0.5f, 2.0f);

        players.forEach(p -> {
            if(p.isSpectator() || p.isTeammate(player)) return;
            double distance = p.getPos().distanceTo(pos);
            if(distance < 10.0 && !handler.getPlayerInfo(p.getUuid()).withinEMPPrev) {
                distance /= 10.0;
                p.damage(world.getDamageSources().explosion(player, player), (float)(1.0 - distance) * 4.0f + 4.0f);
            }
        });
    }

    private void clearSphere() {
        TagKey<Block> breakable = TagKey.of(Registries.BLOCK.getKey(), Identifier.of("suso", "primatica_breakable"));
        for(float yaw = 0.0f; yaw < 359.0f; yaw += 10.0f) {
            for(float pitch = -90.0f; pitch < 91.0f; pitch += 15.0f) {
                for(float d = 0.4f; d < 6.5f; d += 0.5f) {
                    BlockPos t = MiscUtil.blockPosFrom3d(pos.add(Vec3d.fromPolar(pitch, yaw).multiply(d)));
                    if(world.getBlockState(t).isIn(breakable)) world.setBlockState(t, Blocks.AIR.getDefaultState());
                }
            }
        }
    }

    public void vanish() {
        SoundUtil.playSound(world.getPlayers(), Identifier.ofVanilla("block.fire.extinguish"), SoundCategory.NEUTRAL, pos, 3.0f, 2.0f);
        ParticleUtil.forceParticle(ParticleTypes.SMOKE, pos.x, pos.y + 0.3, pos.z, 50, 0.3, 0.3, 0.3, 0.01);
        remove();
    }
}
