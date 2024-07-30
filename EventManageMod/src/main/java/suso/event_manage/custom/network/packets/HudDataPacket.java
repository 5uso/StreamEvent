package suso.event_manage.custom.network.packets;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;
import suso.event_manage.EvtBaseConstants;
import suso.event_manage.state_handlers.primatica.PrimaticaScore;

import java.util.UUID;

public class HudDataPacket {
    public enum DataTypes {
        STATE, TIMER, FEED, AGILITY, PRIMATICA_SCORE, INFO, KILL
    }

    private final DataTypes type;
    private final PacketByteBuf buf;

    protected HudDataPacket(DataTypes type, PacketByteBuf buf) {
        this.type = type;
        this.buf = buf;
    }

    public HudDataPacket(PacketByteBuf buf) {
        this.type = DataTypes.values()[buf.readInt()];
        this.buf = PacketByteBufs.create();

        this.buf.writeBytes(buf);
    }

    public void write(PacketByteBuf buf) {
        buf.writeInt(type.ordinal());
        buf.writeBytes(this.buf);
    }
}
