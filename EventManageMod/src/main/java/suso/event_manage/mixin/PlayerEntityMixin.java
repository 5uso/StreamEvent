package suso.event_manage.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import suso.event_manage.EventManager;

@Mixin(PlayerEntity.class)
public class PlayerEntityMixin {
    @Inject(
            method = "jump",
            at = @At("HEAD")
    )
    private void onJump(CallbackInfo ci) {
        if(((Entity)(Object) this).isRegionUnloaded() || !((Object) this instanceof ServerPlayerEntity)) return;

        BlockPos blockPos = ((PlayerEntity)(Object) this).getLandingPos();
        EventManager.getInstance().onPlayerJump((ServerPlayerEntity)(Object) this, blockPos);
    }
}
