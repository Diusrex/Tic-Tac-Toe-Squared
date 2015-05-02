/*
 * Copyright 2015 Morgan Redshaw
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 **/
package com.diusrex.tictactoe.data_structures;

import com.diusrex.tictactoe.logic.GridConstants;
import com.diusrex.tictactoe.logic.GridLists;

public class Move {
    public static final int SIZE_OF_SAVED_MOVE = 5;
    private static final int START_OF_BOX_POSITION = 2;
    private static final int START_OF_PLAYER = 4;
    
    private static Move[][][][][] allPossibleMoves;

    private final SectionPosition section;
    private final BoxPosition box;
    private final Player player;

    public static Move fromString(String string) {
        SectionPosition section = SectionPosition.fromString(string.substring(0, START_OF_BOX_POSITION));
        BoxPosition box = BoxPosition.fromString(string.substring(START_OF_BOX_POSITION, START_OF_PLAYER));
        Player player = Player.fromString(string.substring(START_OF_PLAYER, SIZE_OF_SAVED_MOVE));

        return make(section, box, player);
    }
    
    public static void init() {
        int secSize = GridConstants.NUMBER_OF_SECTIONS_PER_SIDE;
        int boxSize = GridConstants.SIZE_OF_SECTION;
        allPossibleMoves = new Move[secSize][secSize][boxSize][boxSize][2];
        
        for (SectionPosition section : GridLists.getAllStandardSections()) {
            Move[][][] currentSectionMoves = allPossibleMoves[section.getGridX()][section.getGridY()];
            
            for (BoxPosition box : GridLists.getAllStandardBoxPositions()) {
                Move[] currentBoxMoves = currentSectionMoves[box.getGridX()][box.getGridY()];
                
                currentBoxMoves[0] = new Move(section, box, Player.Player_1);
                currentBoxMoves[1] = new Move(section, box, Player.Player_2);
            }
        }
    }
    
    public static Move make(SectionPosition section, BoxPosition box, Player player) {
        if (allPossibleMoves == null) {
            init();
        }
        
        if (isInBounds(section, GridConstants.NUMBER_OF_SECTIONS_PER_SIDE) &&
                isInBounds(box, GridConstants.SIZE_OF_SECTION) &&
                player != Player.Unowned)
            return allPossibleMoves[section.getGridX()][section.getGridY()][box.getGridX()][box.getGridY()][(player == Player.Player_1) ? 0 : 1];
        
        return new Move(section, box, player);
    }

    private static boolean isInBounds(Position pos, int sizeForPos) {
        return pos.getGridX() >= 0 && pos.getGridX() < sizeForPos
                && pos.getGridY() >= 0 && pos.getGridY() < sizeForPos;
    }

    private Move(SectionPosition section, BoxPosition box, Player madeBy) {
        this.section = section;
        this.box = box;
        this.player = madeBy;
    }

    public SectionPosition getSection() {
        return section;
    }

    public BoxPosition getBox() {
        return box;
    }

    public Player getPlayer() {
        return player;
    }

    @Override
    public String toString() {
        return section.toString() + box.toString() + player.toString();
    }
}
