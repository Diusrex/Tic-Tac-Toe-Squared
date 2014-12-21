package com.diusrex.tictactoe.logic;

// Immutable data structure
public class BoxPosition {
    static private final BoxPosition[][] standardPositions = new BoxPosition[BoardStatus.NUMBER_OF_BOXES_PER_SIDE][BoardStatus.NUMBER_OF_BOXES_PER_SIDE];
    static private boolean initialized = false;

    public static BoxPosition make(int x, int y) {
        if (!initialized) {
            initialize();
            initialized = true;
        }

        if (x >= 0 && x < BoardStatus.NUMBER_OF_BOXES_PER_SIDE && y >= 0 && y < BoardStatus.NUMBER_OF_BOXES_PER_SIDE)
            return standardPositions[x][y];

        else
            return new BoxPosition(x, y);
    }

    private static void initialize() {
        for (int y = 0; y < BoardStatus.NUMBER_OF_BOXES_PER_SIDE; ++y) {
            for (int x = 0; x < BoardStatus.NUMBER_OF_BOXES_PER_SIDE; ++x) {
                standardPositions[x][y] = new BoxPosition(x, y);
            }
        }
    }

    private final int x;
    private final int y;

    private BoxPosition(int x, int y) {
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
        return increaseBy(increase.x, increase.y);
    }

    public BoxPosition increaseBy(int x, int y) {
        return make(this.x + x, this.y + y);
    }

    public BoxPosition decreaseBy(BoxPosition decrease) {
        return increaseBy(-decrease.x, -decrease.y);
    }

    public BoxPosition decreaseBy(int x, int y) {
        return increaseBy(-x, -y);
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

        return SectionPosition.make(sectionX, sectionY);
    }
}
