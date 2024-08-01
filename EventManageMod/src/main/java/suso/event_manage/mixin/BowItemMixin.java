package suso.event_manage.mixin;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.BowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import suso.event_manage.EventManager;

@Mixin(BowItem.class)
public class BowItemMixin {
    @Inject(
            method = "onStoppedUsing",
            at = @At("HEAD"),
            cancellable = true
    )
    private void onShootBow(ItemStack stack, World world, LivingEntity user, int remainingUseTicks, CallbackInfo ci) {
        if(user instanceof ServerPlayerEntity player) {
            int useTicks = stack.getMaxUseTime(user) - remainingUseTicks;
            if(EventManager.getInstance().onPlayerShoot(player, stack, useTicks)) ci.cancel();
        }
    }

    @ModifyArg(
            method = "onStoppedUsing",
            at = @At(
                    value = "INVOKE",
                    target = "net/minecraft/item/BowItem.shootAll(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/util/Hand;Lnet/minecraft/item/ItemStack;Ljava/util/List;FFZLnet/minecraft/entity/LivingEntity;)V"
            ),
            index = 5
    )
    private float removeBowDivergence(float divergence) {
        return 0.0f;
    }
}
