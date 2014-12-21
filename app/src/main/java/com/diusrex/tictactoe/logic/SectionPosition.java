package com.diusrex.tictactoe.logic;

public class SectionPosition {
    static private final SectionPosition[][] standardPositions = new SectionPosition[BoardStatus.SIZE_OF_SECTION][BoardStatus.SIZE_OF_SECTION];
    static private boolean initialized = false;

    public static SectionPosition make(int x, int y) {
        if (!initialized) {
            initialize();
            initialized = true;
        }

        if (x >= 0 && x < BoardStatus.SIZE_OF_SECTION && y >= 0 && y < BoardStatus.SIZE_OF_SECTION)
            return standardPositions[x][y];

        else
            return new SectionPosition(x, y);
    }

    private static void initialize() {
        for (int y = 0; y < BoardStatus.SIZE_OF_SECTION; ++y) {
            for (int x = 0; x < BoardStatus.SIZE_OF_SECTION; ++x) {
                standardPositions[x][y] = new SectionPosition(x, y);
            }
        }
    }

    private final int x;
    private final int y;

    private SectionPosition(int x, int y) {
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
        return BoxPosition.make(x * BoardStatus.SIZE_OF_SECTION, y * BoardStatus.SIZE_OF_SECTION);
    }

    public SectionPosition increaseBy(SectionPosition increase) {
        return make(x + increase.x, y + increase.y);
    }
}
