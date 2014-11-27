package com.diusrex.tictactoe;

public class Move {
    
    private final BoxPosition pos;
    private final Player player;

    public Move(BoxPosition pos, Player madeBy) {
        this.pos = pos;
        this.player = madeBy;
    }

    public BoxPosition getPosition() {
        return pos;
    }
    
    public Player getPlayer() {
        return player;
    }

    public SectionPosition getSectionIn() {
        return pos.getSectionIn();
    }

}
