package com.diusrex.tictactoe.data_structures;

// This class is meant for storing information about the number
// of lines formed within a certain grid, for both players
// While this class doesn't have a dedicated suite of tests, it is tested
// through usage in many different AI and grid checker tests. Especially MainGridTests
// TODO: Make sure all tests for this file are correct!
public class LinesFormed {
    public LinesFormed(Player mainPlayer) {
        assert(mainPlayer != Player.Unowned);
        this.mainPlayer = mainPlayer;
    }
    public final Player mainPlayer;
    
    // Note that it isn't required for there to be a count for every valid line.
    // It is possible that 
    
    // Owns 1 in a row, where it can win the line.
    public int oneFormedForMain, oneFormedForOther;
    
    // Owns 2 in a row where it can win the line.
    public int twoFormedForMain, twoFormedForOther;

    // Owns 3 in a row. If only one has a count, then it is the winner.
    // Otherwise, it is the first to reach this
    public int threeFormedForMain, threeFormedForOther;
    
    // Will NOT count lines where each player owns one (since those would cancel out).
    // Instead, only applies to lines where one player owns something, and the other player doesn't,
    // but a point is not winnable. Just applies to the MainGrid, since the smaller grid can't reach this situation.
    public int oneInRowWasBlockedForMain, oneInRowWasBlockedForOther;
    
    // Owns two in a row, but has been blocked (by other player owning,
    // or by a section that cannot be won).
    public int twoInRowWasBlockedForMain, twoInRowWasBlockedForOther;

    // Lines which can be won by player, but the player currently doesn't have any spots in it.
    public int unownedButWinnableForMain, unownedButWinnableForOther;


    public void reset() {
        oneFormedForMain = oneFormedForOther = 0;
        oneInRowWasBlockedForMain = oneInRowWasBlockedForOther = 0;
        twoFormedForMain = twoFormedForOther = 0;
        twoInRowWasBlockedForMain = twoInRowWasBlockedForOther = 0;
        threeFormedForMain = threeFormedForOther = 0;
        unownedButWinnableForMain = unownedButWinnableForOther = 0;
    }
    
    @Override
    public String toString() {
        return "Player: " + mainPlayer +
                " formed 1: " + oneFormedForMain + " 1-blocked: " + oneInRowWasBlockedForMain +
                " 2-in-row: " + twoFormedForMain + " 2-blocked: " + twoInRowWasBlockedForMain + ", " +
                "unowned but winnable for main: " + unownedButWinnableForMain + "." +
                " Other formed 1: " + oneFormedForOther + " 1-blocked: " + oneInRowWasBlockedForOther +
                " 2-in-row: " + twoFormedForOther + " 2-blocked: " + twoInRowWasBlockedForOther + ", " +
                "unowned but winnable for other: " + unownedButWinnableForOther + ".";
    }

    public void copyFrom(LinesFormed from) {
        if (mainPlayer == from.mainPlayer) {
            unownedButWinnableForMain = from.unownedButWinnableForMain;
            unownedButWinnableForOther = from.unownedButWinnableForOther;

            oneFormedForMain = from.oneFormedForMain;
            oneFormedForOther = from.oneFormedForOther;

            oneInRowWasBlockedForMain = from.oneInRowWasBlockedForMain;
            oneInRowWasBlockedForOther = from.oneInRowWasBlockedForOther;

            twoFormedForMain = from.twoFormedForMain;
            twoFormedForOther = from.twoFormedForOther;

            twoInRowWasBlockedForMain = from.twoInRowWasBlockedForMain;
            twoInRowWasBlockedForOther = from.twoInRowWasBlockedForOther;
            
            threeFormedForMain = from.threeFormedForMain;
            threeFormedForOther = from.threeFormedForOther;
        } else {
            // Reverse main/other
            unownedButWinnableForMain = from.unownedButWinnableForOther;
            unownedButWinnableForOther = from.unownedButWinnableForMain;

            oneFormedForMain = from.oneFormedForOther;
            oneFormedForOther = from.oneFormedForMain;

            oneInRowWasBlockedForMain = from.oneInRowWasBlockedForOther;
            oneInRowWasBlockedForOther = from.oneInRowWasBlockedForMain;

            twoFormedForMain = from.twoFormedForOther;
            twoFormedForOther = from.twoFormedForMain;

            twoInRowWasBlockedForMain = from.twoInRowWasBlockedForOther;
            twoInRowWasBlockedForOther = from.twoInRowWasBlockedForMain;
            
            threeFormedForMain = from.threeFormedForOther;
            threeFormedForOther = from.threeFormedForMain;
        }
    }
}
