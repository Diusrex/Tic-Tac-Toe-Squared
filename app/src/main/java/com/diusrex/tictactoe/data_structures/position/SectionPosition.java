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


public class SectionPosition implements Position {
    static private final SectionPosition[][] standardPositions = new SectionPosition[GridConstants.SIZE_OF_SECTION][GridConstants.SIZE_OF_SECTION];

    static private boolean initialized = false;

    public static SectionPosition make(int x, int y) {
        if (!initialized) {
            initialize();
            initialized = true;
        }

        if (x >= 0 && x < GridConstants.SIZE_OF_SECTION && y >= 0 && y < GridConstants.SIZE_OF_SECTION)
            return standardPositions[x][y];

        else
            return new SectionPosition(x, y);
    }

    public static SectionPosition fromString(String string) {
        int totalValue = Integer.parseInt(string);
        int x = totalValue / 10;
        int y = totalValue % 10;

        return SectionPosition.make(x, y);
    }
    
    private static void initialize() {
        for (int y = 0; y < GridConstants.SIZE_OF_SECTION; ++y) {
            for (int x = 0; x < GridConstants.SIZE_OF_SECTION; ++x) {
                standardPositions[x][y] = new SectionPosition(x, y);
            }
        }
    }

    private final int x;
    private final int y;

    private SectionPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getGridX() {
        return x;
    }

    public int getGridY() {
        return y;
    }

    public SectionPosition increaseBy(SectionPosition increase) {
        return make(x + increase.x, y + increase.y);
    }
    
    @Override
    public String toString() {
        return String.format("%d%d", x, y);
    }
}
