package suso.event_manage.state_handlers.primatica;

import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import suso.event_manage.state_handlers.TickableInstance;
import suso.event_manage.util.InventoryUtil;

import java.util.List;
import java.util.Queue;
import java.util.UUID;

public class PrimaticaPowerupInstance implements TickableInstance {
    private final PrimaticaInfo.Powerups type;
    private final ArmorStandEntity entity;
    private final World world;
    private final PrimaticaIngameHandler handler;

    public PrimaticaPowerupInstance(World world, Vec3d pos, float rotation, PrimaticaInfo.Powerups type, PrimaticaIngameHandler handler) {
        this.type = type;
        this.world = world;
        this.handler = handler;

        entity = new ArmorStandEntity(world, pos.x, pos.y, pos.z);
        entity.setNoGravity(true);
        entity.setYaw(rotation);
        entity.addScoreboardTag("primatica_powerup");
        entity.addScoreboardTag("volatile");
        world.spawnEntity(entity);

        handler.powerupAmount++;
    }

    @Override
    public boolean tick() {
        if(entity.isRemoved()) return true;

        PlayerEntity player = world.getClosestPlayer(entity, 20.0);
        if(player instanceof ServerPlayerEntity sPlayer) {
            if(player.getBoundingBox().intersects(entity.getBoundingBox())) {
                collectPowerup(sPlayer);
                return true;
            }
        }

        return false;
    }

    @Override
    public void remove() {
        entity.kill();
        handler.powerupAmount--;
    }

    private void collectPowerup(ServerPlayerEntity player) {
        UUID id = player.getUuid();
        if(!handler.getPlayerInfo(id).hasPowerup) {
            switch (type) {
                case AGILITY -> InventoryUtil.giveItem(player, ItemStack.fromNbt(PrimaticaInfo.AGILITY));
                case BRIDGE -> InventoryUtil.giveItem(player, ItemStack.fromNbt(PrimaticaInfo.BRIDGE));
                case GRAVITY -> InventoryUtil.giveItem(player, ItemStack.fromNbt(PrimaticaInfo.GRAVITY));
                default -> InventoryUtil.giveItem(player, ItemStack.fromNbt(PrimaticaInfo.EMP));
            }
            handler.setHasPowerup(id, true);
        }
    }
}
