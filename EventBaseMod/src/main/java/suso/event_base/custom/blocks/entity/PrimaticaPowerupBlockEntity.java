package suso.event_base.custom.blocks.entity;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.Packet;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.core.AnimationState;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.builder.ILoopType;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.util.GeckoLibUtil;
import suso.event_base.custom.blocks.CustomBlocks;

public class PrimaticaPowerupBlockEntity extends BlockEntity implements IAnimatable {
    private final AnimationFactory factory = GeckoLibUtil.createFactory(this);

    private boolean spawned = false;
    private boolean collected = false;

    public PrimaticaPowerupBlockEntity(BlockPos pos, BlockState state) {
        super(CustomBlocks.PRIMATICA_POWERUP_ENTITY, pos, state);
    }

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController<>(this, "controller", 0, event -> {
            AnimationController<PrimaticaPowerupBlockEntity> controller = event.getController();

            if(!spawned) {
                spawned = true;
                controller.setAnimation(new AnimationBuilder().addAnimation("animation.primatica_powerup.spawn", ILoopType.EDefaultLoopTypes.HOLD_ON_LAST_FRAME));
                return PlayState.CONTINUE;
            }

            if(collected) {
                controller.transitionLengthTicks = 2;
                controller.setAnimation(new AnimationBuilder().addAnimation("animation.primatica_powerup.collect", ILoopType.EDefaultLoopTypes.HOLD_ON_LAST_FRAME));
                return PlayState.CONTINUE;
            }

            if(controller.getAnimationState().equals(AnimationState.Stopped)) {
                controller.setAnimation(new AnimationBuilder().addAnimation("animation.primatica_powerup.idle", ILoopType.EDefaultLoopTypes.LOOP));
                return PlayState.CONTINUE;
            }

            return PlayState.CONTINUE;
        }));
    }

    @Override
    public AnimationFactory getFactory() {
        return factory;
    }

    @Override
    public void writeNbt(NbtCompound nbt) {
        nbt.putBoolean("collected", collected);
        super.writeNbt(nbt);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        collected = nbt.getBoolean("collected");
    }

    @Nullable @Override
    public Packet<ClientPlayPacketListener> toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }

    @Override
    public NbtCompound toInitialChunkDataNbt() {
        return createNbt();
    }
}
