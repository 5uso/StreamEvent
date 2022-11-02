package suso.event_manage.mixin;

import net.minecraft.fluid.FluidState;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.world.GameMode;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import suso.event_manage.EventManager;

@Mixin(BlockItem.class)
public class BlockItemMixin {
    @Inject(
            method = "place(Lnet/minecraft/item/ItemPlacementContext;)Lnet/minecraft/util/ActionResult;",
            at = @At("HEAD"),
            cancellable = true
    )
    private void preventWaterReplace(ItemPlacementContext context, CallbackInfoReturnable<ActionResult> cir) {
        if(context.getPlayer() instanceof ServerPlayerEntity player) {
            GameMode mode = player.interactionManager.getGameMode();
            FluidState f = context.getWorld().getBlockState(context.getBlockPos()).getFluidState();

            if(mode == GameMode.ADVENTURE && !f.isEmpty() && !f.isStill()) {
                cir.setReturnValue(ActionResult.FAIL);
            }

            if(EventManager.getInstance().onPlayerPlacedBlock(player, context)) {
                cir.setReturnValue(ActionResult.FAIL);
            }
        }
    }

    @Redirect(
            method = "place(Lnet/minecraft/item/ItemPlacementContext;)Lnet/minecraft/util/ActionResult;",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/item/ItemStack;decrement(I)V"
            )
    )
    private void dontDecrementIfInfinite(ItemStack instance, int amount) {
        NbtCompound nbt = instance.getNbt();
        if(nbt != null && nbt.getBoolean("infinite")) return;

        instance.decrement(amount);
    }
}
