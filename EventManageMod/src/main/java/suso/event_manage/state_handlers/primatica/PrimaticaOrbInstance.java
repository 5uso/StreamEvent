package suso.event_manage.state_handlers.primatica;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.StringNbtReader;
import net.minecraft.particle.DustParticleEffect;
import net.minecraft.particle.ItemStackParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.scoreboard.AbstractTeam;
import net.minecraft.scoreboard.Team;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3f;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import suso.event_manage.custom.entities.PrimaticaOrbEntity;
import suso.event_manage.state_handlers.TickableInstance;
import suso.event_manage.util.ParticleUtil;
import suso.event_manage.util.SoundUtil;

import java.util.List;

public class PrimaticaOrbInstance implements TickableInstance {
    private final PrimaticaOrbEntity entity;
    private final Vec3d pos;
    private final PrimaticaIngameHandler handler;
    private final ServerWorld world;

    public PrimaticaOrbInstance(World world, Vec3d pos, PrimaticaIngameHandler handler) {
        this.pos = pos;
        this.handler = handler;
        this.world = (ServerWorld) world;

        entity = new PrimaticaOrbEntity(world, pos);
        entity.setNoGravity(true);
        entity.setGlowing(true);
        entity.addScoreboardTag("primatica_objective");
        entity.addScoreboardTag("volatile");
        world.spawnEntity(entity);

        handler.orbLocations.add(pos);
    }

    @Override
    public boolean tick() {
        if(entity.isRemoved()) return true;

        world.spawnParticles(new DustParticleEffect(new Vec3f(1.0f, 1.0f, 1.0f), 1.0f), pos.x, pos.y + 0.3, pos.z, 1, 0.2, 0.2, 0.2, 0.0);

        PlayerEntity player = world.getClosestPlayer(entity, 20.0);
        MinecraftServer server = world.getServer();
        if(player instanceof ServerPlayerEntity sPlayer) {
            if(player.getScoreboardTeam() == null) return false;
            server.getScoreboard().addPlayerToTeam(entity.getUuidAsString(), (Team) player.getScoreboardTeam());

            if(player.getBoundingBox().intersects(entity.getBoundingBox().expand(0.1))) {
                collectOrb(server, sPlayer);
                return true;
            }
        } else {
            server.getScoreboard().clearPlayerTeam(entity.getUuidAsString());
        }

        return false;
    }

    @Override
    public void remove() {
        entity.kill();
        handler.orbLocations.remove(pos);
    }

    private void collectOrb(MinecraftServer server, ServerPlayerEntity player) {
        AbstractTeam team = player.getScoreboardTeam();
        if(team == null) return;

        int score = handler.getTeamScore(team) + 1;
        handler.setTeamScore(team, score);
        System.out.println(team.getName() + ": " + score);

        List<ServerPlayerEntity> players = world.getPlayers();

        server.getOverworld().spawnParticles(new ItemStackParticleEffect(ParticleTypes.ITEM, new ItemStack(Items.GOLD_INGOT)), pos.x, pos.y + 0.3, pos.z, 100, 0.0, 0.0, 0.0, 1.0);
        server.getOverworld().spawnParticles(ParticleTypes.FLASH, pos.x, pos.y + 0.3, pos.z, 1, 0.0, 0.0, 0.0, 1.0);

        server.getOverworld().spawnParticles(new DustParticleEffect(ParticleUtil.teamColor(team), 2.0f), player.getX(), player.getY() + 0.3, player.getZ(), 15, 0.5, 0.5, 0.5, 2.0);
        server.getOverworld().spawnParticles(new ItemStackParticleEffect(ParticleTypes.ITEM, new ItemStack(Registry.ITEM.get(new Identifier(PrimaticaInfo.getCorrespondingBlock(team.getColor().getColorIndex()))))), pos.x, pos.y + 0.3, pos.z, 70, 0.0, 0.0, 0.0, 0.6);

        try {
            NbtCompound firework = StringNbtReader.parse("{Explosions:[{Type:1,Colors:[I;" + team.getColor().getColorValue() + "]}]}");
            ParticleUtil.fireworkParticle(players, pos.x, pos.y + 0.3, pos.z, 0.0, 0.0, 0.0, firework);
        } catch (CommandSyntaxException | NullPointerException e) {
            e.printStackTrace();
        }

        SoundUtil.playSound(player, new Identifier("minecraft:entity.player.levelup"), SoundCategory.MASTER, pos, 1.0f, 2.0f);
        SoundUtil.playSound(players, new Identifier("minecraft:item.totem.use"), SoundCategory.MASTER, pos, 0.5f, 2.0f);

        players.forEach(p -> {
            if(p.isSpectator() || p.isTeammate(player)) return;
            double distance = p.getPos().distanceTo(pos);
            if(distance < 10.0 && !handler.getPlayerInfo(player.getUuid()).withinEMPPrev) {
                distance /= 10.0;
                player.damage(DamageSource.explosion(player), (float)(1.0 - distance) * 4.0f + 4.0f);
            }
        });
    }
}
