package com.diusrex.tictactoe;

public class Move {
    
    private final Position pos;
    private final Player player;

    public Move(Position pos, Player madeBy) {
        this.pos = pos;
        this.player = madeBy;
    }

    public Position getPosition() {
        return pos;
    }
    
    public Player getPlayer() {
        return player;
    }

}
