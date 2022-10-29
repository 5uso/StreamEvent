package suso.event_base.mixin;

import net.minecraft.util.Formatting;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Formatting.class)
public class FormattingMixin {
    @Shadow @Final private int colorIndex;

    @Inject(
            method = "getColorValue",
            at = @At("RETURN"),
            cancellable = true
    )
    private void replaceColor(CallbackInfoReturnable<Integer> cir) {
        switch (colorIndex) {
            case  7 -> //Gray
                    cir.setReturnValue(0x444444);
            case 15 -> //White
                    cir.setReturnValue(0xf7f4ea);
            case 13 -> //Pink
                    cir.setReturnValue(0xe072a4);
            case  5 -> //Purple
                    cir.setReturnValue(0x6100a1);
            case  9 -> //Blue
                    cir.setReturnValue(0x3415ff);
            case  3 -> //Cyan
                    cir.setReturnValue(0x3bceac);
            case 11 -> //Light blue
                    cir.setReturnValue(0x197bbd);
            case  2 -> //Green
                    cir.setReturnValue(0x256d1b);
            case 10 -> //Lime
                    cir.setReturnValue(0x1efc1e);
            case 14 -> //Yellow
                    cir.setReturnValue(0xffb400);
            case  6 -> //Orange
                    cir.setReturnValue(0xfb5913);
            case  4 -> //Red
                    cir.setReturnValue(0xb0202a);
        }
    }
}
