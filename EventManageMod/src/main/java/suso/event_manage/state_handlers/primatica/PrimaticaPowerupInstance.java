package suso.event_manage.state_handlers.primatica;

import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import suso.event_manage.state_handlers.TickableInstance;
import suso.event_manage.util.InventoryUtil;
import suso.event_manage.util.SoundUtil;

import java.util.UUID;

public class PrimaticaPowerupInstance implements TickableInstance {
    private final PrimaticaInfo.Powerups type;
    private final ArmorStandEntity entity;
    private final World world;
    private final PrimaticaIngameHandler handler;

    public PrimaticaPowerupInstance(World world, BlockPos pos, float rotation, PrimaticaInfo.Powerups type, PrimaticaIngameHandler handler) {
        this.type = type;
        this.world = world;
        this.handler = handler;

        Vec3d vpos = new Vec3d(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5);

        entity = new ArmorStandEntity(world, vpos.x, vpos.y, vpos.z);
        entity.setNoGravity(true);
        entity.setYaw(rotation);
        entity.addScoreboardTag("primatica_powerup");
        entity.addScoreboardTag("volatile");
        world.spawnEntity(entity);

        handler.powerupAmount++;
    }

    @Override
    public boolean tick() {
        if(entity == null || entity.isRemoved()) return true;

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
        if(entity != null) {
            entity.kill();
            handler.powerupAmount--;
        }
    }

    private void collectPowerup(ServerPlayerEntity player) {
        UUID id = player.getUuid();
        if(!handler.getPlayerInfo(id).hasPowerup) {
            switch (type) {
                case AGILITY -> InventoryUtil.giveItem(player, ItemStack.fromNbt(PrimaticaInfo.AGILITY));
                case BRIDGE -> InventoryUtil.giveItem(player, ItemStack.fromNbt(PrimaticaInfo.BRIDGE));
                case GRAVITY -> InventoryUtil.giveItem(player, ItemStack.fromNbt(PrimaticaInfo.GRAVITY));
                case EMP -> InventoryUtil.giveItem(player, ItemStack.fromNbt(PrimaticaInfo.EMP));
                case ARROW -> {
                    Inventory playerInventory = player.getInventory();
                    int size = playerInventory.size();
                    int slot = 0;
                    for(int i = 0; i < size; i++) {
                        if(playerInventory.getStack(i).itemMatches(item -> item.matchesId(new Identifier("minecraft:bow"))))
                            slot = i == 40 ? 99 : i;
                    }
                    InventoryUtil.replaceSlot(player, slot, ItemStack.fromNbt(PrimaticaInfo.ARROW_BOW));
                }
                case GUNK -> InventoryUtil.giveItem(player, ItemStack.fromNbt(PrimaticaInfo.GUNK));
            }
            handler.setHasPowerup(id, true);
            SoundUtil.playSound(player, new Identifier("eniah:sfx.collect"), SoundCategory.PLAYERS, player.getPos(), 1.0f, 1.0f);
            return;
        }

        SoundUtil.playSound(player, new Identifier("eniah:sfx.collect_fail"), SoundCategory.PLAYERS, player.getPos(), 1.0f, 1.0f);
    }
}
