package com.diusrex.tictactoe.logic;


public class MainGrid implements Grid {
    private Player owner;
    private Player[][] boardOwners;

    MainGrid() {
        boardOwners = new Player[BoardStatus.NUMBER_OF_SECTIONS_PER_SIDE][BoardStatus.NUMBER_OF_BOXES_PER_SIDE];

        for (int x = 0; x < boardOwners.length; ++x) {
            for (int y = 0; y < boardOwners.length; ++y) {
                boardOwners[x][y] = Player.Unowned;
            }
        }

        owner = Player.Unowned;
    }

    @Override
    public Player getGridOwner() {
        return owner;
    }

    @Override
    public Player getPointOwner(Position pos) {
        return getOwner(pos.getGridX(), pos.getGridY());
    }

    private Player getOwner(int x, int y) {
        return boardOwners[x][y];
    }

    @Override
    public boolean canBeWon() {
        // TODO Auto-generated method stub
        return false;
    }

    public void setOwner(SectionPosition pos, Player owner) {
        boardOwners[pos.getGridX()][pos.getGridY()] = owner;
    }

}
