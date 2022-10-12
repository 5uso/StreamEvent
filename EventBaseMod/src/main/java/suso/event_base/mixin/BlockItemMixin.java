package suso.event_base.mixin;

import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(BlockItem.class)
public class BlockItemMixin {
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
