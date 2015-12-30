package com.diusrex.tictactoe.ai.scoring_calculations;


import com.diusrex.tictactoe.data_structures.*;
import com.diusrex.tictactoe.logic.GridLists;

public class Scorer {
    private static final PlayerScoreCalculator SectionImportantWrapper = new SectionIsImportantForPlayerScoreCalculator();
    private static final PlayerScoreCalculator SectionUnimportantWrapper = new SectionIsUnimportantForPlayerScoreCalculator();
    private final ScoringValues scoring;

    public Scorer(ScoringValues scoring) {
        this.scoring = scoring;
    }


    public int calculateScore(Player positivePlayer, BoardStatus board, MainGrid mainGrid) {
        int score = calculateGridScoreWinningGridIsImportantForBothPlayers(positivePlayer, mainGrid,
                scoring.getMainScoring());

        for (SectionPosition section : GridLists.getAllStandardSections()) {
            score += calculateSectionScore(positivePlayer, board, section, mainGrid.getGrid(section))
                    * scoring.getSectionGridMultiplier(section);
        }

        return score;
    }

    private int calculateSectionScore(Player positivePlayer, BoardStatus board, SectionPosition section, Grid sectionGrid) {
        Player negativePlayer = positivePlayer.opposite();

        ScoringFunction scoringFunction = scoring.getSectionScoring();
        if (!sectionGrid.canBeWon()) {
            return SectionUnimportantWrapper.calculateSetupScore(positivePlayer, sectionGrid, scoringFunction)
                    - SectionUnimportantWrapper.calculateSetupScore(negativePlayer, sectionGrid, scoringFunction);
        }

        int score = 0;
        if (board.sectionIsImportantToPlayer(section, positivePlayer)) {
            score += SectionImportantWrapper.calculateSetupScore(positivePlayer, sectionGrid, scoringFunction);
        } else {
            score += SectionUnimportantWrapper.calculateSetupScore(positivePlayer, sectionGrid, scoringFunction);
        }

        if (board.sectionIsImportantToPlayer(section, negativePlayer)) {
            score -= SectionImportantWrapper.calculateSetupScore(negativePlayer, sectionGrid, scoringFunction);
        } else {
            score -= SectionUnimportantWrapper.calculateSetupScore(negativePlayer, sectionGrid, scoringFunction);
        }

        return score;
    }

    private int calculateGridScoreWinningGridIsImportantForBothPlayers(Player positivePlayer, Grid grid, ScoringFunction scoringFunction) {
        Player negativePlayer = positivePlayer.opposite();
        if (!grid.canBeWon()) {
            return SectionUnimportantWrapper.calculateSetupScore(positivePlayer, grid, scoringFunction)
                    - SectionUnimportantWrapper.calculateSetupScore(negativePlayer, grid, scoringFunction);
        }

        return SectionImportantWrapper.calculateSetupScore(positivePlayer, grid, scoringFunction)
                - SectionImportantWrapper.calculateSetupScore(negativePlayer, grid, scoringFunction);
    }

    public int getTieScore() {
        return 0;
    }
}
