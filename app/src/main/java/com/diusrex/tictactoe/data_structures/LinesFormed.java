package com.diusrex.tictactoe.data_structures;

// This class is meant for storing information about the number
// of lines formed within a certain grid, for both players
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
    
    // TODO: Should I add this specially for MainGrid?
    // Will NOT count lines where each player owns one (since those would immediatley cancel out).
    // Instead, only applies to lines where one player owns something, and the other player doesn't.
    // Not a huge investment though, and idk how much it would actually improve things.
    //public int oneWasBlockedForMain, oneWasBlockedForOther
    
    // Owns two in a row, but has been blocked (by other player owning,
    // or by a section that cannot be won).
    public int twoWereBlockedForMain, twoWereBlockedForOther;
    
    // Line which can be won by player, but the player currently doesn't have any spots in it.
    // TODO: Convert to be:
    //public int unownedButWinnableForMain, unownedButWinnableForOther;
    public int emptyLines;
    
    public void reset() {
        oneFormedForMain = oneFormedForOther = 0;
        twoFormedForMain = twoFormedForOther = 0;
        twoWereBlockedForMain = twoWereBlockedForOther = 0;
        //unownedButWinnableForMain = unownedButWinnableForOther = 0;
        emptyLines = 0;
    }
    
    @Override
    public String toString() {
        return "Player: " + mainPlayer + " formed 1: " + oneFormedForMain + " 2: " + twoFormedForMain +
                " blocked: " + twoWereBlockedForMain + "." +
                " other formed 1: " + oneFormedForOther + " 2: " + twoFormedForOther +
                " blocked: " + twoWereBlockedForOther + ". Empty lines: " + emptyLines + ".";
    }

    public void copyFrom(LinesFormed from) {
        emptyLines = from.emptyLines;
        
        if (mainPlayer == from.mainPlayer) {
            // Keep same main/other
            oneFormedForMain = from.oneFormedForMain;
            oneFormedForOther = from.oneFormedForOther;
            twoFormedForMain = from.twoFormedForMain;
            twoFormedForOther = from.twoFormedForOther;
            twoWereBlockedForMain = from.twoWereBlockedForMain;
            twoWereBlockedForOther = from.twoWereBlockedForOther;
        } else {
            // Reverse main/other
            oneFormedForMain = from.oneFormedForOther;
            oneFormedForOther = from.oneFormedForMain;
            twoFormedForMain = from.twoFormedForOther;
            twoFormedForOther = from.twoFormedForMain;
            twoWereBlockedForMain = from.twoWereBlockedForOther;
            twoWereBlockedForOther = from.twoWereBlockedForMain;
        }
    }
}
