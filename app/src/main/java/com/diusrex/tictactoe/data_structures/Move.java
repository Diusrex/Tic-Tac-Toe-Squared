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

public class Move {
    public static final int SIZE_OF_SAVED_MOVE = 5;
    private static final int START_OF_BOX_POSITION = 2;
    private static final int START_OF_PLAYER = 4;

    private final SectionPosition section;
    private final BoxPosition box;
    private final Player player;

    public static Move fromString(String string) {
        SectionPosition section = SectionPosition.fromString(string.substring(0, START_OF_BOX_POSITION));
        BoxPosition box = BoxPosition.fromString(string.substring(START_OF_BOX_POSITION, START_OF_PLAYER));
        Player player = Player.fromString(string.substring(START_OF_PLAYER, SIZE_OF_SAVED_MOVE));

        return new Move(section, box, player);
    }

    public Move(SectionPosition section, BoxPosition box, Player madeBy) {
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
