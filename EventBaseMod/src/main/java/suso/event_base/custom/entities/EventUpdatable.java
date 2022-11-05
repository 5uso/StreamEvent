package suso.event_base.custom.entities;

import net.minecraft.nbt.NbtCompound;

public interface EventUpdatable {
    void customUpdate(NbtCompound nbt);
}
