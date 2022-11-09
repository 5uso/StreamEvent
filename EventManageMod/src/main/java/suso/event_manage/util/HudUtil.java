package suso.event_manage.util;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import suso.event_manage.EvtBaseConstants;

public class HudUtil {
    public enum DataTypes {
        STATE, TIMER, FEED
    }

    public static void setState(ServerPlayerEntity player, EvtBaseConstants.States state) {
        PacketByteBuf p = PacketByteBufs.create();
        p.writeInt(DataTypes.STATE.ordinal());
        p.writeInt(state.ordinal());

        ServerPlayNetworking.send(player, EvtBaseConstants.HUD_DATA, p);
    }
}
