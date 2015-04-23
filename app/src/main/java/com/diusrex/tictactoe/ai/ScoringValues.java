package com.diusrex.tictactoe.ai;

import com.diusrex.tictactoe.data_structures.SectionPosition;

public class ScoringValues {
    private final int mainGridMultiplier;
    private final ScoringFunction mainScoring;
    private final ScoringFunction sectionScoring;

    public ScoringValues(int mainGridMultiplier, ScoringFunction mainScoring, ScoringFunction sectionScoring) {
        this.mainGridMultiplier = mainGridMultiplier;
        this.mainScoring = mainScoring;
        this.sectionScoring = sectionScoring;
    }

    public ScoringFunction getMainScoring() {
        return mainScoring;
    }

    public ScoringFunction getSectionScoring() {
        return sectionScoring;
    }

    public int getMainGridMultiplier() {
        return mainGridMultiplier;
    }

    public int getSectionGridMultiplier(SectionPosition section) {
        return mainScoring.getMultiplier(section);
    }

}
