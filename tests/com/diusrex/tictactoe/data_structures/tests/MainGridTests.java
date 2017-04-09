package com.diusrex.tictactoe.data_structures.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.diusrex.tictactoe.data_structures.LinesFormed;
import com.diusrex.tictactoe.data_structures.Move;
import com.diusrex.tictactoe.data_structures.Player;
import com.diusrex.tictactoe.data_structures.grid.MainGrid;
import com.diusrex.tictactoe.data_structures.grid.SectionGrid;
import com.diusrex.tictactoe.data_structures.position.BoxPosition;
import com.diusrex.tictactoe.data_structures.position.SectionPosition;
import com.diusrex.tictactoe.logic.StandardTicTacToeEngine;
import com.diusrex.tictactoe.logic.TicTacToeEngine;

// TODO: Figure out how much can be shared with SectionGridTests
public class MainGridTests {
    // Just testing the 
    
    MainGridExposer mainGrid = new MainGridExposer(new StandardTicTacToeEngine());
    
    LinesFormed fromP1Perspective = new LinesFormed(Player.Player_1);
    LinesFormed fromP2Perspective = new LinesFormed(Player.Player_2);
    
    @Test
    public void getLinesFormedEmptyTest() {
        
        mainGrid.getLinesFormed(fromP1Perspective);
        mainGrid.getLinesFormed(fromP2Perspective);
        
        checkPerspectivesAreOpposite();
        
        assertEquals(8, fromP1Perspective.unownedButWinnableForMain);
        assertEquals(8, fromP2Perspective.unownedButWinnableForMain);
        
        // Should be winnable for both players
        assertTrue(mainGrid.canBeWon());
        assertTrue(mainGrid.canBeWonByPlayer(Player.Player_1));
        assertTrue(mainGrid.canBeWonByPlayer(Player.Player_2));
    }

    @Test
    public void getLinesFormedSomeOwnershipTest() {
        // 1  1  0
        // 1  0  0
        // 2  0  0
        mainGrid.changeOwnerOfSection(SectionPosition.make(0, 0), Player.Player_1);
        mainGrid.changeOwnerOfSection(SectionPosition.make(0, 1), Player.Player_1);
        mainGrid.changeOwnerOfSection(SectionPosition.make(1, 0), Player.Player_1);

        // Blocks one of the lines for player 1
        mainGrid.changeOwnerOfSection(SectionPosition.make(0, 2), Player.Player_2);
        
        
        mainGrid.getLinesFormed(fromP1Perspective);
        mainGrid.getLinesFormed(fromP2Perspective);
        
        checkPerspectivesAreOpposite();

        assertEquals(3, fromP1Perspective.oneFormedForMain);
        assertEquals(1, fromP1Perspective.twoFormedForMain);
        assertEquals(1, fromP1Perspective.twoInRowWasBlockedForMain);

        assertEquals(2, fromP2Perspective.oneFormedForMain);
        
        assertEquals(1, fromP1Perspective.unownedButWinnableForMain);
        assertEquals(1, fromP2Perspective.unownedButWinnableForMain);
        
        // Should be winnable for both players
        assertTrue(mainGrid.canBeWon());
        assertTrue(mainGrid.canBeWonByPlayer(Player.Player_1));
        assertTrue(mainGrid.canBeWonByPlayer(Player.Player_2));
    }
    
