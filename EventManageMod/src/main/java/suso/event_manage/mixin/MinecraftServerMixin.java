package suso.event_manage.mixin;

import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import suso.event_manage.EventManager;

@Mixin(MinecraftServer.class)
public class MinecraftServerMixin {
    @Inject(
            method = "save",
            at = @At("TAIL")
    )
    private void handleSave(boolean suppressLogs, boolean flush, boolean force, CallbackInfoReturnable<Boolean> cir) {
        EventManager.getInstance().onSave();
    }

    @Inject(
            method = "getServerModName",
            at = @At("HEAD"),
            cancellable = true,
            remap = false
    )
    private void replaceModName(CallbackInfoReturnable<String> cir) {
        cir.setReturnValue("§1§lStreamEvent§r");
        cir.cancel();
    }
}
