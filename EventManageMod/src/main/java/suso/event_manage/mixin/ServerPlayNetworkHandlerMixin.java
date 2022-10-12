package suso.event_manage.mixin;

import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket.Action;
import net.minecraft.network.packet.c2s.play.PlayerInteractItemC2SPacket;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import suso.event_manage.EventManager;

@Mixin(ServerPlayNetworkHandler.class)
public abstract class ServerPlayNetworkHandlerMixin {
    @Shadow public abstract ServerPlayerEntity getPlayer();

    @Shadow public ServerPlayerEntity player;

    @Inject(
            method = "onPlayerAction",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/network/packet/c2s/play/PlayerActionC2SPacket;getAction()Lnet/minecraft/network/packet/c2s/play/PlayerActionC2SPacket$Action;"
            ),
            cancellable = true
    )
    private void interceptAction(PlayerActionC2SPacket packet, CallbackInfo ci) {
        if(EventManager.getInstance().canDropItems(this.getPlayer())) return;

        Action action = packet.getAction();
        if(action == Action.DROP_ITEM || action == Action.DROP_ALL_ITEMS) {
            player.currentScreenHandler.syncState();
            player.clearActiveItem();
            ci.cancel();
        }
    }

    @Inject(
            method = "onPlayerInteractItem",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/item/ItemStack;isEmpty()Z"
            ),
            cancellable = true
    )
    private void interceptRightClick(PlayerInteractItemC2SPacket packet, CallbackInfo ci) {
        Hand hand = packet.getHand();
        ServerPlayerEntity player = this.getPlayer();
        if(EventManager.getInstance().onPlayerRightClick(player, player.getStackInHand(hand), hand)) ci.cancel();
    }

    @Redirect(
            method = "tick",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/server/network/ServerPlayNetworkHandler;disconnect(Lnet/minecraft/text/Text;)V",
                    ordinal = 0
            )
    )
    private void dontKickFlying(ServerPlayNetworkHandler instance, Text reason) {
        //System.out.println("Didn't kick for flying");
    }

    @Redirect(
            method = "tick",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/server/network/ServerPlayNetworkHandler;disconnect(Lnet/minecraft/text/Text;)V",
                    ordinal = 1
            )
    )
    private void dontKickFlyingVehicle(ServerPlayNetworkHandler instance, Text reason) {
        //System.out.println("Didn't kick for flying vehicle");
    }

    @Redirect(
            method = "onPlayerMove",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/server/network/ServerPlayNetworkHandler;isHost()Z"
            )
    )
    private boolean dontMoveTooQuickly(ServerPlayNetworkHandler instance) {
        //System.out.println("Didn't snap player back");
        return true;
    }

    @Redirect(
            method = "onVehicleMove",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/server/network/ServerPlayNetworkHandler;isHost()Z"
            )
    )
    private boolean dontMoveVehicleTooQuickly(ServerPlayNetworkHandler instance) {
        //System.out.println("Didn't snap player vehicle back");
        return true;
    }
}
