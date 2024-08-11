package suso.event_base.custom.blocks.entity;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.math.BlockPos;
import software.bernie.geckolib.animatable.GeoBlockEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.*;
import software.bernie.geckolib.util.GeckoLibUtil;
import suso.event_common.custom.blocks.entity.PrimaticaPowerupBlockEntity;

public class PrimaticaPowerupBlockEntityClient extends PrimaticaPowerupBlockEntity implements GeoBlockEntity {
    public static BlockEntityType<PrimaticaPowerupBlockEntityClient> TYPE;

    private static final RawAnimation SPAWN_ANIM = RawAnimation.begin()
            .thenPlay("animation.primatica_powerup.spawn")
            .thenLoop("animation.primatica_powerup.idle");
    private static final RawAnimation COLLECT_ANIM = RawAnimation.begin()
            .thenPlayAndHold("animation.primatica_powerup.collect");

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    public PrimaticaPowerupBlockEntityClient(BlockPos pos, BlockState state) {
        super(TYPE, pos, state);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "controller", 0, event -> {
            AnimationController<PrimaticaPowerupBlockEntityClient> controller = event.getController();

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
}
