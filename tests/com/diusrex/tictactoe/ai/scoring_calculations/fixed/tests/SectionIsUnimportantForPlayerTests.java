package com.diusrex.tictactoe.ai.scoring_calculations.fixed.tests;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;

import com.diusrex.tictactoe.ai.scoring_calculations.fixed.GridScoringFunction;
import com.diusrex.tictactoe.ai.scoring_calculations.fixed.SectionIsUnimportantForPlayerScoreCalculator;
import com.diusrex.tictactoe.data_structures.Player;
import com.diusrex.tictactoe.data_structures.position.Position;

// Simple tests, since the calculation for this is pretty basic.
public class SectionIsUnimportantForPlayerTests {
    Player un = Player.Unowned;
    Player p1 = Player.Player_1;
    Player p2 = Player.Player_2;
    
    SectionIsUnimportantForPlayerScoreCalculator calculator = new SectionIsUnimportantForPlayerScoreCalculator();
    GridScoringFunction function;
    
    private final int CannotWinPointScore = -1;
    
    @Before
    public void setup() {
        function = mock(GridScoringFunction.class);
    }
    private void addWhenValuesToFunction() {
        when(function.getCannotWinPointScore()).thenReturn(CannotWinPointScore);
    }
    
    @Test
    public void testGridSimple() {
        // p1 has 4 spots.
        // p2 has 3 spots.
        TestingGrid grid = new TestingGrid(Player.Unowned,
                new Player[][]{
                    {p1, p1, p2},
                    {un, p2, p1},
                    {un, p2, p1}});

        addWhenValuesToFunction();
        int score = calculator.calculateGridScoreForPlayer(p1, grid, function);
        
        int expectedScore = 0;
        verify(function, never()).getMultiplier(any(Position.class));
        verify(function, times(4)).getCannotWinPointScore();
        expectedScore += 4 * CannotWinPointScore;
        verify(function, never()).getOwnsOnlyTakenInLine();
        verify(function, never()).getOwnsBothOnlyTakenInLine();
        verify(function, never()).blockedPlayerInLine();
        
        assertEquals(expectedScore, score);
        
        reset(function);
        
        addWhenValuesToFunction();
        score = calculator.calculateGridScoreForPlayer(p2, grid, function);
        
        expectedScore = 0;
        verify(function, never()).getMultiplier(any(Position.class));
        verify(function, times(3)).getCannotWinPointScore();
        expectedScore += 3 * CannotWinPointScore;
        verify(function, never()).getOwnsOnlyTakenInLine();
        verify(function, never()).getOwnsBothOnlyTakenInLine();
        verify(function, never()).blockedPlayerInLine();
        
        assertEquals(expectedScore, score);
    }
}
