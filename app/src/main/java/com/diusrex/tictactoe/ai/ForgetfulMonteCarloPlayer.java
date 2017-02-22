package com.diusrex.tictactoe.ai;

import com.diusrex.tictactoe.data_structures.*;
import com.diusrex.tictactoe.data_structures.position.BoxPosition;
import com.diusrex.tictactoe.data_structures.position.SectionPosition;
import com.diusrex.tictactoe.logic.GridLists;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/*
 *  This is a forgetful player because it only remembers how well the currently possible moves are.
 *  Means that it will not be quite as strong, because it starts from a blank slate each time it runs.
 *
 */
public class ForgetfulMonteCarloPlayer extends AIPlayer {
    public static final String IDENTIFIER = "ForgetfulMonteCarlo";
    private static final double EPSILON = 1e-6;
    private int numberOfIterations;

    public static ForgetfulMonteCarloPlayer getStandardPlayer() {
        return new ForgetfulMonteCarloPlayer(100000);
    }

    public ForgetfulMonteCarloPlayer(int numberOfIterations) {
        super();
        this.numberOfIterations = numberOfIterations;
    }

    @Override
    protected Move choosePosition(BoardStatus board) {
        return getBestValidMove(board);
    }

    protected Random getRandom() {
        return new Random();
    }

    private Move getBestValidMove(BoardStatus board) {
        Player currentPlayer = board.getNextPlayer();

        Random random = getRandom();
        final List<Move> allValidMoves = getAllValidMoves(board);
        final int numMoves = allValidMoves.size();

        if (numMoves == 1) {
            return allValidMoves.get(0);
        }

        List<Integer> numWins = new ArrayList<Integer>(Collections.nCopies(numMoves, 1));
        List<Integer> numPlays = new ArrayList<Integer>(Collections.nCopies(numMoves, 1));
        List<Double> winPercent = new ArrayList<Double>(Collections.nCopies(numMoves, 1.0));
        Double logValue = Math.log(1);

        for (int timesRun = 0; timesRun < numberOfIterations; ++timesRun) {
            int bestMoveIndex = getBestMoveIndex(numMoves, numWins, numPlays, winPercent, logValue, random);

            board.applyMoveIfValid(allValidMoves.get(bestMoveIndex));
            boolean won = winsRandomGame(board, random, currentPlayer);
            board.undoLastMove();

            numPlays.set(bestMoveIndex, numPlays.get(bestMoveIndex) + 1);
            if (won) {
                numWins.set(bestMoveIndex, numWins.get(bestMoveIndex) + 1);
            }
            winPercent.set(bestMoveIndex, 1.0 * numWins.get(bestMoveIndex) / numPlays.get(bestMoveIndex));

            logValue = Math.log(timesRun);
        }

        int bestMoveIndex = getBestMoveIndex(numMoves, numWins, numPlays, winPercent, logValue, random);
        return allValidMoves.get(bestMoveIndex);
    }

    private int getBestMoveIndex(int numMoves, List<Integer> numWins, List<Integer> numPlays, List<Double> firstValue,
                                 Double logValue, Random random) {
        int bestIndex = 0;
        double valueOfBest = calculateMoveValue(bestIndex, numWins, numPlays, firstValue, logValue, random);

        for (int index = 1; index < numMoves; ++index) {
            double currentVal = calculateMoveValue(index, numWins, numPlays, firstValue, logValue, random);
            if (currentVal > valueOfBest) {
                bestIndex = index;
                valueOfBest = currentVal;
            }
        }

        return bestIndex;
    }

    private double calculateMoveValue(int index, List<Integer> numWins, List<Integer> numPlays,
                                      List<Double> firstValue, Double logValue, Random random) {
        int numberPlaysForMove = numPlays.get(index);
        return 1.0 * numWins.get(index) / numberPlaysForMove + Math.sqrt(logValue) / numberPlaysForMove
                + random.nextDouble() * EPSILON;
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
        List<Move> allValidMoves = new ArrayList<Move>();

        for (SectionPosition section : GridLists.getAllStandardSections()) {
            addValidMovesInSection(board, section, allValidMoves);
        }

        return allValidMoves;
    }

    private List<Move> getValidMovesInRequiredSection(BoardStatus board) {
        List<Move> allValidMoves = new ArrayList<Move>();
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

    private boolean winsRandomGame(BoardStatus board, Random random, Player wantedPlayerToWin) {
        Player winner = board.getWinner();
        if (winner != Player.Unowned) {
            return winner == wantedPlayerToWin;
        }

        List<Move> allValidMoves = getAllValidMoves(board);
        if (allValidMoves.size() == 0) {
            return false;
        }

        int chosen = random.nextInt(allValidMoves.size());
        board.applyMoveIfValid(allValidMoves.get(chosen));

        boolean wins = winsRandomGame(board, random, wantedPlayerToWin);

        board.undoLastMove();

        return wins;
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
        // Nothing to do
    }

    @Override
    public void saveInternalState(PrintStream logger) {
        logger.println("ForgetfulMonteCarloPlayer");
        logger.println(numberOfIterations);
    }
}
