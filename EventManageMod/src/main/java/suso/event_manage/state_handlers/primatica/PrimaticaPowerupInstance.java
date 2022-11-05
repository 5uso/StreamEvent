package suso.event_manage.state_handlers.primatica;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.registry.Registry;
import suso.event_manage.custom.blocks.CustomBlocks;
import suso.event_manage.custom.blocks.entity.PrimaticaPowerupBlockEntity;
import suso.event_manage.state_handlers.TickableInstance;
import suso.event_manage.util.InventoryUtil;
import suso.event_manage.util.MiscUtil;
import suso.event_manage.util.SoundUtil;

import java.util.*;

public class PrimaticaPowerupInstance implements TickableInstance {
    public static final Set<Vec3d> positions = new HashSet<>();

    private final PrimaticaInfo.Powerups type;
    private final Vec3d position;
    private final BlockPos blockPos;
    private final ServerWorld world;
    private final PrimaticaIngameHandler handler;
    private int ticksLeft = -1;

    public PrimaticaPowerupInstance(ServerWorld world, BlockPos pos, PrimaticaInfo.Powerups type, PrimaticaIngameHandler handler) {
        this.type = type;
        this.world = world;
        this.handler = handler;
        this.blockPos = pos.up();
        this.position = Vec3d.ofCenter(pos.up());

        world.setBlockState(blockPos, CustomBlocks.PRIMATICA_POWERUP.getDefaultState());
        BlockEntity be = world.getBlockEntity(blockPos);
        if(be != null) {
            NbtCompound type_nbt = new NbtCompound();
            type_nbt.putByte("type", (byte) type.ordinal());
            MiscUtil.setBlockEntityNBT(be, type_nbt);
        }
        positions.add(position);

        SoundUtil.playSound(world.getPlayers(), new Identifier("minecraft:block.beehive.exit"), SoundCategory.PLAYERS, position, 1.0f, 2.0f);
    }

    @Override
    public boolean tick() {
        if(!(world.getBlockEntity(blockPos) instanceof PrimaticaPowerupBlockEntity)) return true;
        if(ticksLeft > 0) return --ticksLeft == 0;

        PlayerEntity player = world.getClosestPlayer(position.x, position.y, position.z, 20.0, false);
        if(player instanceof ServerPlayerEntity sPlayer) {
            if(MiscUtil.distance(player.getBoundingBox(), position) < 0.6) {
                collectPowerup(sPlayer);

                BlockEntity be = world.getBlockEntity(blockPos);
                if(be instanceof PrimaticaPowerupBlockEntity ppbe) {
                    NbtCompound collected = new NbtCompound();
                    ppbe.writeNbt(collected);
                    collected.putBoolean("collected", true);
                    MiscUtil.setBlockEntityNBT(ppbe, collected);
                }

                ticksLeft = 25;
            }
        }

        return false;
    }

    @Override
    public void remove() {
        world.setBlockState(blockPos, Blocks.AIR.getDefaultState());
        positions.remove(position);
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
            SoundUtil.playSound(player, new Identifier("eniah:sfx.collect"), SoundCategory.PLAYERS, player.getPos(), 1.0f, 1.8f);
        }

        List<ServerPlayerEntity> players = world.getPlayers();
        players.forEach(p -> SoundUtil.playSound(p, new Identifier("eniah:sfx.collect_fail"), SoundCategory.PLAYERS, position, p.equals(player) ? 1.0f : 0.5f, 1.0f));
        world.spawnParticles(ParticleTypes.ENCHANTED_HIT, position.x, position.y, position.z, 10, 0.1, 0.1, 0.1, 0.5);
    }

    private static final Random random = new Random();
    public static BlockPos getPowerupPosition(ServerWorld w) {
        for(int i = 0; i < 10; i++) {
            int x = random.nextInt(-75, 76);
            int z = random.nextInt(-75, 76);

            int sqr_dist = x*x + z*z;
            if(sqr_dist > 75*75) continue;

            int y = random.nextInt(66, 153);

            BlockPos r = new BlockPos(x, y, z);
            TagKey<Block> floor_tag = TagKey.of(Registry.BLOCK_KEY, new Identifier("suso:primatica_floor"));
            for(int j = 0; j < 10; j++) {
                if(!w.getBlockState(r).isAir()) break;
                if(w.getBlockState(r.down()).isIn(floor_tag)) {
                    final Vec3d vpos = Vec3d.ofCenter(r);
                    boolean occupied = positions.stream().anyMatch(pos -> {
                        Vec3d diff = pos.subtract(vpos);
                        return Math.abs(diff.y) < 3.0 && diff.horizontalLength() < 10.0;
                    });
                    if(!occupied) {
                        if(w.getBlockState(r.up()).isAir()) return r;
                        else continue;
                    }
                    else break;
                }
                r = r.down();
            }
        }

        return null;
    }
}
