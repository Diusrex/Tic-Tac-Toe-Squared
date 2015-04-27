package com.diusrex.tictactoe.ai;

import com.diusrex.tictactoe.data_structures.BoardStatus;
import com.diusrex.tictactoe.data_structures.Grid;
import com.diusrex.tictactoe.data_structures.Player;
import com.diusrex.tictactoe.data_structures.SectionPosition;
import com.diusrex.tictactoe.logic.GridLists;

public class Scorer {
    private static final PlayerScoreCalculator SectionImportantWrapper = new SectionIsImportantForPlayerScoreCalculator();
    private static final PlayerScoreCalculator SectionUnimportantWrapper = new SectionIsUnimportantForPlayerScoreCalculator();
    private final ScoringValues scoring;

    public Scorer(ScoringValues scoring) {
        this.scoring = scoring;
    }

    public int calculateScore(Player positivePlayer, BoardStatus board) {
        int score = calculateGridScoreWinningGridIsImportantForBothPlayers(positivePlayer, board.getMainGrid(),
                scoring.getMainScoring());

        for (SectionPosition section : GridLists.getAllStandardSections()) {
            score += calculateSectionScore(positivePlayer, board, section) * scoring.getSectionGridMultiplier(section);
        }

        return score;
    }

    private int calculateSectionScore(Player positivePlayer, BoardStatus board, SectionPosition section) {
        Player negativePlayer = positivePlayer.opposite();
        
        ScoringFunction scoringFunction = scoring.getSectionScoring();
        Grid sectionGrid = board.getSectionGrid(section);
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

    private int calculateGridScoreWinningGridIsImportantForBothPlayers(Player positivePlayer, Grid grid,
            ScoringFunction scoringFunction) {
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
