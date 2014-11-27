package com.diusrex.tictactoe;

public class SectionPosition {
    private final int x;
    private final int y;
    
    public SectionPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        SectionPosition other = (SectionPosition) obj;
        if (x != other.x)
            return false;
        if (y != other.y)
            return false;
        return true;
    }

    public BoxPosition getTopLeftPosition() {
        return new BoxPosition(x * BoardStatus.SIZE_OF_SECTION, y * BoardStatus.SIZE_OF_SECTION);
    }

    public SectionPosition increaseBy(SectionPosition increase) {
        return new SectionPosition(x + increase.x, y + increase.y);
    }
}
