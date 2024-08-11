package suso.event_base.custom.blocks.entity;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.math.BlockPos;
import software.bernie.geckolib.animatable.GeoBlockEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.*;
import software.bernie.geckolib.util.GeckoLibUtil;
import suso.event_common.custom.blocks.entity.PrimaticaDoorBlockEntity;

public class PrimaticaDoorBlockEntityClient extends PrimaticaDoorBlockEntity implements GeoBlockEntity {
    public static BlockEntityType<PrimaticaDoorBlockEntityClient> TYPE;

    private boolean view_open = false;

    private static final RawAnimation CLOSED_ANIM = RawAnimation.begin()
            .thenLoop("animation.primatica_door.closed");
    private static final RawAnimation OPEN_ANIM = RawAnimation.begin()
            .thenLoop("animation.primatica_door.open");
    private static final RawAnimation CLOSING_ANIM = RawAnimation.begin()
            .thenPlay("animation.primatica_door.closing")
            .thenLoop("animation.primatica_door.closed");
    private static final RawAnimation OPENING_ANIM = RawAnimation.begin()
            .thenPlay("animation.primatica_door.opening")
            .thenLoop("animation.primatica_door.open");

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    public PrimaticaDoorBlockEntityClient(BlockPos pos, BlockState state) {
        super(TYPE, pos, state);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "controller", 0, event -> {
            AnimationController<PrimaticaDoorBlockEntityClient> controller = event.getController();

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
