package suso.event_manage.state_handlers.primatica;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.s2c.play.EntityVelocityUpdateS2CPacket;
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

        if(entity.getVelocity().length() > 0.0) {
            entity.setVelocity(Vec3d.ZERO);
            entity.setPosition(pos);
            world.getPlayers().forEach(p -> p.networkHandler.sendPacket(new EntityVelocityUpdateS2CPacket(entity)));
        }

        world.spawnParticles(new DustParticleEffect(new Vec3f(1.0f, 1.0f, 1.0f), 1.0f), entity.getX(), entity.getY() + 0.3, entity.getZ(), 1, 0.2, 0.2, 0.2, 0.0);

        PlayerEntity player = world.getClosestPlayer(entity, 20.0);
        MinecraftServer server = world.getServer();
        if(player instanceof ServerPlayerEntity sPlayer) {
            if(player.getScoreboardTeam() == null) return false;
            server.getScoreboard().addPlayerToTeam(entity.getUuidAsString(), (Team) player.getScoreboardTeam());

            if(player.getBoundingBox().intersects(entity.getBoundingBox().expand(0.1))) {
                collectOrb(server, sPlayer, entity);
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

    private void collectOrb(MinecraftServer server, ServerPlayerEntity player, PrimaticaOrbEntity orb) {
        server.getOverworld().spawnParticles(new ItemStackParticleEffect(ParticleTypes.ITEM, new ItemStack(Registry.ITEM.get(new Identifier("minecraft:snowball")))), orb.getX(), orb.getY() + 0.3, orb.getZ(), 100, 0.0, 0.0, 0.0, 1.0);

        SoundUtil.playSound(player, new Identifier("minecraft:entity.player.levelup"), SoundCategory.MASTER, orb.getPos(), 1.0f, 2.0f);
        SoundUtil.playSound(server.getPlayerManager().getPlayerList(), new Identifier("minecraft:item.totem.use"), SoundCategory.MASTER, orb.getPos(), 0.5f, 2.0f);

        AbstractTeam team = player.getScoreboardTeam();
        int score = handler.getTeamScore(team) + 1;
        handler.setTeamScore(team, score);
        System.out.println(team == null ? "None" : team.getName() + ": " + score);

        server.getOverworld().spawnParticles(new DustParticleEffect(ParticleUtil.teamColor(team), 2.0f), player.getX(), player.getY() + 0.3, player.getZ(), 20, 1.0, 1.0, 1.0, 2.0);
    }
}
