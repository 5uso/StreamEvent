package suso.event_manage.state_handlers.primatica;

import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
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

        BlockPos finalPos = findSuitablePos(pos);
        if(finalPos == null) {
            entity = null;
            return;
        }

        pos = new Vec3d(finalPos.getX() + 0.5, finalPos.getY(), finalPos.getZ() + 0.5);

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
                case GUNK -> InventoryUtil.giveItem(player, ItemStack.fromNbt(PrimaticaInfo.GUNK));
            }
            handler.setHasPowerup(id, true);
        }
    }

    private BlockPos findSuitablePos(Vec3d original) {
        BlockPos pos = new BlockPos(original);
        for(int i = 0; i < 3; i++) {
            if(isPosSuitable(pos)) return pos;

            Vec3d vecPos = new Vec3d(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5);
            for(float a = 0.0f; a < 359.0f; a += 30.0f) {
                BlockPos apos = new BlockPos(vecPos.add(Vec3d.fromPolar(0.0f, a).multiply(2.0)));
                if(isPosSuitable(apos)) return apos;
            }

            pos = pos.up();
        }

        return null;
    }

    private boolean isPosSuitable(BlockPos pos) {
        BlockPos below = pos.down();
        return !world.getBlockState(pos).isSolidBlock(world, pos) && world.getBlockState(below).isSolidBlock(world, below);
    }
}
