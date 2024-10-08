package suso.event_manage.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import suso.event_manage.EventManager;
import suso.event_manage.injected_interfaces.ServerPlayerEntityExtended;

@Mixin(ServerPlayerEntity.class)
public abstract class ServerPlayerEntityMixin implements ServerPlayerEntityExtended {
    @Shadow public abstract ServerWorld getServerWorld();

    @Inject(
            method = "dropItem",
            at = @At("HEAD"),
            cancellable = true
    )
    private void handleThrow(ItemStack stack, boolean throwRandomly, boolean retainOwnership, CallbackInfoReturnable<ItemEntity> cir) {
        if(EventManager.getInstance().canDropItems((ServerPlayerEntity)(Object) this) || !retainOwnership) return;

        ((PlayerEntity)(Object) this).getInventory().insertStack(stack);
        ((PlayerEntity)(Object) this).currentScreenHandler.sendContentUpdates();

        cir.setReturnValue(new ItemEntity(this.getServerWorld(), 0.0, 0.0, 0.0, ItemStack.EMPTY));
        cir.cancel();
    }

    @Inject(
            method = "handleFall",
            at = @At("HEAD"),
            cancellable = true
    )
    private void handleLand(double xDifference, double heightDifference, double zDifference, boolean onGround, CallbackInfo ci) {
        if(((Entity)(Object) this).isRegionUnloaded() || !onGround || ((Entity)(Object) this).fallDistance <= 0.0) return;

        BlockPos blockPos = ((ServerPlayerEntity)(Object) this).getLandingPos();
        if(EventManager.getInstance().onPlayerLand((ServerPlayerEntity)(Object) this, xDifference, heightDifference, zDifference, blockPos)) ci.cancel();
    }

    @Inject(
            method = "updateKilledAdvancementCriterion",
            at = @At("TAIL")
    )
    private void onKill(Entity entityKilled, int score, DamageSource damageSource, CallbackInfo ci) {
        EventManager.getInstance().onPlayerKill((ServerPlayerEntity)(Object) this, entityKilled, damageSource);
    }

    @Inject(
            method = "tick",
            at = @At("RETURN")
    )
    private void updatePosDelta(CallbackInfo ci) {
        Vec3d newPos = ((Entity)(Object) this).getPos();
        boolean temp = currPos.equals(newPos);
        if(!temp || samePrevTick) {
            prevPos = currPos;
            currPos = newPos;
        }
        samePrevTick = temp;
    }
    private boolean samePrevTick = true;
    private Vec3d currPos = ((Entity)(Object) this).getPos();
    private Vec3d prevPos = currPos;
    @Override
    public Vec3d getPosDelta() {
        return currPos.subtract(prevPos);
    }

    private boolean isJumpPressed = false;
    @Override
    public void setJumpPressed(boolean isJumpPressed) {
        this.isJumpPressed = isJumpPressed;
    }
    @Override
    public boolean isJumpPressed() {
        return isJumpPressed;
    }
}
