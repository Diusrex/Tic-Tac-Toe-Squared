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
package com.diusrex.tictactoe.android;

import android.view.View;
import android.widget.ImageView;

import com.diusrex.tictactoe.data_structures.BoxPosition;
import com.diusrex.tictactoe.data_structures.Move;
import com.diusrex.tictactoe.data_structures.Player;
import com.diusrex.tictactoe.data_structures.SectionPosition;
import com.diusrex.tictactoe.logic.GridLists;

public class SelectedSectionOwner {
    private final GameEventHandler handler;
    private SectionOwner sectionOwner;
    private SectionPosition sectionPosition;
    private BoxPositionSelectedListener currentListener;

    SelectedSectionOwner(GameEventHandler handler) {
        this.handler = handler;
    }

    public void selectedSectionChanged(SectionOwner newSectionOwner, SectionPosition newSectionPosition) {
        sectionOwner = newSectionOwner;
        sectionPosition = newSectionPosition;

        updateSectionOwnerOnClick();
    }

    //@Override
    public void setUpOnClick(final Player currentPlayer) {
        currentListener = new BoxPositionSelectedListener(currentPlayer);

        updateSectionOwnerOnClick();
    }

    public void updateSectionOwnerOnClick() {
        for (BoxPosition position : GridLists.getAllStandardBoxPositions()) {
            ImageView imageView = sectionOwner.getBox(position);
            imageView.setTag(position);
            imageView.setOnClickListener(currentListener);
        }
    }

    class BoxPositionSelectedListener implements View.OnClickListener {
        private final Player player;

        BoxPositionSelectedListener(Player player) {
            this.player = player;
        }

        @Override
        public void onClick(View v) {
            Move move = Move.make(sectionPosition, (BoxPosition) v.getTag(), player);
            handler.moveChosen(move);
        }
    }
}
