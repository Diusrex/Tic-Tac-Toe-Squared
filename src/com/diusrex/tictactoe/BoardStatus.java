package com.diusrex.tictactoe;

public class BoardStatus {
    public static final int NUMBER_OF_BOXES_PER_SIDE = 9;
    public static final int NUMBER_OF_SECTIONS_PER_SIDE = 3;

    Player[][] boardOwners;

    public BoardStatus() {
        boardOwners = new Player[NUMBER_OF_BOXES_PER_SIDE][NUMBER_OF_BOXES_PER_SIDE];

        for (int x = 0; x < NUMBER_OF_BOXES_PER_SIDE; ++x) {
            for (int y = 0; y < NUMBER_OF_BOXES_PER_SIDE; ++y) {
                setBoxValue(x, y, Player.Unowned);
            }
        }
    }

    public void setBoxValue(Position pos, Player newOwner) {
        setBoxValue(pos.getX(), pos.getY(), newOwner);
    }

    private void setBoxValue(int x, int y, Player owner) {
        boardOwners[x][y] = owner;
    }

    public Player getBoxOwner(Position position) {
        return getBoxOwner(position.getX(), position.getY());
    }

    private Player getBoxOwner(int x, int y) {
        return boardOwners[x][y];
    }

    public int getPlayerCount(Player wantedPlayer) {
        int count = 0;
        for (int x = 0; x < NUMBER_OF_BOXES_PER_SIDE; ++x) {
            for (int y = 0; y < NUMBER_OF_BOXES_PER_SIDE; ++y) {
                if (getBoxOwner(x, y) == wantedPlayer)
                    ++count;
            }
        }

        return count;
    }

    public boolean isInsideBounds(Position pos) {
        return (pos.getX() >= 0 && pos.getX() < NUMBER_OF_BOXES_PER_SIDE
                && pos.getY() >= 0 && pos.getY() < NUMBER_OF_BOXES_PER_SIDE);
    }
}