    @Test
    public void getLinesFormedSomeOwnershipAndBlockedTest() {
        // 1   1  ?2
        // 1   0   0
        // 2  ?1  ?2
        //   ?1 means the section can only be won by P1
        //   ?2 means the section can only be won by P1
        //   - means the section can't be won by either player
        
        // Both has two possible win lines, but the lines are quite different.

        mainGrid.changeOwnerOfSection(SectionPosition.make(0, 0), Player.Player_1);
        mainGrid.changeOwnerOfSection(SectionPosition.make(0, 1), Player.Player_1);
        mainGrid.changeOwnerOfSection(SectionPosition.make(1, 0), Player.Player_1);

        mainGrid.changeOwnerOfSection(SectionPosition.make(0, 2), Player.Player_2);


        mainGrid.changeSectionToBeUnwinnableForPlayer(SectionPosition.make(1, 2), Player.Player_2);
        
        mainGrid.changeSectionToBeUnwinnableForPlayer(SectionPosition.make(2, 0), Player.Player_1);
        mainGrid.changeSectionToBeUnwinnableForPlayer(SectionPosition.make(2, 2), Player.Player_1);
        
        mainGrid.getLinesFormed(fromP1Perspective);
        mainGrid.getLinesFormed(fromP2Perspective);
        
        checkPerspectivesAreOpposite();

        assertEquals(0, fromP1Perspective.unownedButWinnableForMain);
        assertEquals(2, fromP1Perspective.oneFormedForMain);
        assertEquals(1, fromP1Perspective.oneInRowWasBlockedForMain);
        assertEquals(0, fromP1Perspective.twoFormedForMain);
        assertEquals(2, fromP1Perspective.twoInRowWasBlockedForMain);
        

        assertEquals(1, fromP2Perspective.unownedButWinnableForMain);
        assertEquals(1, fromP2Perspective.oneFormedForMain);
        assertEquals(1, fromP2Perspective.oneInRowWasBlockedForMain);
        
        // Should be winnable for both players
        assertTrue(mainGrid.canBeWon());
        assertTrue(mainGrid.canBeWonByPlayer(Player.Player_1));
        assertTrue(mainGrid.canBeWonByPlayer(Player.Player_2));
    }

    
    @Test
    public void getLinesFormedSomeOwnershipAndBlockedUndoTest() {
        // Initial setup is the same as getLinesFormedSomeOwnershipAndBlockedTest:
        // 1   1  ?2
        // 1   0   0
        // 2  ?1  ?2
        //   ?1 means the section can only be won by P1
        //   ?2 means the section can only be won by P1
        //   - means the section can't be won by either player
        
        // but then undo some moves:
        //   Change it so (0, 2) isn't owned by player 2
        //   Change it so (1, 2) isn't blocked for player 2
        //       Means that player 2 can win the now empty line on bottom
        
        // Both has two possible win lines, but the lines are quite different.

        mainGrid.changeOwnerOfSection(SectionPosition.make(0, 0), Player.Player_1);
        mainGrid.changeOwnerOfSection(SectionPosition.make(0, 1), Player.Player_1);
        mainGrid.changeOwnerOfSection(SectionPosition.make(1, 0), Player.Player_1);

        mainGrid.changeOwnerOfSection(SectionPosition.make(0, 2), Player.Player_2);


        mainGrid.changeSectionToBeUnwinnableForPlayer(SectionPosition.make(1, 2), Player.Player_2);
        
        mainGrid.changeSectionToBeUnwinnableForPlayer(SectionPosition.make(2, 0), Player.Player_1);
        mainGrid.changeSectionToBeUnwinnableForPlayer(SectionPosition.make(2, 2), Player.Player_1);
        
        // Undo for sections (0, 2) and (1, 2)
        mainGrid.undoSectionState(SectionPosition.make(0, 2));
        mainGrid.undoSectionState(SectionPosition.make(1, 2));
        
        mainGrid.getLinesFormed(fromP1Perspective);
        mainGrid.getLinesFormed(fromP2Perspective);
        
        checkPerspectivesAreOpposite();

        assertEquals(0, fromP1Perspective.unownedButWinnableForMain);
        assertEquals(2, fromP1Perspective.oneFormedForMain);
        assertEquals(1, fromP1Perspective.oneInRowWasBlockedForMain);
        assertEquals(1, fromP1Perspective.twoFormedForMain);
        assertEquals(1, fromP1Perspective.twoInRowWasBlockedForMain);
        

        assertEquals(3, fromP2Perspective.unownedButWinnableForMain);
        assertEquals(0, fromP2Perspective.oneFormedForMain);
        assertEquals(0, fromP2Perspective.oneInRowWasBlockedForMain);
        
        // Should be winnable for both players
        assertTrue(mainGrid.canBeWon());
        assertTrue(mainGrid.canBeWonByPlayer(Player.Player_1));
        assertTrue(mainGrid.canBeWonByPlayer(Player.Player_2));
    }
    
