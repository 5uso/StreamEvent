package suso.event_manage.mixin;

import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import suso.event_manage.EventManager;

@Mixin(ServerPlayerEntity.class)
public abstract class ServerPlayerEntityMixin {
    @Shadow public abstract ServerWorld getWorld();

    @Inject(
            method = "dropItem",
            at = @At("HEAD"),
            cancellable = true
    )
    private void handleThrow(ItemStack stack, boolean throwRandomly, boolean retainOwnership, CallbackInfoReturnable<ItemEntity> cir) {
        if(EventManager.getInstance().canDropItems((ServerPlayerEntity)(Object) this)) return;

        ((PlayerEntity)(Object) this).getInventory().insertStack(stack);
        ((PlayerEntity)(Object) this).currentScreenHandler.sendContentUpdates();

        cir.setReturnValue(new ItemEntity(this.getWorld(), 0.0, 0.0, 0.0, ItemStack.EMPTY));
        cir.cancel();
    }
}
