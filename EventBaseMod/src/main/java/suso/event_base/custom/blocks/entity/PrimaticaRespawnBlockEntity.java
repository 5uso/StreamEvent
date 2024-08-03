package suso.event_base.custom.blocks.entity;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoBlockEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.*;
import software.bernie.geckolib.util.Color;
import software.bernie.geckolib.util.GeckoLibUtil;
import suso.event_base.custom.blocks.CustomBlocks;

public class PrimaticaRespawnBlockEntity extends BlockEntity implements GeoBlockEntity {
    private boolean view_open = false;

    private boolean open = false;
    private Color color = Color.ofOpaque(0xFFFFFF);

    private static final RawAnimation CLOSED_ANIM = RawAnimation.begin()
            .thenLoop("animation.primatica_respawn.closed");
    private static final RawAnimation OPEN_ANIM = RawAnimation.begin()
            .thenLoop("animation.primatica_respawn.open");
    private static final RawAnimation CLOSING_ANIM = RawAnimation.begin()
            .thenPlay("animation.primatica_respawn.closing")
            .thenLoop("animation.primatica_respawn.closed");
    private static final RawAnimation OPENING_ANIM = RawAnimation.begin()
            .thenPlay("animation.primatica_respawn.opening")
            .thenLoop("animation.primatica_respawn.open");

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    public PrimaticaRespawnBlockEntity(BlockPos pos, BlockState state) {
        super(CustomBlocks.PRIMATICA_RESPAWN_ENTITY, pos, state);
    }

    public Color getColor() {
        return color;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "controller", 0, event -> {
            AnimationController<PrimaticaRespawnBlockEntity> controller = event.getController();

            if(controller.getCurrentAnimation() == null) {
                controller.setAnimation(open ? OPEN_ANIM : CLOSED_ANIM);
                view_open = open;
            }

            if(view_open != open) {
                controller.setAnimation(open ? OPENING_ANIM : CLOSING_ANIM);
                view_open = open;
            }

            return PlayState.CONTINUE;
        }));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    @Override
    public void writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup wrapper) {
        nbt.putBoolean("open", open);
        nbt.putInt("color", color.getColor());
        super.writeNbt(nbt, wrapper);
    }

    @Override
    public void readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup wrapper) {
        super.readNbt(nbt, wrapper);
        open = nbt.getBoolean("open");
        color = new Color(nbt.getInt("color"));
    }

    @Nullable @Override
    public Packet<ClientPlayPacketListener> toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }

    @Override
    public NbtCompound toInitialChunkDataNbt(RegistryWrapper.WrapperLookup wrapper) {
        return createNbt(wrapper);
    }
}
