package com.diusrex.tictactoe.logic;

import java.util.Stack;

import com.diusrex.tictactoe.data_structures.BoardStatus;
import com.diusrex.tictactoe.data_structures.BoxPosition;
import com.diusrex.tictactoe.data_structures.Move;
import com.diusrex.tictactoe.data_structures.Player;
import com.diusrex.tictactoe.data_structures.SectionPosition;

public class StringSaver {
    private StringSaver() {
    }
    
    static final int START_OF_BOX_POSITION = 2;
    static final int START_OF_PLAYER = 4;
    static final int SIZE_OF_SAVED_MOVE = 5;

    public static String getSaveString(BoardStatus board) {
        Stack<Move> allMoves = board.getAllMoves();
        StringBuffer buffer = new StringBuffer();
        for (Move move : allMoves) {
            buffer.append(moveToString(move));
        }

        return buffer.toString();
    }

    // Will change the given board
    // Will allow any move to be played
    public static BoardStatus loadBoardFromString(BoardStatus board, String savedBoardStatus) {
        // Is <= because i + SIZE_OF_SAVED_MOVE is not inclusive
        for (int i = 0; i + SIZE_OF_SAVED_MOVE <= savedBoardStatus.length(); i += SIZE_OF_SAVED_MOVE) {
            Move move = stringToMove(savedBoardStatus.substring(i, i + SIZE_OF_SAVED_MOVE));
            TicTacToeEngine.applyMoveIfValid(board, move);
        }

        return board;
    }

    // TODO: Refactor out of engine, most likely into Move/SectionPosition/BoxPosition
    // Format: [SectionPosX][SectionPosY][BoxPosX][BoxPosY][Player]
    public static Move stringToMove(String string) {
        SectionPosition section = stringToSectionPosition(string.substring(0, START_OF_BOX_POSITION));
        BoxPosition box = stringToBoxPosition(string.substring(START_OF_BOX_POSITION, START_OF_PLAYER));
        Player player = Player.fromString(string.substring(START_OF_PLAYER, SIZE_OF_SAVED_MOVE));

        return new Move(section, box, player);
    }

    private static String moveToString(Move m) {
        return sectionPositionToString(m.getSection()) + boxPositionToString(m.getBox()) + m.getPlayer().toString();
    }

    public static String sectionPositionToString(SectionPosition sectionPosition) {
        return String.format("%d%d", sectionPosition.getGridX(), sectionPosition.getGridY());
    }
    
    private static String boxPositionToString(BoxPosition box) {
        return String.format("%d%d", box.getGridX(), box.getGridY());
    }

    public static SectionPosition stringToSectionPosition(String string) {
        int totalValue = Integer.parseInt(string);
        int x = totalValue / 10;
        int y = totalValue % 10;

        return SectionPosition.make(x, y);
    }

    public static BoxPosition stringToBoxPosition(String string) {
        int totalValue = Integer.parseInt(string);
        int x = totalValue / 10;
        int y = totalValue % 10;

        return BoxPosition.make(x, y);
    }
}
