package suso.event_common.custom.network.payloads;


import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;
import suso.event_common.EventConstants;
import suso.event_common.custom.network.packets.HudDataPacket;

import java.util.UUID;

public class HudDataPayload extends HudDataPacket implements CustomPayload {
    public static final Id<HudDataPayload> ID = new Id<>(Identifier.of("suso", "hud_data"));
    public static final PacketCodec<PacketByteBuf, HudDataPayload> CODEC = CustomPayload.codecOf(HudDataPayload::write, HudDataPayload::new);

    public HudDataPayload(HudDataPacket.DataTypes type, PacketByteBuf buf) {
        super(type, buf);
    }

    public HudDataPayload(final PacketByteBuf buf) {
        super(buf);
    }

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }

    public static HudDataPayload ofState(EventConstants.States state) {
        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeInt(state.ordinal());

        return new HudDataPayload(HudDataPacket.DataTypes.STATE, buf);
    }

    public static HudDataPayload ofTimer(long time) {
        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeLong(time);

        return new HudDataPayload(HudDataPacket.DataTypes.TIMER, buf);
    }

    public static HudDataPayload ofFeed(UUID player1, Identifier texture, @Nullable UUID player2) {
        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeUuid(player1);
        buf.writeIdentifier(texture);
        buf.writeUuid(player2 == null ? EventConstants.NULL_UUID : player2);

        return new HudDataPayload(HudDataPacket.DataTypes.FEED, buf);
    }

    public static HudDataPayload ofAgility(boolean active) {
        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeBoolean(active);

        return new HudDataPayload(HudDataPacket.DataTypes.AGILITY, buf);
    }

    public static HudDataPayload ofPrimaticaScore(int[] scores, int[] ranks) {
        PacketByteBuf buf = PacketByteBufs.create();

        for(int i = 0; i < 12; i++) buf.writeInt(scores[i]);
        for(int i = 0; i < 12; i++) buf.writeInt(ranks[i]);

        return new HudDataPayload(HudDataPacket.DataTypes.PRIMATICA_SCORE, buf);
    }

    public static HudDataPayload ofInfo(Identifier id) {
        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeBoolean(id != null);
        if(id != null) buf.writeIdentifier(id);

        return new HudDataPayload(HudDataPacket.DataTypes.INFO, buf);
    }

    public static HudDataPayload ofKill(ServerPlayerEntity victim) {
        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeString(victim.getNameForScoreboard());
        buf.writeInt(victim.getScoreboardTeam() == null ? Formatting.WHITE.ordinal() : victim.getScoreboardTeam().getColor().ordinal());

        return new HudDataPayload(HudDataPacket.DataTypes.KILL, buf);
    }
}
