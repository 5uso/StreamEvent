package suso.event_manage.mixin;

import net.minecraft.inventory.CraftingInventory;
import net.minecraft.recipe.RepairItemRecipe;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(RepairItemRecipe.class)
public class RepairItemRecipeMixin {
    @Inject(
            method = "matches(Lnet/minecraft/inventory/CraftingInventory;Lnet/minecraft/world/World;)Z",
            at = @At("HEAD"),
            cancellable = true
    )
    private void disableCraftingRepair(CraftingInventory craftingInventory, World world, CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(false);
    }
}
