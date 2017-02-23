package com.diusrex.tictactoe.ai.scoring_calculations.fixed;


import java.io.PrintStream;

import com.diusrex.tictactoe.ai.scoring_calculations.*;
import com.diusrex.tictactoe.data_structures.*;
import com.diusrex.tictactoe.data_structures.grid.Grid;
import com.diusrex.tictactoe.data_structures.position.SectionPosition;
import com.diusrex.tictactoe.logic.GridLists;

public class StaticScorer extends Scorer {
    public static final String IDENTIFIER = "StaticScorer";
    public static final double WIN_SCORE = 10000000;
    private static final PlayerGridScoreCalculator SectionImportantWrapper = new SectionIsImportantForPlayerScoreCalculator();
    private static final PlayerGridScoreCalculator SectionUnimportantWrapper = new SectionIsUnimportantForPlayerScoreCalculator();
    private final ScoringValues scoring;

    public StaticScorer(ScoringValues scoring) {
        this.scoring = scoring;
    }

    @Override
    public double calculateScore(Player positivePlayer, BoardStatus board) {
        int score = calculateGridScoreWinningGridIsImportantForBothPlayers(positivePlayer, board.getMainGrid(),
                scoring.getMainScoring());

        for (SectionPosition section : GridLists.getAllStandardSections()) {
            score += calculateSectionScore(positivePlayer, board, section, board.getSubGrid(section))
                    * scoring.getSectionGridMultiplier(section);
        }

        return score;
    }

    private int calculateSectionScore(Player positivePlayer, BoardStatus board, SectionPosition section, Grid sectionGrid) {
        Player negativePlayer = positivePlayer.opposite();

        GridScoringFunction scoringFunction = scoring.getSectionScoring();
        if (!sectionGrid.canBeWon()) {
            return SectionUnimportantWrapper.calculateGridScoreForPlayer(positivePlayer, sectionGrid, scoringFunction)
                    - SectionUnimportantWrapper.calculateGridScoreForPlayer(negativePlayer, sectionGrid, scoringFunction);
        }

        int score = 0;
        if (board.sectionIsImportantToPlayer(section, positivePlayer)) {
            score += SectionImportantWrapper.calculateGridScoreForPlayer(positivePlayer, sectionGrid, scoringFunction);
        } else {
            score += SectionUnimportantWrapper.calculateGridScoreForPlayer(positivePlayer, sectionGrid, scoringFunction);
        }

        if (board.sectionIsImportantToPlayer(section, negativePlayer)) {
            score -= SectionImportantWrapper.calculateGridScoreForPlayer(negativePlayer, sectionGrid, scoringFunction);
        } else {
            score -= SectionUnimportantWrapper.calculateGridScoreForPlayer(negativePlayer, sectionGrid, scoringFunction);
        }

        return score;
    }

    private int calculateGridScoreWinningGridIsImportantForBothPlayers(Player positivePlayer, Grid grid, GridScoringFunction scoringFunction) {
        Player negativePlayer = positivePlayer.opposite();
        if (!grid.canBeWon()) {
            return SectionUnimportantWrapper.calculateGridScoreForPlayer(positivePlayer, grid, scoringFunction)
                    - SectionUnimportantWrapper.calculateGridScoreForPlayer(negativePlayer, grid, scoringFunction);
        }

        return SectionImportantWrapper.calculateGridScoreForPlayer(positivePlayer, grid, scoringFunction)
                - SectionImportantWrapper.calculateGridScoreForPlayer(negativePlayer, grid, scoringFunction);
    }

    @Override
    public double getWinScore() {
        return WIN_SCORE;
    }

    // Doesn't have any state to reset
    @Override
    public void newGame(BoardStatus board) {
    }

    // Doesn't learn
    @Override
    public void learnFromChange(BoardStatus board) {
    }

    @Override
    protected void saveInternalState(PrintStream logger) {
        GridScoringFunction cF = scoring.getMainScoring();
        logger.print(cF.getCannotWinPointScore() + " " + cF.getOwnsOnlyTakenInLine() + " "
                + cF.getOwnsBothOnlyTakenInLine() + " " + cF.blockedPlayerInLine() + " ");

        cF = scoring.getSectionScoring();
        logger.println(cF.getCannotWinPointScore() + " " + cF.getOwnsOnlyTakenInLine() + " "
                + cF.getOwnsBothOnlyTakenInLine() + " " + cF.blockedPlayerInLine());
    }

    @Override
    public String getIdentifier() {
        return IDENTIFIER;
    }
}
