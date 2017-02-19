package com.diusrex.tictactoe.data_structures;

// This class is meant for storing information about the number
// of lines formed within a certain grid, for both players
public class LinesFormed {
    public LinesFormed(Player mainPlayer) {
        this.mainPlayer = mainPlayer;
    }
    public final Player mainPlayer;
    public int oneFormedForMain, oneFormedForOther;
    public int twoFormedForMain, twoFormedForOther;
    public int mainBlocked, otherBlocked;
    public int emptyLines;
    
    public void reset() {
        oneFormedForMain = oneFormedForOther =
                twoFormedForMain = twoFormedForOther =
                mainBlocked = otherBlocked =
                emptyLines = 0;
    }
}
