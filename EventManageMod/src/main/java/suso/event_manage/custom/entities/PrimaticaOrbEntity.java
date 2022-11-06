package suso.event_manage.custom.entities;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.Packet;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class PrimaticaOrbEntity extends Entity {
    public PrimaticaOrbEntity(EntityType<?> type, World world) {
        super(type, world);
    }

    public PrimaticaOrbEntity(World world, Vec3d pos) {
        super(CustomEntities.PRIMATICA_ORB, world);
        this.setPosition(pos);
    }

    @Override
    protected void initDataTracker() {
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
    }

    @Override
    public Packet<?> createSpawnPacket() {
        return new EntitySpawnS2CPacket(this);
    }

    @Override
    public boolean shouldRender(double cameraX, double cameraY, double cameraZ) {
        return true;
    }

    @Override
    public boolean shouldRender(double distance) {
        return true;
    }
}
