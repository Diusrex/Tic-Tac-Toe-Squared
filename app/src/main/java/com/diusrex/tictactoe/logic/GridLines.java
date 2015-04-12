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
package com.diusrex.tictactoe.logic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class GridLines {
    private static List<LineIterator> allLineIterators;
    private static List<Line> allLines;

    private static final int NUM_LINES_PER_SIDE = 3;
    private static final int LINE_SIZE = 3;

    private GridLines() {
    }

    public static List<LineIterator> getAllLineIterators() {
        if (allLineIterators == null) {
            List<Line> allLines = getAllLines();
            allLineIterators = new ArrayList<LineIterator>();

            for (Line line : allLines) {
                allLineIterators.add(new LineIterator(line));
            }
        }

        return allLineIterators;
    }

    public static List<Line> getAllLines() {
        if (allLines == null) {
            allLines = new ArrayList<Line>();

            setUpHorizontalLines();
            setUpVerticalLines();
            setUpDiagonalLines();
        }

        return Collections.unmodifiableList(allLines);
    }

    private static void setUpHorizontalLines() {
        BoxPosition start = BoxPosition.make(0, 0);
        BoxPosition end = BoxPosition.make(LINE_SIZE - 1, 0);
        BoxPosition increase = BoxPosition.make(0, 1);

        for (int i = 0; i < NUM_LINES_PER_SIDE; ++i, start = start.increaseBy(increase), end = end.increaseBy(increase)) {
            allLines.add(new Line(start, end));
        }
    }

    private static void setUpVerticalLines() {
        BoxPosition start = BoxPosition.make(0, 0);
        BoxPosition end = BoxPosition.make(0, LINE_SIZE - 1);
        BoxPosition increase = BoxPosition.make(1, 0);

        for (int i = 0; i < NUM_LINES_PER_SIDE; ++i, start = start.increaseBy(increase), end = end.increaseBy(increase)) {
            allLines.add(new Line(start, end));
        }
    }

    private static void setUpDiagonalLines() {
        allLines.add(new Line(BoxPosition.make(0, 0), BoxPosition.make(2, 2)));
        allLines.add(new Line(BoxPosition.make(0, 2), BoxPosition.make(2, 0)));
    }
}
