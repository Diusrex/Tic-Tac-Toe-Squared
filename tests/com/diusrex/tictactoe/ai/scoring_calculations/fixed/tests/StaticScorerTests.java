package com.diusrex.tictactoe.ai.scoring_calculations.fixed.tests;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import com.diusrex.tictactoe.ai.scoring_calculations.fixed.GridScoringFunction;
import com.diusrex.tictactoe.ai.scoring_calculations.fixed.PlayerGridScoreCalculator;
import com.diusrex.tictactoe.ai.scoring_calculations.fixed.ScoringValues;
import com.diusrex.tictactoe.ai.scoring_calculations.fixed.SectionIsImportantForPlayerScoreCalculator;
import com.diusrex.tictactoe.ai.scoring_calculations.fixed.SectionIsUnimportantForPlayerScoreCalculator;
import com.diusrex.tictactoe.ai.scoring_calculations.fixed.StaticScorer;
import com.diusrex.tictactoe.data_structures.Player;
import com.diusrex.tictactoe.data_structures.board_status.BoardStatus;
import com.diusrex.tictactoe.data_structures.grid.Grid;
import com.diusrex.tictactoe.data_structures.grid.MainGrid;
import com.diusrex.tictactoe.data_structures.grid.SectionGrid;
import com.diusrex.tictactoe.data_structures.position.SectionPosition;
import com.diusrex.tictactoe.logic.GridLists;

public class StaticScorerTests {
    Player p1 = Player.Player_1;
    Player p2 = Player.Player_2;
    // TODO: Test section that can be won, can't be won, isn't important to
    // specific player.
    // will test using SectionWrappers - what do they report being called with?
    @Mock
    SectionIsImportantForPlayerScoreCalculator importantCalculator = mock(SectionIsImportantForPlayerScoreCalculator.class);
    @Mock
    SectionIsUnimportantForPlayerScoreCalculator unimportantCalculator = mock(SectionIsUnimportantForPlayerScoreCalculator.class);

    @Mock
    BoardStatus board;

    GridScoringFunction mainFunction = new GridScoringFunction.Builder().build();
    GridScoringFunction sectionFunction = new GridScoringFunction.Builder().build();

    @Mock
    ScoringValues scoringValues;
    @Mock
    MainGrid sectionsOwnersGrid;

    // [x][y]
    @Mock
    SectionGrid sectionsGrids[][] = new SectionGrid[3][3];

    StaticScorerWrapper scorer;

    @Before
    public void setup() {
        scoringValues = mock(ScoringValues.class);
        sectionsOwnersGrid = mock(MainGrid.class);
        board = mock(BoardStatus.class);

        when(board.getMainGrid()).thenReturn(sectionsOwnersGrid);

        for (int x = 0; x < 3; ++x) {
            for (int y = 0; y < 3; ++y) {
                sectionsGrids[x][y] = mock(SectionGrid.class);

                when(board.getSubGrid(SectionPosition.make(x, y))).thenReturn(sectionsGrids[x][y]);
            }
        }

        when(scoringValues.getMainScoring()).thenReturn(mainFunction);
        when(scoringValues.getSectionScoring()).thenReturn(sectionFunction);
        scorer = new StaticScorerWrapper(scoringValues);
    }

    @Test
    public void checkWhenBoardIsWinnable() {
        when(sectionsOwnersGrid.canBeWon()).thenReturn(true);

        // All of the sections are winnable, but not all are important to the
        // players
        // Only ones that are changed to return true will be important for the
        // player
        for (int x = 0; x < 3; ++x) {
            for (int y = 0; y < 3; ++y) {
                when(sectionsGrids[x][y].canBeWon()).thenReturn(true);
            }
        }

        Set<SectionPosition> sectionsImportantToPlayer1 = new HashSet<SectionPosition>(Arrays.asList(
                SectionPosition.make(0, 0), SectionPosition.make(2, 0), SectionPosition.make(1, 1),
                SectionPosition.make(1, 2)));

        addSectionsAsImportantToPlayer(sectionsImportantToPlayer1, p1);

        Set<SectionPosition> sectionsImportantToPlayer2 = new HashSet<SectionPosition>(Arrays.asList(
                SectionPosition.make(0, 0), SectionPosition.make(1, 0), SectionPosition.make(2, 0),
                SectionPosition.make(1, 1), SectionPosition.make(0, 2)));

        addSectionsAsImportantToPlayer(sectionsImportantToPlayer2, p2);

        scorer.calculateScore(p1, board, importantCalculator, unimportantCalculator);

        // Sections owners grid was treated important for both players
        verify(importantCalculator, times(1)).calculateGridScoreForPlayer(p1, sectionsOwnersGrid, mainFunction);
        verify(importantCalculator, times(1)).calculateGridScoreForPlayer(p2, sectionsOwnersGrid, mainFunction);

        // Each section grid was treated important or unimportant correctly
        verifyCorrectSectionsAreImportant(sectionsImportantToPlayer1, p1);
        verifyCorrectSectionsAreImportant(sectionsImportantToPlayer2, p2);
    }

