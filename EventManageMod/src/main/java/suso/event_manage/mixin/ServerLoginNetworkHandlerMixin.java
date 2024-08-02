package suso.event_manage.mixin;

import net.minecraft.network.packet.c2s.login.LoginQueryResponseC2SPacket;
import net.minecraft.server.network.ServerLoginNetworkHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import suso.event_manage.ModCheck;

@Mixin(ServerLoginNetworkHandler.class)
public class ServerLoginNetworkHandlerMixin {
    @Inject(
            method = "onQueryResponse",
            at = @At("HEAD"),
            cancellable = true
    )
    public void handleQueryResponse(LoginQueryResponseC2SPacket packet, CallbackInfo ci) {
        ModCheck.handleResponse((ServerLoginNetworkHandler)(Object) this, packet);
        ci.cancel();
    }
}
