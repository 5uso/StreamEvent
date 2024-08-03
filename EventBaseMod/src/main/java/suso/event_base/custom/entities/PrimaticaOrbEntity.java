package suso.event_base.custom.entities;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import net.minecraft.server.network.EntityTrackerEntry;
import net.minecraft.util.Arm;
import net.minecraft.world.World;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.*;
import software.bernie.geckolib.util.Color;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.ArrayList;

public class PrimaticaOrbEntity extends LivingEntity implements GeoEntity {
    protected Color previousColor = Color.WHITE;
    protected boolean transitioningColor = false;
    protected long transitionStartMs = 0;

    private static final RawAnimation SPAWN_ANIM = RawAnimation.begin()
            .thenPlay("animation.primatica_orb.spawn")
            .thenLoop("animation.primatica_orb.idle");

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    public PrimaticaOrbEntity(EntityType<? extends PrimaticaOrbEntity> type, World world) {
        super(type, world);
        this.ignoreCameraFrustum = true;
        this.setSilent(true);
    }

    @Override
    protected void initDataTracker(DataTracker.Builder builder) {
        super.initDataTracker(builder);
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
    public Packet<ClientPlayPacketListener> createSpawnPacket(EntityTrackerEntry entityTrackerEntry) {
        return new EntitySpawnS2CPacket(this, entityTrackerEntry);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "controller", 0, event -> {
            AnimationController<PrimaticaOrbEntity> controller = event.getController();

            if(controller.getCurrentAnimation() == null) {
                controller.setAnimation(SPAWN_ANIM);
            }

            return PlayState.CONTINUE;
        }));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    @Override
    public void tick() {
        if(this.isDead() && !this.getWorld().isClient) {
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

    @Override
    public boolean shouldRender(double distance) {
        return true;
    }

    @Override
    public boolean shouldRender(double cameraX, double cameraY, double cameraZ) {
        return true;
    }
}
