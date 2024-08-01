package suso.event_base.mixin.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.item.HeldItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.CustomModelDataComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HeldItemRenderer.class) @Environment(EnvType.CLIENT)
public class HeldItemRendererMixin {
    private static ItemStack renderingStack;
    private static Hand renderingHand;
    private static AbstractClientPlayerEntity renderingPlayer;

    @Inject(
            method = "renderFirstPersonItem",
            at = @At("HEAD")
    )
    private void setRenderingStack(AbstractClientPlayerEntity player, float tickDelta, float pitch, Hand hand, float swingProgress, ItemStack item, float equipProgress, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, CallbackInfo ci) {
        renderingStack = item;
        renderingHand = hand;
        renderingPlayer = player;
    }

    @ModifyVariable(
            method = "renderFirstPersonItem",
            at = @At("STORE"),
            index = 19
    )
    private float modifyBowWobble(float value) {
        if(renderingStack.isOf(Items.BOW)
        && renderingPlayer.isUsingItem()
        && renderingPlayer.getItemUseTimeLeft() > 0
        && renderingPlayer.getActiveHand() == renderingHand
        && renderingStack.getOrDefault(DataComponentTypes.CUSTOM_MODEL_DATA, CustomModelDataComponent.DEFAULT).value() == 1) {
            return value * 10.0f;
        }

        return value;
    }
}