    @Test
    public void getLinesFormedPlayer2OnlyAbleToWinTest() {
        // 1   1  ?2
        // 1   0  ?2
        // 2  ?2  ?2
        //   ?1 means the section can only be won by P1
        //   ?2 means the section can only be won by P1
        
        // Only Player 2 has actual possibility to win

        mainGrid.changeOwnerOfSection(SectionPosition.make(0, 0), Player.Player_1);
        mainGrid.changeOwnerOfSection(SectionPosition.make(0, 1), Player.Player_1);
        mainGrid.changeOwnerOfSection(SectionPosition.make(1, 0), Player.Player_1);

        mainGrid.changeOwnerOfSection(SectionPosition.make(0, 2), Player.Player_2);


        
        mainGrid.changeSectionToBeUnwinnableForPlayer(SectionPosition.make(2, 0), Player.Player_1);
        mainGrid.changeSectionToBeUnwinnableForPlayer(SectionPosition.make(2, 1), Player.Player_1);
        mainGrid.changeSectionToBeUnwinnableForPlayer(SectionPosition.make(1, 2), Player.Player_1);
        mainGrid.changeSectionToBeUnwinnableForPlayer(SectionPosition.make(2, 2), Player.Player_1);
        
        mainGrid.getLinesFormed(fromP1Perspective);
        mainGrid.getLinesFormed(fromP2Perspective);
        
        checkPerspectivesAreOpposite();
        
        assertTrue(mainGrid.canBeWon());
        assertFalse(mainGrid.canBeWonByPlayer(Player.Player_1));
        assertTrue(mainGrid.canBeWonByPlayer(Player.Player_2));
    }

    @Test
    public void getLinesFormedPlayer1OnlyAbleToWinTest() {
        // 1   1  ?1
        // 1   0   0
        // 2  ?1   0
        //   ?1 means the section can only be won by P1
        //   ?2 means the section can only be won by P1
        
        // Only Player 2 has actual possibility to win

        mainGrid.changeOwnerOfSection(SectionPosition.make(0, 0), Player.Player_1);
        mainGrid.changeOwnerOfSection(SectionPosition.make(0, 1), Player.Player_1);
        mainGrid.changeOwnerOfSection(SectionPosition.make(1, 0), Player.Player_1);

        mainGrid.changeOwnerOfSection(SectionPosition.make(0, 2), Player.Player_2);
        
        mainGrid.changeSectionToBeUnwinnableForPlayer(SectionPosition.make(2, 0), Player.Player_2);
        mainGrid.changeSectionToBeUnwinnableForPlayer(SectionPosition.make(1, 2), Player.Player_2);
        
        mainGrid.getLinesFormed(fromP1Perspective);
        mainGrid.getLinesFormed(fromP2Perspective);
        
        checkPerspectivesAreOpposite();
        
        // Should be winnable for just player 2
        assertTrue(mainGrid.canBeWon());
        assertTrue(mainGrid.canBeWonByPlayer(Player.Player_1));
        assertFalse(mainGrid.canBeWonByPlayer(Player.Player_2));
    }
    
    // Now, also have some of the sections be unownable for one, or both of the players
    // Should also requireEmpty lines to not be different between the two of them.

    // TODO: Instead, set the sections to be a FakeSections
    // Implements SectionGrid, but changes owner to be at position 0, 0
    
    private void checkPerspectivesAreOpposite() {
        assertEquals(fromP1Perspective.unownedButWinnableForMain, fromP2Perspective.unownedButWinnableForOther);
        assertEquals(fromP1Perspective.unownedButWinnableForOther, fromP2Perspective.unownedButWinnableForMain);
        
        assertEquals(fromP1Perspective.oneFormedForMain, fromP2Perspective.oneFormedForOther);
        assertEquals(fromP1Perspective.oneFormedForOther, fromP2Perspective.oneFormedForMain);

        assertEquals(fromP1Perspective.twoFormedForMain, fromP2Perspective.twoFormedForOther);
        assertEquals(fromP1Perspective.twoFormedForOther, fromP2Perspective.twoFormedForMain);

        assertEquals(fromP1Perspective.twoInRowWasBlockedForMain, fromP2Perspective.twoInRowWasBlockedForOther);
        assertEquals(fromP1Perspective.twoInRowWasBlockedForOther, fromP2Perspective.twoInRowWasBlockedForMain);
    }
    
    
    public class MainGridExposer extends MainGrid {
        
