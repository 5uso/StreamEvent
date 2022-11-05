package suso.event_manage.state_handlers.primatica;

import com.google.common.collect.ImmutableMap;
import net.minecraft.scoreboard.AbstractTeam;
import net.minecraft.util.Formatting;

import java.util.Map;

public class PrimaticaScore {
    private static final Map<Integer, Integer> indexes = ImmutableMap.<Integer, Integer>builder()
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

    private final int[] scores = new int[12];
    private final int[] ranks = new int[12];
    private final int[] inv_ranks = new int[12];

    public PrimaticaScore() {
        for(int i = 0; i < 12; i++) ranks[i] = inv_ranks[i] = i;
    }

    public void score(AbstractTeam team) {
        int team_idx = indexes.get(team.getColor().getColorIndex());
        int our_score = ++scores[team_idx];

        for(int curr_rank = ranks[team_idx] - 1; curr_rank >= 0; curr_rank--) {
            int team_to_beat = inv_ranks[curr_rank];
            int their_score = scores[team_to_beat];
            if(their_score >= our_score) break;

            inv_ranks[curr_rank] = team_idx;
            inv_ranks[curr_rank + 1] = team_to_beat;
            ranks[team_idx]--;
            ranks[team_to_beat]++;
        }
    }

    public int getScore(AbstractTeam team) {
        return scores[indexes.get(team.getColor().getColorIndex())];
    }

    public int[] getRanks() {
        return ranks;
    }
}
