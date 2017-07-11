package com.diusrex.tictactoe.ai;

import com.diusrex.tictactoe.data_structures.*;
import com.diusrex.tictactoe.data_structures.board_status.BoardStatus;
import com.diusrex.tictactoe.data_structures.board_status.StringSaver;
import com.diusrex.tictactoe.data_structures.position.BoxPosition;
import com.diusrex.tictactoe.data_structures.position.SectionPosition;
import com.diusrex.tictactoe.logic.GridLists;

import java.io.PrintStream;
import java.util.*;

public class FullMonteCarloPlayer extends AIPlayer {
    public static final String IDENTIFIER = "FullMonteCarlo";
    private int numberOfIterations;

    private Map<String, FullMonteCarloPlayerStateNode> stateToMove;

    public static FullMonteCarloPlayer getStandardPlayer() {
        return new FullMonteCarloPlayer(100000);
    }

    public FullMonteCarloPlayer(int numberOfIterations) {
        super();
        stateToMove = new HashMap<>();
        this.numberOfIterations = numberOfIterations;
    }

    @Override
    protected Move choosePosition(BoardStatus board) {

        // The starting node is created from the move belonging to the OTHER player
        FullMonteCarloPlayerStateNode startingNode = stateToMove.get(StringSaver.getSaveString(board));
        if (startingNode == null) {
            startingNode = new FullMonteCarloPlayerStateNode(board.getNextPlayer().opposite(), null);
        }

        if (startingNode.isLeaf()) {
            startingNode.expand(getAllValidMoves(board));
        }

        Player currentPlayer = board.getNextPlayer();
        Random random = getRandom();

        for (int i = 0; i < numberOfIterations; ++i) {
            runOneIteration(board, startingNode, currentPlayer, random);
        }

        return startingNode.getBestMove();
    }

    protected Random getRandom() {
        return new Random();
    }

    private void runOneIteration(BoardStatus board, FullMonteCarloPlayerStateNode startingNode, Player mainPlayer, Random random) {
        List<FullMonteCarloPlayerStateNode> visited = new LinkedList<>();
        FullMonteCarloPlayerStateNode currentNode = startingNode.select(random); // Don't want to apply the startingNode, or even update its stats

        // Need to handle the possibility that it has reached a draw, or no valid moves exist
        while (currentNode != null && !currentNode.isLeaf()) {
            visited.add(currentNode);
            board.applyMoveIfValid(currentNode.getMove());

            currentNode = currentNode.select(random);
        }

        Player winner;
        if (currentNode != null) {
            // Need to add currentNode to the list, and apply it to the board
            visited.add(currentNode);
            board.applyMoveIfValid(currentNode.getMove());

            currentNode.expand(getAllValidMoves(board));

            // Save the current node into the tree
            if (board.getNextPlayer() == mainPlayer) { // But no need to save the player who is not going next
                stateToMove.put(StringSaver.getSaveString(board), currentNode);
            }

            winner = winsRandomGame(board, random);
        } else {
            winner = Player.Unowned; // In this case, the node encountered was a draw
        }

        updateStats(board, visited, winner);

        // There is no need to update the startingNode, because it was actually the opponents move
    }

    private void updateStats(BoardStatus board, List<FullMonteCarloPlayerStateNode> visited, Player winner) {
        for (FullMonteCarloPlayerStateNode nodeVisited : visited) {
            // Reset the board state, and update all values
            nodeVisited.updateStats(winner);
            //System.out.println("Undoing for " + nodeVisited);
            board.undoLastMove();
        }
    }

    // TODO: If this is fairly efficient, could apply to all of the AI's
    private List<Move> getAllValidMoves(BoardStatus board) {
        if (canPlayInAnySection(board)) {
            return getValidMovesInAllSections(board);
        } else {
            return getValidMovesInRequiredSection(board);
        }
    }

    private List<Move> getValidMovesInAllSections(BoardStatus board) {
        List<Move> allValidMoves = new ArrayList<>();

        for (SectionPosition section : GridLists.getAllStandardSections()) {
            addValidMovesInSection(board, section, allValidMoves);
        }

        return allValidMoves;
    }

    private List<Move> getValidMovesInRequiredSection(BoardStatus board) {
        List<Move> allValidMoves = new ArrayList<>();
        addValidMovesInSection(board, board.getSectionToPlayIn(), allValidMoves);

        return allValidMoves;
    }

    private void addValidMovesInSection(BoardStatus board, SectionPosition section, List<Move> allValidMoves) {
        for (BoxPosition pos : GridLists.getAllStandardBoxPositions()) {
            Move move = Move.make(section, pos, board.getNextPlayer());

            if (board.isValidMove(move)) {
                allValidMoves.add(move);
            }
        }
    }

    private Player winsRandomGame(BoardStatus board, Random random) {
        Player winner = board.getWinner();
        if (winner == Player.Unowned) {
            List<Move> allValidMoves = getAllValidMoves(board);
            if (allValidMoves.size() == 0) {
                return Player.Unowned;
            }

            int chosen = random.nextInt(allValidMoves.size());
            board.applyMoveIfValid(allValidMoves.get(chosen));

            winner = winsRandomGame(board, random);

            board.undoLastMove();
        }

        return winner;
    }

    @Override
    public String getIdentifier() {
        return IDENTIFIER;
    }

    @Override
    public void learnFromChange(BoardStatus board) {
        // Nothing to do
    }

    @Override
    public void newGame(BoardStatus board) {
        // Don't want to reset the board
    }

    @Override
    public void saveInternalPlayerSpecification(PrintStream logger) {
        // Don't have any additional identifiers
    }

    @Override
    public void saveParameters(PrintStream printStream) {
        printStream.println(numberOfIterations);
    }
}
