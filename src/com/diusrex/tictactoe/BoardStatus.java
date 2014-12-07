package com.diusrex.tictactoe;

public class BoardStatus {
    public static final int NUMBER_OF_BOXES_PER_SIDE = 9;
    public static final int NUMBER_OF_SECTIONS_PER_SIDE = 3;
    public static final int SIZE_OF_SECTION = NUMBER_OF_BOXES_PER_SIDE
            / NUMBER_OF_SECTIONS_PER_SIDE;

    private Player[][] boardOwners;
    private Player[][] sectionOwners;

    private SectionPosition sectionToPlayIn;

    public BoardStatus() {
        this(new SectionPosition(1, 1));
    }

    public BoardStatus(SectionPosition startingSection) {
        initializeBoardOwners();
        initializeSectionOwners();

        sectionToPlayIn = startingSection;
    }

    private void initializeSectionOwners() {
        sectionOwners = new Player[NUMBER_OF_SECTIONS_PER_SIDE][NUMBER_OF_SECTIONS_PER_SIDE];

        for (int x = 0; x < NUMBER_OF_SECTIONS_PER_SIDE; ++x) {
            for (int y = 0; y < NUMBER_OF_SECTIONS_PER_SIDE; ++y)
                setSectionOwner(x, y, Player.Unowned);
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

    public void setSectionOwner(SectionPosition changedSection, Player player) {
        setSectionOwner(changedSection.getX(), changedSection.getY(), player);
    }

    private void setSectionOwner(int x, int y, Player owner) {
        sectionOwners[x][y] = owner;
    }

    public void applyMove(Move move) {
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

    private Player getBoxOwner(int x, int y) {
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
        return (pos.getX() >= 0 && pos.getX() < NUMBER_OF_BOXES_PER_SIDE
                && pos.getY() >= 0 && pos.getY() < NUMBER_OF_BOXES_PER_SIDE);
    }

}
