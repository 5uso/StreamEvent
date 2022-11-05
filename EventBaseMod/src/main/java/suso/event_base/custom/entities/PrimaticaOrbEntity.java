package suso.event_base.custom.entities;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.Packet;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import net.minecraft.util.Arm;
import net.minecraft.world.World;
import software.bernie.geckolib3.core.AnimationState;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.builder.ILoopType;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.util.GeckoLibUtil;

import java.util.ArrayList;

public class PrimaticaOrbEntity extends LivingEntity implements IAnimatable {
    private final AnimationFactory factory = GeckoLibUtil.createFactory(this);

    private boolean spawned = false;

    public PrimaticaOrbEntity(EntityType<? extends LivingEntity> type, World world) {
        super(type, world);
        this.ignoreCameraFrustum = true;
        this.setSilent(true);
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
    }

    @Override
    public Iterable<ItemStack> getArmorItems() {
        return new ArrayList<>(0);
    }

    @Override
    public ItemStack getEquippedStack(EquipmentSlot slot) {
        return ItemStack.EMPTY;
    }

    @Override
    public void equipStack(EquipmentSlot slot, ItemStack stack) {
    }

    @Override
    public Arm getMainArm() {
        return Arm.LEFT;
    }

    @Override
    public Packet<?> createSpawnPacket() {
        return new EntitySpawnS2CPacket(this);
    }

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController<>(this, "controller", 0, event -> {
            AnimationController<PrimaticaOrbEntity> controller = event.getController();

            if(!spawned) {
                spawned = true;
                controller.setAnimation(new AnimationBuilder().addAnimation("animation.primatica_orb.spawn", ILoopType.EDefaultLoopTypes.HOLD_ON_LAST_FRAME));
                return PlayState.CONTINUE;
            }

            if(controller.getAnimationState().equals(AnimationState.Stopped)) {
                controller.setAnimation(new AnimationBuilder().addAnimation("animation.primatica_orb.idle", ILoopType.EDefaultLoopTypes.LOOP));
                return PlayState.CONTINUE;
            }

            return PlayState.CONTINUE;
        }));
    }

    @Override
    public AnimationFactory getFactory() {
        return factory;
    }

    @Override
    public void tick() {
        if(this.isDead() && !this.world.isClient) {
            this.remove(RemovalReason.KILLED);
        }
    }

    @Override
    public boolean canHit() {
        return false;
    }

    @Override
    public void handleStatus(byte status) {
    }
}
