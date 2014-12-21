package com.diusrex.tictactoe.logic;

import java.util.Arrays;
import java.util.Stack;

public class BoardStatus {
    public static final int NUMBER_OF_BOXES_PER_SIDE = 9;
    public static final int NUMBER_OF_SECTIONS_PER_SIDE = 3;
    public static final int SIZE_OF_SECTION = NUMBER_OF_BOXES_PER_SIDE / NUMBER_OF_SECTIONS_PER_SIDE;

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        BoardStatus other = (BoardStatus) obj;
        if (allMoves == null) {
            if (other.allMoves != null)
                return false;
        } else if (allMoves.size() != other.allMoves.size())
            return false;
        if (!Arrays.deepEquals(boardOwners, other.boardOwners))
            return false;
        if (!Arrays.deepEquals(sectionOwners, other.sectionOwners))
            return false;
        if (sectionToPlayIn == null) {
            if (other.sectionToPlayIn != null)
                return false;
        } else if (!sectionToPlayIn.equals(other.sectionToPlayIn))
            return false;
        return true;
    }

    private Player[][] boardOwners;
    private Player[][] sectionOwners;
    private Line[][] lines;

    private SectionPosition sectionToPlayIn;

    private Stack<Move> allMoves;

    public BoardStatus() {
        this(SectionPosition.make(1, 1));
    }

    public BoardStatus(SectionPosition startingSection) {
        initializeBoardOwners();
        initializeSectionOwners();

        sectionToPlayIn = startingSection;

        allMoves = new Stack<Move>();
    }

    private void initializeSectionOwners() {
        sectionOwners = new Player[NUMBER_OF_SECTIONS_PER_SIDE][NUMBER_OF_SECTIONS_PER_SIDE];
        lines = new Line[NUMBER_OF_SECTIONS_PER_SIDE][NUMBER_OF_SECTIONS_PER_SIDE];

        for (int x = 0; x < NUMBER_OF_SECTIONS_PER_SIDE; ++x) {
            for (int y = 0; y < NUMBER_OF_SECTIONS_PER_SIDE; ++y)
                setSectionOwner(x, y, null, Player.Unowned);
        }
    }

    private void initializeBoardOwners() {
        boardOwners = new Player[NUMBER_OF_BOXES_PER_SIDE][NUMBER_OF_BOXES_PER_SIDE];

        for (int x = 0; x < NUMBER_OF_BOXES_PER_SIDE; ++x) {
            for (int y = 0; y < NUMBER_OF_BOXES_PER_SIDE; ++y)
                setBoxOwner(x, y, Player.Unowned);
        }
    }

    public SectionPosition getSectionToPlayIn() {
        return sectionToPlayIn;
    }

    public void setSectionOwner(SectionPosition changedSection, Line line, Player player) {
        setSectionOwner(changedSection.getX(), changedSection.getY(), line, player);
    }

    private void setSectionOwner(int x, int y, Line line, Player owner) {
        sectionOwners[x][y] = owner;
        lines[x][y] = line;
    }

    public void applyMove(Move move) {
        allMoves.push(move);
        setBoxOwner(move.getPosition(), move.getPlayer());
    }

    public void setBoxOwner(BoxPosition pos, Player newOwner) {
        setBoxOwner(pos.getX(), pos.getY(), newOwner);
    }

    private void setBoxOwner(int x, int y, Player owner) {
        boardOwners[x][y] = owner;
    }

    public void setSectionToPlayIn(SectionPosition pos) {
        sectionToPlayIn = pos;
    }

    public Player getBoxOwner(BoxPosition pos) {
        return getBoxOwner(pos.getX(), pos.getY());
    }

    public Player getBoxOwner(int x, int y) {
        return boardOwners[x][y];
    }

    public Player getSectionOwner(SectionPosition pos) {
        return getSectionOwner(pos.getX(), pos.getY());
    }

    private Player getSectionOwner(int x, int y) {
        return sectionOwners[x][y];
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

    public boolean isInsideBounds(BoxPosition pos) {
        return (pos.getX() >= 0 && pos.getX() < NUMBER_OF_BOXES_PER_SIDE && pos.getY() >= 0 && pos.getY() < NUMBER_OF_BOXES_PER_SIDE);
    }

    public Player[][] getBoxGrid() {
        return boardOwners;
    }

    public Player[][] getOwnerGrid() {
        return sectionOwners;
    }

    public Stack<Move> getAllMoves() {
        return allMoves;
    }

    public Line getLine(SectionPosition sectionPosition) {
        return lines[sectionPosition.getX()][sectionPosition.getY()];
    }
}
