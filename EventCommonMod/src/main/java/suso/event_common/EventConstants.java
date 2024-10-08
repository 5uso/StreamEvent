package suso.event_common;

import com.google.common.collect.ImmutableMap;
import net.minecraft.util.Formatting;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public class EventConstants {
    public static final boolean DEBUG = false;

    public enum States {
        IDLE, PRIMATICA_INGAME
    }

    public static final Map<Integer, Integer> teamIndexes = ImmutableMap.<Integer, Integer>builder()
            .put(Formatting.GRAY.getColorIndex(), 0)
            .put(Formatting.WHITE.getColorIndex(), 1)
            .put(Formatting.LIGHT_PURPLE.getColorIndex(), 2)
            .put(Formatting.DARK_PURPLE.getColorIndex(), 3)
            .put(Formatting.BLUE.getColorIndex(), 4)
            .put(Formatting.DARK_AQUA.getColorIndex(), 5)
            .put(Formatting.AQUA.getColorIndex(), 6)
            .put(Formatting.DARK_GREEN.getColorIndex(), 7)
            .put(Formatting.GREEN.getColorIndex(), 8)
            .put(Formatting.YELLOW.getColorIndex(), 9)
            .put(Formatting.GOLD.getColorIndex(), 10)
            .put(Formatting.DARK_RED.getColorIndex(), 11)
            .build();

    public static final List<Formatting> formattingCorrespondence = List.of(Formatting.GRAY, Formatting.WHITE, Formatting.LIGHT_PURPLE, Formatting.DARK_PURPLE, Formatting.BLUE, Formatting.DARK_AQUA,Formatting.AQUA, Formatting.DARK_GREEN, Formatting.GREEN, Formatting.YELLOW, Formatting.GOLD, Formatting.DARK_RED);
    public static final List<String> colorCorrespondence = List.of("gray", "white", "pink", "purple", "blue", "cyan", "light_blue", "green", "lime", "yellow", "orange", "red");

    public static String getTeamColor(int colorIndex) {
        return colorCorrespondence.get(teamIndexes.get(colorIndex));
    }

    public static final UUID NULL_UUID = UUID.fromString("00000000-0000-0000-0000-000000000000");
}
