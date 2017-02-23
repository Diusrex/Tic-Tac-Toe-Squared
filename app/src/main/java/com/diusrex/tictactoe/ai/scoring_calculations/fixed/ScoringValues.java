package com.diusrex.tictactoe.ai.scoring_calculations.fixed;

import java.io.PrintStream;

import com.diusrex.tictactoe.data_structures.position.SectionPosition;

// A convenience wrapper to store the two ScoringFunctions - one for mainGrid, and other for section storing
public class ScoringValues {
    private final GridScoringFunction mainGridScoring;
    private final GridScoringFunction sectionScoring;

    public ScoringValues(GridScoringFunction mainScoring, GridScoringFunction sectionScoring) {
        this.mainGridScoring = mainScoring;
        this.sectionScoring = sectionScoring;
    }

    public GridScoringFunction getMainScoring() {
        return mainGridScoring;
    }

    public GridScoringFunction getSectionScoring() {
        return sectionScoring;
    }

    public int getSectionGridMultiplier(SectionPosition section) {
        return mainGridScoring.getMultiplier(section);
    }

    // Don't bother to save any state, since it hasn't changed at all.
    public void saveState(PrintStream logger) {
    }

}
