package suso.event_base.custom.entities;

import net.minecraft.entity.EntityType;
import net.minecraft.world.World;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.*;
import software.bernie.geckolib.util.Color;
import software.bernie.geckolib.util.GeckoLibUtil;
import suso.event_common.custom.entities.PrimaticaOrbEntity;

public class PrimaticaOrbEntityClient extends PrimaticaOrbEntity implements GeoEntity {
    public static EntityType<PrimaticaOrbEntityClient> TYPE;

    protected Color previousColor = Color.WHITE;
    protected boolean transitioningColor = false;
    protected long transitionStartMs = 0;

    private static final RawAnimation SPAWN_ANIM = RawAnimation.begin()
            .thenPlay("animation.primatica_orb.spawn")
            .thenLoop("animation.primatica_orb.idle");

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    public PrimaticaOrbEntityClient(EntityType<? extends PrimaticaOrbEntityClient> type, World world) {
        super(type, world);
        this.ignoreCameraFrustum = true;
        this.setSilent(true);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "controller", 0, event -> {
            AnimationController<PrimaticaOrbEntityClient> controller = event.getController();

            if(controller.getCurrentAnimation() == null) {
                controller.setAnimation(SPAWN_ANIM);
            }

            return PlayState.CONTINUE;
        }));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }
}