        SimpleSectionGrid[][] sectionsGrid = new SimpleSectionGrid[3][3];

        public MainGridExposer(TicTacToeEngine engine) {
            super(engine);
            
            // Overwrite the sections created by MainGrid
            for (int x = 0; x < sections.length; ++x) {
                for (int y = 0; y < sections.length; ++y) {

                    sectionsGrid[x][y] = new SimpleSectionGrid(new StandardTicTacToeEngine());
                    sections[x][y] = sectionsGrid[x][y];
                }
            }
        }
        
        public void changeOwnerOfSection(SectionPosition section, Player wantedPlayer) {
            sectionsGrid[section.getGridX()][section.getGridY()].changeOwnerOnSet = true;
            setBoxOwner(section, BoxPosition.make(0, 0), wantedPlayer);
        }
        
        public void changeSectionToBeUnwinnable(SectionPosition section) {
            // Set it for both players
            changeSectionToBeUnwinnableForPlayer(section, Player.Player_1);
            changeSectionToBeUnwinnableForPlayer(section, Player.Player_2);
        }
        
        public void changeSectionToBeUnwinnableForPlayer(SectionPosition section, Player player) {
            sectionsGrid[section.getGridX()][section.getGridY()].changeToUnWinnableOnSet = true;
            setBoxOwner(section, BoxPosition.make(0, 0), player);
        }
        
        public void undoSectionState(SectionPosition section) {
            sectionsGrid[section.getGridX()][section.getGridY()].undoOwnerAndUnWinnableOnSet = true;
            Player owner = sectionsGrid[section.getGridX()][section.getGridY()].getGridOwner();
            if (owner != Player.Unowned) {
                // Undoing ownership of the grid
                Move move = Move.make(section, BoxPosition.make(0, 0), owner);
                undoMove(move);
            } else {
                // Undoing blocked player for grid, so the player who made the move doesn't matter
                Move move = Move.make(section, BoxPosition.make(0, 0), Player.Player_1);
                undoMove(move);
            }
        }
    }
    
    // Will mostly use SectionGrids original functions.
    // Allows easy changing of the owner and if it is winnable.s
    public class SimpleSectionGrid extends SectionGrid {

        public SimpleSectionGrid(TicTacToeEngine engine) {
            super(engine);
        }
        
        public boolean changeOwnerOnSet = false;
        public boolean changeToUnWinnableOnSet = false;
        public boolean undoOwnerAndUnWinnableOnSet = false;

        private Player owner = Player.Unowned;
        private boolean winnableForP1 = true;
        private boolean winnableForP2 = true;
        
        @Override
        public void setPointOwner(BoxPosition pos, Player player) {
            if (changeOwnerOnSet) {
                owner = player;
                winnableForP1 = false;
                winnableForP2 = false;
                changeOwnerOnSet = changeToUnWinnableOnSet = false;
            } else if (changeToUnWinnableOnSet) {
                if (player == Player.Player_1) {
                    winnableForP1 = false;
                } else if (player == Player.Player_2) {
                    winnableForP2 = false;
                }
                changeToUnWinnableOnSet = false;
            } else if (undoOwnerAndUnWinnableOnSet) {
                if (player == Player.Unowned) {
                    owner = Player.Unowned;
                    winnableForP1 = true;
                    winnableForP2 = true;
                }
            }
        }
        
        @Override
        public Player getGridOwner() {
            return owner;
        }
        
        
        @Override
        public boolean canBeWon() {
            return winnableForP1 || winnableForP2;
        }
        
        @Override
        public boolean canBeWonByPlayer(Player player) {
            if (player == Player.Player_1) {
                return winnableForP1;
            } else if (player == Player.Player_2) {
                return winnableForP2;
            } else {
                return false;
            }
        }
    }
}
