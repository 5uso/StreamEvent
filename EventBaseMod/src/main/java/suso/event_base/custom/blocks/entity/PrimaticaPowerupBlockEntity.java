package suso.event_base.custom.blocks.entity;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoBlockEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.*;
import software.bernie.geckolib.util.GeckoLibUtil;
import suso.event_base.custom.blocks.CustomBlocks;

public class PrimaticaPowerupBlockEntity extends BlockEntity implements GeoBlockEntity {
    public enum Powerups {
        AGILITY, BRIDGE, GRAVITY, EMP, ARROW, GUNK
    }

    private boolean collected = false;

    public Powerups type = Powerups.AGILITY;

    private static final RawAnimation SPAWN_ANIM = RawAnimation.begin()
            .thenPlay("animation.primatica_powerup.spawn")
            .thenLoop("animation.primatica_powerup.idle");
    private static final RawAnimation COLLECT_ANIM = RawAnimation.begin()
            .thenPlayAndHold("animation.primatica_powerup.collect");

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    public PrimaticaPowerupBlockEntity(BlockPos pos, BlockState state) {
        super(CustomBlocks.PRIMATICA_POWERUP_ENTITY, pos, state);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "controller", 0, event -> {
            AnimationController<PrimaticaPowerupBlockEntity> controller = event.getController();

            if(controller.getCurrentAnimation() == null) {
                controller.setAnimation(SPAWN_ANIM);
            }

            if(collected) {
                controller = controller.transitionLength(2);
                controller.setAnimation(COLLECT_ANIM);
            }

            return PlayState.CONTINUE;
        }));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    @Override
    public void writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup wrapper) {
        nbt.putBoolean("collected", collected);
        nbt.putByte("type", (byte) type.ordinal());
        super.writeNbt(nbt, wrapper);
    }

    @Override
    public void readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup wrapper) {
        super.readNbt(nbt, wrapper);
        collected = nbt.getBoolean("collected");
        type = Powerups.values()[nbt.getByte("type")];
    }

    @Nullable @Override
    public Packet<ClientPlayPacketListener> toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }

    @Override
    public NbtCompound toInitialChunkDataNbt(RegistryWrapper.WrapperLookup wrapper) {
        return createNbt(wrapper);
    }
}
