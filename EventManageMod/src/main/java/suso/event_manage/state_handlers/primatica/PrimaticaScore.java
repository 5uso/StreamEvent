package suso.event_manage.state_handlers.primatica;

import net.minecraft.scoreboard.AbstractTeam;
import suso.event_manage.EvtBaseConstants;

public class PrimaticaScore {
    private final int[] scores = new int[12];
    private final int[] ranks = new int[12];
    private final int[] inv_ranks = new int[12];

    public PrimaticaScore() {
        for(int i = 0; i < 12; i++) ranks[i] = inv_ranks[i] = i;
    }

    public void score(AbstractTeam team, int amount) {
        int team_idx = EvtBaseConstants.teamIndexes.get(team.getColor().getColorIndex());
        int our_score = (scores[team_idx] += amount);

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
        return scores[EvtBaseConstants.teamIndexes.get(team.getColor().getColorIndex())];
    }

    public int[] getScores() {
        return scores;
    }

    public int[] getRanks() {
        return ranks;
    }
}
