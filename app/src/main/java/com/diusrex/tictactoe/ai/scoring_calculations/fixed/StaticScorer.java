package com.diusrex.tictactoe.ai.scoring_calculations.fixed;

import java.io.PrintStream;

import com.diusrex.tictactoe.ai.scoring_calculations.*;
import com.diusrex.tictactoe.data_structures.*;
import com.diusrex.tictactoe.data_structures.grid.Grid;
import com.diusrex.tictactoe.data_structures.position.SectionPosition;
import com.diusrex.tictactoe.logic.GridLists;

// Will calculate the score for both players, with the positive player being the player who has their score multiplied by 1,
// while the negative player has his score multiplied by -1 when added together.
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
        return calculateScore(positivePlayer, board, SectionImportantWrapper, SectionUnimportantWrapper);
    }
    
    protected double calculateScore(Player positivePlayer, BoardStatus board, PlayerGridScoreCalculator sectionImportantWrapper,
            PlayerGridScoreCalculator sectionUnimportantWrapper) {
        int score = calculateGridScoreWinningGridIsImportantForBothPlayers(positivePlayer, board.getMainGrid(),
                scoring.getMainScoring(), sectionImportantWrapper, sectionUnimportantWrapper);

        for (SectionPosition section : GridLists.getAllStandardSections()) {
            score += calculateSectionScore(positivePlayer, board, section, board.getSubGrid(section),
                    sectionImportantWrapper, sectionUnimportantWrapper) * scoring.getSectionGridMultiplier(section);
        }

        return score;
    }

    protected int calculateSectionScore(Player positivePlayer, BoardStatus board, SectionPosition section,
            Grid sectionGrid, PlayerGridScoreCalculator sectionImportantWrapper,
            PlayerGridScoreCalculator sectionUnimportantWrapper) {
        Player negativePlayer = positivePlayer.opposite();

        GridScoringFunction scoringFunction = scoring.getSectionScoring();
        // Faster than checking if section is important to each player
        // separately.
        if (!sectionGrid.canBeWon()) {
            return sectionUnimportantWrapper.calculateGridScoreForPlayer(positivePlayer, sectionGrid, scoringFunction)
                    - sectionUnimportantWrapper.calculateGridScoreForPlayer(negativePlayer, sectionGrid,
                            scoringFunction);
        }

        int score = 0;
        if (board.sectionIsImportantToPlayer(section, positivePlayer)) {
            score += sectionImportantWrapper.calculateGridScoreForPlayer(positivePlayer, sectionGrid, scoringFunction);
        } else {
            score += sectionUnimportantWrapper
                    .calculateGridScoreForPlayer(positivePlayer, sectionGrid, scoringFunction);
        }

        if (board.sectionIsImportantToPlayer(section, negativePlayer)) {
            score -= sectionImportantWrapper.calculateGridScoreForPlayer(negativePlayer, sectionGrid, scoringFunction);
        } else {
            score -= sectionUnimportantWrapper
                    .calculateGridScoreForPlayer(negativePlayer, sectionGrid, scoringFunction);
        }

        return score;
    }

    private int calculateGridScoreWinningGridIsImportantForBothPlayers(Player positivePlayer, Grid grid,
            GridScoringFunction scoringFunction, PlayerGridScoreCalculator sectionImportantWrapper,
            PlayerGridScoreCalculator sectionUnimportantWrapper) {
        Player negativePlayer = positivePlayer.opposite();
        if (!grid.canBeWon()) {
            return sectionUnimportantWrapper.calculateGridScoreForPlayer(positivePlayer, grid, scoringFunction)
                    - sectionUnimportantWrapper.calculateGridScoreForPlayer(negativePlayer, grid, scoringFunction);
        }

        return sectionImportantWrapper.calculateGridScoreForPlayer(positivePlayer, grid, scoringFunction)
                - sectionImportantWrapper.calculateGridScoreForPlayer(negativePlayer, grid, scoringFunction);
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
    protected void saveInternalIdentifiers(PrintStream logger) {
        // Doesn't have any special identifiers.
    }

    @Override
    public void saveParameters(PrintStream logger) {
        GridScoringFunction cF = scoring.getMainScoring();
        logger.print(cF.getCannotWinPointScore() + " " + cF.getOwnsOnlyTakenInLine() + " "
                + cF.getOwnsBothOnlyTakenInLine() + " " + cF.blockedPlayerInLine() + " ");

        cF = scoring.getSectionScoring();
        logger.println(cF.getCannotWinPointScore() + " " + cF.getOwnsOnlyTakenInLine() + " "
                + cF.getOwnsBothOnlyTakenInLine() + " " + cF.blockedPlayerInLine());
    }
    
    public ScoringValues getScoringValues() {
        return scoring;
    }

    @Override
    public String getIdentifier() {
        return IDENTIFIER;
    }
}
