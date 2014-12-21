package com.diusrex.tictactoe.logic;

/*
 * GridChecker
 * This class is built around checking to see if a player has made a pattern of 3 inside of the given grid, or optionally inside of the
 * section within the grid
 */
public class GridChecker {
    static final int LINE_LENGTH = 3;
    static final BoxPosition horizontalIncrease = BoxPosition.make(1, 0);
    static final BoxPosition horizontalLineTotalIncrease = BoxPosition.make(2, 0);
    static final BoxPosition verticalIncrease = BoxPosition.make(0, 1);
    static final BoxPosition verticalLineTotalIncrease = BoxPosition.make(0, 2);

    public static Player searchForPattern(Player[][] grid) {
        return searchForPattern(grid, SectionPosition.make(0, 0));
    }

    // Will return the first pattern it finds. If there are multiple, then it
    // will return the first
    public static Player searchForPattern(Player[][] grid, SectionPosition sectionIn) {
        Line foundLine = searchForLineOrGetNull(grid, sectionIn);

        if (foundLine != null) {
            BoxPosition lineCreatorPos = foundLine.getStart();
            return grid[lineCreatorPos.getX()][lineCreatorPos.getY()];
        }
        return Player.Unowned;
    }

    public static Line searchForLineOrGetNull(Player[][] grid) {
        return searchForLineOrGetNull(grid, SectionPosition.make(0, 0));
    }

    public static Line searchForLineOrGetNull(Player[][] grid, SectionPosition sectionIn) {
        if (!gridIsValid(grid, sectionIn))
            return null;

        Line foundLine = searchForHorizontalLineOrNull(grid, sectionIn);
        if (foundLine != null)
            return foundLine;

        foundLine = searchForVertialLineOrNull(grid, sectionIn);
        if (foundLine != null)
            return foundLine;

        return searchForDiagonalLineOrNull(grid, sectionIn);
    }

    private static Line searchForHorizontalLineOrNull(Player[][] grid, SectionPosition sectionIn) {
        BoxPosition currentPos = sectionIn.getTopLeftPosition();

        for (int i = 0; i < LINE_LENGTH; ++i, currentPos = currentPos.increaseBy(verticalIncrease)) {
            if (lineOwnedBySinglePlayer(grid, currentPos, horizontalIncrease))
                return new Line(currentPos, currentPos.increaseBy(horizontalLineTotalIncrease));
        }

        return null;
    }

    private static Line searchForDiagonalLineOrNull(Player[][] grid, SectionPosition sectionIn) {
        BoxPosition currentPos = sectionIn.getTopLeftPosition();
        BoxPosition diagonalIncrease = BoxPosition.make(1, 1);
        BoxPosition diagonalLineTotalIncrease = diagonalIncrease.increaseBy(diagonalIncrease);

        if (lineOwnedBySinglePlayer(grid, currentPos, diagonalIncrease))
            return new Line(currentPos, currentPos.increaseBy(diagonalLineTotalIncrease));

        BoxPosition differenceBetweenDiagonals = BoxPosition.make(2, 0);
        currentPos = currentPos.increaseBy(differenceBetweenDiagonals);

        diagonalIncrease = BoxPosition.make(-1, 1);
        diagonalLineTotalIncrease = diagonalIncrease.increaseBy(diagonalIncrease);

        if (lineOwnedBySinglePlayer(grid, currentPos, diagonalIncrease))
            return new Line(currentPos, currentPos.increaseBy(diagonalLineTotalIncrease));

        return null;
    }

    private static Line searchForVertialLineOrNull(Player[][] grid, SectionPosition sectionIn) {
        BoxPosition currentPos = sectionIn.getTopLeftPosition();

        for (int i = 0; i < LINE_LENGTH; ++i, currentPos = currentPos.increaseBy(horizontalIncrease)) {
            if (lineOwnedBySinglePlayer(grid, currentPos, verticalIncrease))
                return new Line(currentPos, currentPos.increaseBy(verticalLineTotalIncrease));
        }

        return null;
    }

    private static boolean gridIsValid(Player[][] grid, SectionPosition sectionIn) {
        if (grid == null)
            return false;

        boolean validXSpot = sectionIsValid(sectionIn.getX(), grid);
        boolean validYSpot = sectionIsValid(sectionIn.getY(), grid);
        return validXSpot && validYSpot;
    }

    private static boolean sectionIsValid(int sectionValue, Player[][] grid) {
        return sectionValue >= 0 && sectionValue * BoardStatus.SIZE_OF_SECTION < grid.length;
    }

    private static boolean lineOwnedBySinglePlayer(Player[][] grid, BoxPosition lineStart, BoxPosition increase) {
        final Player firstPlayer = getPlayer(grid, lineStart);
        if (firstPlayer == Player.Unowned)
            return false;

        BoxPosition currentSpot = lineStart;
        // Could skip the spot at 0, but this works too
        for (int i = 0; i < LINE_LENGTH; ++i, currentSpot = currentSpot.increaseBy(increase)) {
            if (firstPlayer != getPlayer(grid, currentSpot))
                return false;
        }

        return true;
    }

    private static Player getPlayer(Player[][] grid, BoxPosition pos) {
        return grid[pos.getX()][pos.getY()];
    }


}
