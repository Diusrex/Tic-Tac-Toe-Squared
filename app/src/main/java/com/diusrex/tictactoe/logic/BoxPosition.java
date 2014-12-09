package com.diusrex.tictactoe.logic;

public class BoxPosition {

    private final int x;
    private final int y;

    public BoxPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public BoxPosition increaseBy(BoxPosition increase) {
        return new BoxPosition(x + increase.x, y + increase.y);
    }

    public BoxPosition decreaseBy(BoxPosition increase) {
        return new BoxPosition(x - increase.x, y - increase.y);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        BoxPosition other = (BoxPosition) obj;
        if (x != other.x)
            return false;
        if (y != other.y)
            return false;
        return true;
    }

    public SectionPosition getSectionIn() {
        int sectionX = x / BoardStatus.SIZE_OF_SECTION;
        int sectionY = y / BoardStatus.SIZE_OF_SECTION;

        return new SectionPosition(sectionX, sectionY);
    }
}
