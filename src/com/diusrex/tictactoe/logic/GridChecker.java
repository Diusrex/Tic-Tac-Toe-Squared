package com.diusrex.tictactoe.logic;

/*
 * GridChecker
 * This class is built around checking to see if a player has made a pattern of 3 inside of the given grid, or optionally inside of the
 * section within the grid
 */
public class GridChecker {
    static final int LINE_LENGTH = 3;

    public static Player searchForPattern(Player[][] grid) {
        return searchForPattern(grid, new SectionPosition(0, 0));
    }

    // Will return the first pattern it finds. If there are multiple, then it
    // will return the first
    public static Player searchForPattern(Player[][] grid, SectionPosition sectionIn) {
        if (!gridIsValid(grid, sectionIn))
            return Player.Unowned;

        Player foundPlayer = searchForHorizontalPattern(grid, sectionIn);

        if (foundPlayer != Player.Unowned)
            return foundPlayer;

        foundPlayer = searchForVerticalPattern(grid, sectionIn);

        if (foundPlayer != Player.Unowned)
            return foundPlayer;

        return searchForDiagonalPattern(grid, sectionIn);
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

    private static Player searchForHorizontalPattern(Player[][] grid, SectionPosition sectionIn) {
        BoxPosition currentPos = sectionIn.getTopLeftPosition();
        BoxPosition horizontalIncrease = new BoxPosition(1, 0);
        BoxPosition verticalIncrease = new BoxPosition(0, 1);

        for (int i = 0; i < LINE_LENGTH; ++i, currentPos = currentPos.increaseBy(verticalIncrease)) {
            if (lineOwnedBySinglePlayer(grid, currentPos, horizontalIncrease))
                return getPlayer(grid, currentPos);
        }
        return Player.Unowned;
    }

    private static Player searchForVerticalPattern(Player[][] grid, SectionPosition sectionIn) {
        BoxPosition currentPos = sectionIn.getTopLeftPosition();
        BoxPosition horizontalIncrease = new BoxPosition(1, 0);
        BoxPosition verticalIncrease = new BoxPosition(0, 1);

        for (int i = 0; i < LINE_LENGTH; ++i, currentPos = currentPos.increaseBy(horizontalIncrease)) {
            if (lineOwnedBySinglePlayer(grid, currentPos, verticalIncrease))
                return getPlayer(grid, currentPos);
        }
        return Player.Unowned;
    }

    private static Player searchForDiagonalPattern(Player[][] grid, SectionPosition sectionIn) {
        BoxPosition currentPos = sectionIn.getTopLeftPosition();
        BoxPosition diagonalIncrease = new BoxPosition(1, 1);

        if (lineOwnedBySinglePlayer(grid, currentPos, diagonalIncrease))
            return getPlayer(grid, currentPos);

        BoxPosition differenceBetweenDiagonals = new BoxPosition(2, 0);
        currentPos = currentPos.increaseBy(differenceBetweenDiagonals);

        diagonalIncrease = new BoxPosition(-1, 1);

        if (lineOwnedBySinglePlayer(grid, currentPos, diagonalIncrease))
            return getPlayer(grid, currentPos);

        return Player.Unowned;
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
