package com.diusrex.tictactoe.logic;


public interface Grid {
    public Player getGridOwner();
    
    public Player getPointOwner(Position pos);
    
    public boolean canBeWon();

}
