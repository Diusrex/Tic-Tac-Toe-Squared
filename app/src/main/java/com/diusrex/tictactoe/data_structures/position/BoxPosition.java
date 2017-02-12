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
package com.diusrex.tictactoe.data_structures.position;

import com.diusrex.tictactoe.logic.GridConstants;




// Immutable data structure, which does not need to be allocated
public class BoxPosition implements Position {
    static private final BoxPosition[][] standardPositions = new BoxPosition[GridConstants.NUMBER_OF_BOXES_PER_SIDE][GridConstants.NUMBER_OF_BOXES_PER_SIDE];
    static private boolean initialized = false;
    
    public static BoxPosition make(int x, int y) {
        if (!initialized) {
            initialize();
            initialized = true;
        }

        if (x >= 0 && x < GridConstants.NUMBER_OF_BOXES_PER_SIDE && y >= 0 && y < GridConstants.NUMBER_OF_BOXES_PER_SIDE)
            return standardPositions[x][y];

        else
            return new BoxPosition(x, y);
    }
    
    public static BoxPosition fromString(String string) {
        int totalValue = Integer.parseInt(string);
        int x = totalValue / 10;
        int y = totalValue % 10;

        return BoxPosition.make(x, y);
    }

    private static void initialize() {
        for (int y = 0; y < GridConstants.NUMBER_OF_BOXES_PER_SIDE; ++y) {
            for (int x = 0; x < GridConstants.NUMBER_OF_BOXES_PER_SIDE; ++x) {
                standardPositions[x][y] = new BoxPosition(x, y);
            }
        }
    }

    private final int x;
    private final int y;

    private BoxPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getGridX() {
        return x;
    }

    public int getGridY() {
        return y;
    }

    public BoxPosition increaseBy(BoxPosition increase) {
        return increaseBy(increase.x, increase.y);
    }

    public BoxPosition increaseBy(int x, int y) {
        return make(this.x + x, this.y + y);
    }

    public BoxPosition decreaseBy(BoxPosition decrease) {
        return increaseBy(-decrease.x, -decrease.y);
    }

    public BoxPosition decreaseBy(int x, int y) {
        return increaseBy(-x, -y);
    }

    @Override
    public String toString() {
        return String.format("%d%d", x, y);
    }
}
