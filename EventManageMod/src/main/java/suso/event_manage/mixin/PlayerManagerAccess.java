package suso.event_manage.mixin;

import net.minecraft.server.OperatorList;
import net.minecraft.server.PlayerManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(PlayerManager.class)
public interface PlayerManagerAccess {
    @Accessor OperatorList getOps();
}
