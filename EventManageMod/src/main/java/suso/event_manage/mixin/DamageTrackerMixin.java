package suso.event_manage.mixin;

import net.minecraft.entity.damage.DamageRecord;
import net.minecraft.entity.damage.DamageTracker;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import suso.event_manage.injected_interfaces.DamageTrackerExtended;

import java.util.List;

@Mixin(DamageTracker.class)
public abstract class DamageTrackerMixin implements DamageTrackerExtended {
    @Shadow @Final private List<DamageRecord> recentDamage;

    @Override
    public DamageRecord getMostRecentDamage() {
        return this.recentDamage.getLast();
    }
}
