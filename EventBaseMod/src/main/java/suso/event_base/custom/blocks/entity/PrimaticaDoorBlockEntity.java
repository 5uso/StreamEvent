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
import software.bernie.geckolib.util.Color;
import software.bernie.geckolib.util.GeckoLibUtil;
import suso.event_base.custom.blocks.CustomBlocks;

public class PrimaticaDoorBlockEntity extends BlockEntity implements GeoBlockEntity {
    private boolean open = false;
    private Color color = Color.ofOpaque(0xFFFFFF);
    private boolean diagonal = false;

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    public PrimaticaDoorBlockEntity(BlockPos pos, BlockState state) {
        super(CustomBlocks.PRIMATICA_DOOR_ENTITY, pos, state);
    }

    public Color getColor() {
        return color;
    }

    public boolean isDiagonal() {
        return diagonal;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "controller", 0, event -> {
            AnimationController<PrimaticaDoorBlockEntity> controller = event.getController();

            if(controller.getCurrentAnimation() == null) {
                controller.setAnimation(RawAnimation.begin().then("animation.primatica_door.closed", Animation.LoopType.LOOP));
                return PlayState.CONTINUE;
            }

            if(controller.getAnimationState().equals(AnimationController.State.STOPPED)) {
                if(controller.getCurrentRawAnimation().getAnimationStages().getFirst().animationName().equals("animation.primatica_door.closing"))
                    controller.setAnimation(RawAnimation.begin().then("animation.primatica_door.closed", Animation.LoopType.LOOP));
                else if(controller.getCurrentRawAnimation().getAnimationStages().getFirst().animationName().equals("animation.primatica_door.opening"))
                    controller.setAnimation(RawAnimation.begin().then("animation.primatica_door.open", Animation.LoopType.LOOP));
                return PlayState.CONTINUE;
            }

            if(open && controller.getCurrentRawAnimation().getAnimationStages().getFirst().animationName().equals("animation.primatica_door.closed")) {
                controller.setAnimation(RawAnimation.begin().then("animation.primatica_door.opening", Animation.LoopType.HOLD_ON_LAST_FRAME));
                return PlayState.CONTINUE;
            }

            if(!open && controller.getCurrentRawAnimation().getAnimationStages().getFirst().animationName().equals("animation.primatica_door.open")) {
                controller.setAnimation(RawAnimation.begin().then("animation.primatica_door.closing", Animation.LoopType.HOLD_ON_LAST_FRAME));
                return PlayState.CONTINUE;
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
        nbt.putBoolean("open", open);
        nbt.putInt("color", color.getColor());
        nbt.putBoolean("diagonal", diagonal);
        super.writeNbt(nbt, wrapper);
    }

    @Override
    public void readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup wrapper) {
        super.readNbt(nbt, wrapper);
        open = nbt.getBoolean("open");
        color = new Color(nbt.getInt("color"));
        diagonal = nbt.getBoolean("diagonal");
    }

    @Nullable
    @Override
    public Packet<ClientPlayPacketListener> toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }

    @Override
    public NbtCompound toInitialChunkDataNbt(RegistryWrapper.WrapperLookup wrapper) {
        return createNbt(wrapper);
    }
}
