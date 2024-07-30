package suso.event_manage.injected_interfaces;

import net.minecraft.entity.damage.DamageRecord;

public interface DamageTrackerExtended {
    default DamageRecord getMostRecentDamage() {
        return null;
    }
}
