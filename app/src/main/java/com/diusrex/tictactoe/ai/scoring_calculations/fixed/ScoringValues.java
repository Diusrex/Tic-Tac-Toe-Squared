package com.diusrex.tictactoe.ai.scoring_calculations.fixed;

import java.io.PrintStream;

import com.diusrex.tictactoe.data_structures.position.SectionPosition;

public class ScoringValues {
    private final ScoringFunction mainScoring;
    private final ScoringFunction sectionScoring;

    public ScoringValues(ScoringFunction mainScoring, ScoringFunction sectionScoring) {
        this.mainScoring = mainScoring;
        this.sectionScoring = sectionScoring;
    }

    public ScoringFunction getMainScoring() {
        return mainScoring;
    }

    public ScoringFunction getSectionScoring() {
        return sectionScoring;
    }

    public int getSectionGridMultiplier(SectionPosition section) {
        return mainScoring.getMultiplier(section);
    }

    public void saveState(PrintStream logger) {
        // TODO Auto-generated method stub
        
    }

}
