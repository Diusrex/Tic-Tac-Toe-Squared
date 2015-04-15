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
import com.diusrex.tictactoe.data_structures.SectionPosition;

public class SelectedSectionOwner extends SectionOwner {
    static private final int GRID_LINE_WIDTH = 30;
    private final GameEventHandler handler;
    private final SectionPosition sectionPosition;


    SelectedSectionOwner(SectionPosition sectionPosition, MyGridLayout grid, GameEventHandler handler) {
        super(sectionPosition, grid);
        this.sectionPosition = sectionPosition;
        this.handler = handler;
    }

    @Override
    protected int getLineWidth() {
        return GRID_LINE_WIDTH;
    }

    @Override
    protected void setUpSpecialInformation(ImageView imageView, BoxPosition posInSection) {
        imageView.setTag(posInSection);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handler.boxSelected(sectionPosition, (BoxPosition) v.getTag());
            }
        });
    }
}
