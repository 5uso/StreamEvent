package suso.event_manage.data;

import net.minecraft.server.network.ServerPlayerEntity;

import java.io.Serializable;

public class EventPlayerData implements Serializable {
    public boolean isPlayer;

    public void initialize(ServerPlayerEntity player) {
        EventData eventData = EventData.getInstance();
        isPlayer = eventData.isInPlayerList(player);
    }
}
