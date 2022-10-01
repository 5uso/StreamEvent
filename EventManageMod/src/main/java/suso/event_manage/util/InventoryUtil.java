package suso.event_manage.util;

import net.minecraft.inventory.StackReference;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;

public class InventoryUtil {
    public static void clearPLayer(ServerPlayerEntity player) {
        player.getInventory().remove(i -> true, -1, player.playerScreenHandler.getCraftingInput());
        player.currentScreenHandler.sendContentUpdates();
        player.playerScreenHandler.onContentChanged(player.getInventory());
    }

    public static void replaceSlot(ServerPlayerEntity player, int slot, ItemStack item) {
        StackReference stackReference = player.getStackReference(slot);
        if (stackReference != StackReference.EMPTY && stackReference.set(item.copy())) {
            player.currentScreenHandler.sendContentUpdates();
        }
    }
}
