package suso.event_manage.custom.entities;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import net.minecraft.server.network.EntityTrackerEntry;
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
    protected void initDataTracker(DataTracker.Builder builder) {
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
    }

    @Override
    public Packet<ClientPlayPacketListener> createSpawnPacket(EntityTrackerEntry entityTrackerEntry) {
        return new EntitySpawnS2CPacket(this, entityTrackerEntry);
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
