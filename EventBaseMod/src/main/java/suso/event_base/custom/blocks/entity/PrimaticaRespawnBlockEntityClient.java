package suso.event_base.custom.blocks.entity;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.math.BlockPos;
import software.bernie.geckolib.animatable.GeoBlockEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.*;
import software.bernie.geckolib.util.GeckoLibUtil;
import suso.event_common.custom.blocks.entity.PrimaticaRespawnBlockEntity;

public class PrimaticaRespawnBlockEntityClient extends PrimaticaRespawnBlockEntity implements GeoBlockEntity {
    public static BlockEntityType<PrimaticaRespawnBlockEntityClient> TYPE;

    private boolean view_open = false;

    private static final RawAnimation CLOSED_ANIM = RawAnimation.begin()
            .thenLoop("animation.primatica_respawn.closed");
    private static final RawAnimation OPEN_ANIM = RawAnimation.begin()
            .thenLoop("animation.primatica_respawn.open");
    private static final RawAnimation CLOSING_ANIM = RawAnimation.begin()
            .thenPlay("animation.primatica_respawn.closing")
            .thenLoop("animation.primatica_respawn.closed");
    private static final RawAnimation OPENING_ANIM = RawAnimation.begin()
            .thenPlay("animation.primatica_respawn.opening")
            .thenLoop("animation.primatica_respawn.open");

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    public PrimaticaRespawnBlockEntityClient(BlockPos pos, BlockState state) {
        super(TYPE, pos, state);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "controller", 0, event -> {
            AnimationController<PrimaticaRespawnBlockEntityClient> controller = event.getController();

            if(controller.getCurrentAnimation() == null) {
                controller.setAnimation(open ? OPEN_ANIM : CLOSED_ANIM);
                view_open = open;
            }

            if(view_open != open) {
                controller.setAnimation(open ? OPENING_ANIM : CLOSING_ANIM);
                view_open = open;
            }

            return PlayState.CONTINUE;
        }));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }
}