    @Test
    public void checkWhenBoardIsNotWinnable() {
        when(sectionsOwnersGrid.canBeWon()).thenReturn(false);

        // TODO: Setup sections grid
        when(sectionsOwnersGrid.canBeWon()).thenReturn(false);

        // Just a few sections are winnable at all, and less are important to
        // the players.
        Set<SectionPosition> sectionsImportantToPlayer1 = new HashSet<SectionPosition>(Arrays.asList(
                SectionPosition.make(0, 0), SectionPosition.make(2, 0)));
        Set<SectionPosition> sectionsImportantToPlayer2 = new HashSet<SectionPosition>(Arrays.asList(
                SectionPosition.make(2, 0), SectionPosition.make(1, 1), SectionPosition.make(1, 2)));

        // Mark the important sections as winnable
        for (SectionPosition section : GridLists.getAllStandardSections()) {
            if (sectionsImportantToPlayer1.contains(section) || sectionsImportantToPlayer2.contains(section)) {
                when(sectionsGrids[section.getGridX()][section.getGridY()].canBeWon()).thenReturn(true);
            }
        }

        addSectionsAsImportantToPlayer(sectionsImportantToPlayer1, p1);
        addSectionsAsImportantToPlayer(sectionsImportantToPlayer2, p2);

        scorer.calculateScore(p1, board, importantCalculator, unimportantCalculator);

        // Sections owners grid was treated unimportant for both players
        verify(unimportantCalculator, times(1)).calculateGridScoreForPlayer(p1, sectionsOwnersGrid, mainFunction);
        verify(unimportantCalculator, times(1)).calculateGridScoreForPlayer(p2, sectionsOwnersGrid, mainFunction);

        // Each section grid was treated important or unimportant correctly
        verifyCorrectSectionsAreImportant(sectionsImportantToPlayer1, p1);
        verifyCorrectSectionsAreImportant(sectionsImportantToPlayer2, p2);
    }

    // All other sections will default to unimportant, so don't need to be
    // handled specially
    private void addSectionsAsImportantToPlayer(Set<SectionPosition> sectionsImportantToPlayer, Player player) {
        for (SectionPosition section : sectionsImportantToPlayer) {
            when(board.sectionIsImportantToPlayer(section, player)).thenReturn(true);
        }

    }

    private void verifyCorrectSectionsAreImportant(Set<SectionPosition> sectionsImportantToPlayer, Player player) {
        for (SectionPosition section : GridLists.getAllStandardSections()) {
            SectionGrid grid = sectionsGrids[section.getGridX()][section.getGridY()];
            if (sectionsImportantToPlayer.contains(section)) {
                verify(importantCalculator, times(1)).calculateGridScoreForPlayer(player, grid, sectionFunction);
            } else {
                verify(unimportantCalculator, times(1)).calculateGridScoreForPlayer(player, grid, sectionFunction);
            }
        }
    }

