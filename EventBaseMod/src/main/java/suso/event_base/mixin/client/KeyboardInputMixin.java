package suso.event_base.mixin.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.input.KeyboardInput;
import net.minecraft.client.option.GameOptions;
import net.minecraft.network.PacketByteBuf;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import suso.event_base.EvtBaseConstants;

@Mixin(KeyboardInput.class) @Environment(EnvType.CLIENT)
public class KeyboardInputMixin {
    @Shadow @Final private GameOptions settings;

    private boolean previousJumpInput = false;

    @Inject(
            method = "tick",
            at = @At("TAIL")
    )
    private void sendJumpInput(boolean slowDown, float f, CallbackInfo ci) {
        boolean jumpInput = this.settings.jumpKey.isPressed();
        if(jumpInput != previousJumpInput) {
            previousJumpInput = jumpInput;
            PacketByteBuf p = PacketByteBufs.create();
            p.writeBoolean(jumpInput);
            ClientPlayNetworking.send(EvtBaseConstants.JUMP_INPUT, p);
        }
    }
}
