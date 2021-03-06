/*
 * Copyright 2015 Morgan Redshaw
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.diusrex.tictactoe.android.players;

import com.diusrex.tictactoe.android.SelectedSectionOwner;
import com.diusrex.tictactoe.data_structures.BoardStatus;
import com.diusrex.tictactoe.data_structures.Player;

public class HumanAndroidPlayer implements AndroidPlayerController {
    private final SelectedSectionOwner selectedSectionOwner;
    private final Player currentPlayer;

    public HumanAndroidPlayer(SelectedSectionOwner selectedSectionOwner, Player currentPlayer) {
        this.selectedSectionOwner = selectedSectionOwner;
        this.currentPlayer = currentPlayer;
    }

    @Override
    public void promptForMove(BoardStatus board, MoveListener listener) {
        selectedSectionOwner.setUpOnClick(currentPlayer);
    }

    @Override
    public void undoWasPressed() {
    }

    @Override
    public boolean mayUndo() {
        return true;
    }
}
