package suso.event_manage.util;

import net.minecraft.inventory.StackReference;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.s2c.play.ScreenHandlerSlotUpdateS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;

public class InventoryUtil {
    public static int getStupidSlot(int nonStupidSlot) {
        if(nonStupidSlot >= 0 && nonStupidSlot < 9) {
            return 36 + nonStupidSlot;
        }

        if(nonStupidSlot >= 100 && nonStupidSlot < 104) {
            return 8 - (nonStupidSlot - 100);
        }

        if(nonStupidSlot == 99) return 45;

        return nonStupidSlot;
    }

    public static void clearPLayer(ServerPlayerEntity player) {
        player.getInventory().remove(i -> true, -1, player.playerScreenHandler.getCraftingInput());
        player.currentScreenHandler.sendContentUpdates();
        player.playerScreenHandler.onContentChanged(player.getInventory());
    }

    public static void replaceSlot(ServerPlayerEntity player, int slot, ItemStack item) {
        StackReference stackReference = player.getStackReference(slot);
        if (stackReference != StackReference.EMPTY && stackReference.set(item.copy())) {
            player.networkHandler.sendPacket(new ScreenHandlerSlotUpdateS2CPacket(player.playerScreenHandler.syncId, player.playerScreenHandler.nextRevision(), getStupidSlot(slot), item));
        }
    }
}
