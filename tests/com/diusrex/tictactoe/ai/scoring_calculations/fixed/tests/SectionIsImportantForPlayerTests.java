package com.diusrex.tictactoe.ai.scoring_calculations.fixed.tests;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;

import com.diusrex.tictactoe.ai.scoring_calculations.fixed.GridScoringFunction;
import com.diusrex.tictactoe.ai.scoring_calculations.fixed.SectionIsImportantForPlayerScoreCalculator;
import com.diusrex.tictactoe.data_structures.Player;
import com.diusrex.tictactoe.data_structures.position.Position;


public class SectionIsImportantForPlayerTests {
    Player un = Player.Unowned;
    Player p1 = Player.Player_1;
    Player p2 = Player.Player_2;
    
    SectionIsImportantForPlayerScoreCalculator calculator = new SectionIsImportantForPlayerScoreCalculator();
    GridScoringFunction function;
    
    // Making it so a score is unique based on these values
    private final int OwnsOnlyTakenInLineScore = 1;
    private final int OwnsBothTakenInLineScore = 10;
    private final int BlockedPlayerInLineScore = 100;
    
    @Before
    public void setup() {
        function = mock(GridScoringFunction.class);
    }
    
    private void addWhenValuesToFunction() {
        when(function.getOwnsOnlyTakenInLine()).thenReturn(OwnsOnlyTakenInLineScore);
        when(function.getOwnsBothOnlyTakenInLine()).thenReturn(OwnsBothTakenInLineScore);
        when(function.blockedPlayerInLine()).thenReturn(BlockedPlayerInLineScore);
    }
    
    @Test
    public void testGridIsEqual() {
        // No one owns anything
        TestingGrid grid = new TestingGrid(Player.Unowned,
                new Player[][]{
                    {un, un, un},
                    {un, un, un},
                    {un, un, un}});
        
        addWhenValuesToFunction();
        int score = calculator.calculateGridScoreForPlayer(p1, grid, function);
        assertEquals(0, score);
        
        verify(function, never()).getMultiplier(any(Position.class));
        verify(function, never()).getCannotWinPointScore();
        verify(function, never()).getOwnsOnlyTakenInLine();
        verify(function, never()).getOwnsBothOnlyTakenInLine();
        verify(function, never()).blockedPlayerInLine();
    }
    
    @Test
    public void testGridSimpleLines() {
        // For p1, has one line with two positions and two lines with one position.
        // For p2, has one line with one position
        TestingGrid grid = new TestingGrid(Player.Unowned,
                new Player[][]{
                    {p1, un, un},
                    {un, un, un},
                    {p2, un, p1}});
        
        addWhenValuesToFunction();
        int score = calculator.calculateGridScoreForPlayer(p1, grid, function);
        
        int expectedScore = 0;
        verify(function, never()).getMultiplier(any(Position.class));
        verify(function, never()).getCannotWinPointScore();
        verify(function, times(2)).getOwnsOnlyTakenInLine();
        expectedScore += 2 * OwnsOnlyTakenInLineScore;
        verify(function, times(1)).getOwnsBothOnlyTakenInLine();
        expectedScore += 1 * OwnsBothTakenInLineScore;
        verify(function, never()).blockedPlayerInLine();
        
        assertEquals(expectedScore, score);
        
        reset(function);
        
        addWhenValuesToFunction();
        score = calculator.calculateGridScoreForPlayer(p2, grid, function);
        
        expectedScore = 0;
        verify(function, never()).getMultiplier(any(Position.class));
        verify(function, never()).getCannotWinPointScore();
        verify(function, times(1)).getOwnsOnlyTakenInLine();
        expectedScore += 1 * OwnsOnlyTakenInLineScore;
        verify(function, never()).getOwnsBothOnlyTakenInLine();
        verify(function, never()).blockedPlayerInLine();
        
        assertEquals(expectedScore, score);
    }
    
    @Test
    public void testGridBlocked() {
        // For p1, has two lines with two positions, and is blocked once.
        // For p2, has one line with two positions, and is blocked twice.
        TestingGrid grid = new TestingGrid(Player.Unowned,
                new Player[][]{
                    {p1, un, p1},
                    {un, p2, un},
                    {p2, p2, p1}});
        
        addWhenValuesToFunction();
        int score = calculator.calculateGridScoreForPlayer(p1, grid, function);
        
        int expectedScore = 0;
        verify(function, never()).getMultiplier(any(Position.class));
        verify(function, never()).getCannotWinPointScore();
        verify(function, never()).getOwnsOnlyTakenInLine();
        verify(function, times(2)).getOwnsBothOnlyTakenInLine();
        expectedScore += 2 * OwnsBothTakenInLineScore;
        // How many times did the other player block us
        verify(function, times(1)).blockedPlayerInLine();
        expectedScore -= 1 * BlockedPlayerInLineScore;
        
        assertEquals(expectedScore, score);
        
        reset(function);
        
        addWhenValuesToFunction();
        score = calculator.calculateGridScoreForPlayer(p2, grid, function);
        
        expectedScore = 0;
        verify(function, never()).getMultiplier(any(Position.class));
        verify(function, never()).getCannotWinPointScore();
        verify(function, times(1)).getOwnsOnlyTakenInLine();
        expectedScore += 1 * OwnsOnlyTakenInLineScore;
        verify(function, times(1)).getOwnsBothOnlyTakenInLine();
        expectedScore += 1 * OwnsBothTakenInLineScore;
        // How many times did the other player block us
        verify(function, times(2)).blockedPlayerInLine();
        expectedScore -= 1 * BlockedPlayerInLineScore;
    }
}
