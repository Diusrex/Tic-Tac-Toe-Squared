package com.diusrex.tictactoe.logic;


public class SectionGrid implements Grid {
    static private final int SECTION_SIZE = 3;
    private Player[][] owners;
    private Player owner;

    private Line winningLine;

    SectionGrid() {
        owners = new Player[SECTION_SIZE][SECTION_SIZE];
        owner = Player.Unowned;

        for (int x = 0; x < SECTION_SIZE; ++x) {
            for (int y = 0; y < SECTION_SIZE; ++y) {
                owners[x][y] = Player.Unowned;
            }
        }
    }

    public void setSectionOwner(Player owner, Line winningLine) {
        this.owner = owner;
        this.winningLine = winningLine;
    }
    
    @Override
    public Player getGridOwner() {
        return owner;
    }
    
    public void setPointOwner(BoxPosition pos, Player newOwner) {
        owners[pos.getGridX()][pos.getGridY()] = newOwner;
    }

    @Override
    public Player getPointOwner(Position pos) {
        return getPointOwner(pos.getGridX(), pos.getGridY());
    }

    private Player getPointOwner(int x, int y) {
        return owners[x][y];
    }

    @Override
    public boolean canBeWon() {
        // TODO Auto-generated method stub
        return false;
    }

    public Line getLine() {
        return winningLine;
    }

    public boolean isInsideBounds(BoxPosition pos) {
        return pos.getGridX() >= 0 && pos.getGridX() < SECTION_SIZE
                && pos.getGridY() >= 0 && pos.getGridY() < SECTION_SIZE;
    }
}