    // More detailed tests
    @Test
    public void checkScoringOfSectionImportantToBoth() {
        // Arbitrary position, doesn't matter as long as refer to it.
        SectionPosition section = SectionPosition.make(0, 0);
        SectionGrid grid = mock(SectionGrid.class);

        when(grid.canBeWon()).thenReturn(true);

        when(board.sectionIsImportantToPlayer(section, p1)).thenReturn(true);
        when(board.sectionIsImportantToPlayer(section, p2)).thenReturn(true);

        // 10 pts for p1, 5 pts for p2
        when(importantCalculator.calculateGridScoreForPlayer(p1, grid, sectionFunction)).thenReturn(10);
        when(importantCalculator.calculateGridScoreForPlayer(p2, grid, sectionFunction)).thenReturn(5);

        assertEquals(5, scorer.calculateSectionScoreExposed(p1, board, section, grid), 0.000001);
        assertEquals(-5, scorer.calculateSectionScoreExposed(p2, board, section, grid), 0.000001);
    }

    @Test
    public void checkScoringOfSectionImportantToOne() {
        // Arbitrary position, doesn't matter as long as refer to it.
        SectionPosition section = SectionPosition.make(0, 0);
        SectionGrid grid = mock(SectionGrid.class);

        when(grid.canBeWon()).thenReturn(true);

        when(board.sectionIsImportantToPlayer(section, p1)).thenReturn(true);
        when(board.sectionIsImportantToPlayer(section, p2)).thenReturn(false);

        // 10 pts for p1, 5 pts for p2
        when(importantCalculator.calculateGridScoreForPlayer(p1, grid, sectionFunction)).thenReturn(10);
        when(unimportantCalculator.calculateGridScoreForPlayer(p2, grid, sectionFunction)).thenReturn(5);

        assertEquals(5, scorer.calculateSectionScoreExposed(p1, board, section, grid), 0.000001);
        assertEquals(-5, scorer.calculateSectionScoreExposed(p2, board, section, grid), 0.000001);
    }

    @Test
    public void checkScoringOfSectionImportantToNeither() {
        // Arbitrary position, doesn't matter as long as refer to it.
        SectionPosition section = SectionPosition.make(0, 0);
        SectionGrid grid = mock(SectionGrid.class);

        when(grid.canBeWon()).thenReturn(true);

        when(board.sectionIsImportantToPlayer(section, p1)).thenReturn(false);
        when(board.sectionIsImportantToPlayer(section, p2)).thenReturn(false);

        // 10 pts for p1, 5 pts for p2
        when(unimportantCalculator.calculateGridScoreForPlayer(p1, grid, sectionFunction)).thenReturn(10);
        when(unimportantCalculator.calculateGridScoreForPlayer(p2, grid, sectionFunction)).thenReturn(5);

        assertEquals(5, scorer.calculateSectionScoreExposed(p1, board, section, grid), 0.000001);
        assertEquals(-5, scorer.calculateSectionScoreExposed(p2, board, section, grid), 0.000001);

        // Now, ensure get same results when saying that the grid can't be won
        reset(grid);
        reset(board);
        when(grid.canBeWon()).thenReturn(false);

        when(board.sectionIsImportantToPlayer(section, p1)).thenReturn(true);
        when(board.sectionIsImportantToPlayer(section, p2)).thenReturn(true);

        // 10 pts for p1, 5 pts for p2
        when(unimportantCalculator.calculateGridScoreForPlayer(p1, grid, sectionFunction)).thenReturn(10);
        when(unimportantCalculator.calculateGridScoreForPlayer(p2, grid, sectionFunction)).thenReturn(5);

        assertEquals(5, scorer.calculateSectionScoreExposed(p1, board, section, grid), 0.000001);
        assertEquals(-5, scorer.calculateSectionScoreExposed(p2, board, section, grid), 0.000001);
    }

    class StaticScorerWrapper extends StaticScorer {
        public StaticScorerWrapper(ScoringValues scoring) {
            super(scoring);
        }

        public double calculateScore(Player positivePlayer, BoardStatus board,
                PlayerGridScoreCalculator sectionImportantWrapper, PlayerGridScoreCalculator sectionUnimportantWrapper) {
            return super.calculateScore(positivePlayer, board, sectionImportantWrapper, sectionUnimportantWrapper);
        }

        public double calculateSectionScoreExposed(Player positivePlayer, BoardStatus board, SectionPosition section,
                Grid sectionGrid) {
            return calculateSectionScore(positivePlayer, board, section, sectionGrid, importantCalculator,
                    unimportantCalculator);
        }
    };
}
