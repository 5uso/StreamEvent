package suso.event_base.custom.blocks.entity;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
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
import software.bernie.geckolib3.core.util.Color;
import software.bernie.geckolib3.util.GeckoLibUtil;
import suso.event_base.custom.blocks.CustomBlocks;

public class PrimaticaRespawnBlockEntity extends BlockEntity implements IAnimatable {
    private boolean open = false;
    private Color color = Color.ofOpaque(0xFFFFFF);

    private final AnimationFactory factory = GeckoLibUtil.createFactory(this);

    public PrimaticaRespawnBlockEntity(BlockPos pos, BlockState state) {
        super(CustomBlocks.PRIMATICA_RESPAWN_ENTITY, pos, state);
    }

    public Color getColor() {
        return color;
    }

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController<>(this, "controller", 0, event -> {
            AnimationController<PrimaticaRespawnBlockEntity> controller = event.getController();

            if(controller.getCurrentAnimation() == null) {
                controller.setAnimation(new AnimationBuilder().addAnimation("animation.primatica_respawn.closed", ILoopType.EDefaultLoopTypes.LOOP));
                return PlayState.CONTINUE;
            }

            if(controller.getAnimationState().equals(AnimationState.Stopped)) {
                if(controller.getCurrentAnimation().animationName.equals("animation.primatica_respawn.closing"))
                    controller.setAnimation(new AnimationBuilder().addAnimation("animation.primatica_respawn.closed", ILoopType.EDefaultLoopTypes.LOOP));
                else if(controller.getCurrentAnimation().animationName.equals("animation.primatica_respawn.opening"))
                    controller.setAnimation(new AnimationBuilder().addAnimation("animation.primatica_respawn.open", ILoopType.EDefaultLoopTypes.LOOP));
                return PlayState.CONTINUE;
            }

            if(open && controller.getCurrentAnimation().animationName.equals("animation.primatica_respawn.closed")) {
                controller.setAnimation(new AnimationBuilder().addAnimation("animation.primatica_respawn.opening", ILoopType.EDefaultLoopTypes.HOLD_ON_LAST_FRAME));
                return PlayState.CONTINUE;
            }

            if(!open && controller.getCurrentAnimation().animationName.equals("animation.primatica_respawn.open")) {
                controller.setAnimation(new AnimationBuilder().addAnimation("animation.primatica_respawn.closing", ILoopType.EDefaultLoopTypes.HOLD_ON_LAST_FRAME));
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
        nbt.putBoolean("open", open);
        nbt.putInt("color", color.getColor());
        super.writeNbt(nbt);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        open = nbt.getBoolean("open");
        color = Color.ofTransparent(nbt.getInt("color"));
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
