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

import com.diusrex.tictactoe.data_structures.BasicPosition;
import com.diusrex.tictactoe.data_structures.BoxPosition;
import com.diusrex.tictactoe.data_structures.Line;
import com.diusrex.tictactoe.data_structures.LineIterator;
import com.diusrex.tictactoe.data_structures.Position;
import com.diusrex.tictactoe.data_structures.SectionPosition;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/*
 *  All non line Lists go emulate going through with x in inner for loop
 */
public class GridLists {
    private static List<Position> allPositions;
    private static List<BoxPosition> allBoxesInSection;
    private static List<SectionPosition> allSections;
    private static List<LineIterator> allLineIterators;
    private static List<Line> allLines;

    private static final int NUM_LINES_PER_SIDE = 3;
    private static final int LINE_SIZE = 3;

    private GridLists() {
    }

    public static List<Position> getAllStandardPositions() {
        if (allPositions == null) {
            allPositions = new ArrayList<>();

            for (int y = 0; y < GridConstants.SIZE_OF_SECTION; ++y) {
                for (int x = 0; x < GridConstants.SIZE_OF_SECTION; ++x) {
                    allPositions.add(new BasicPosition(x, y));
                }
            }
        }

        return Collections.unmodifiableList(allPositions);
    }

    public static List<BoxPosition> getAllStandardBoxPositions() {
        if (allBoxesInSection == null) {
            allBoxesInSection = new ArrayList<>();

            for (int y = 0; y < GridConstants.SIZE_OF_SECTION; ++y) {
                for (int x = 0; x < GridConstants.SIZE_OF_SECTION; ++x) {
                    allBoxesInSection.add(BoxPosition.make(x, y));
                }
            }
        }

        return Collections.unmodifiableList(allBoxesInSection);
    }

    public static List<SectionPosition> getAllStandardSections() {
        if (allSections == null) {
            allSections = new ArrayList<>();

            for (int y = 0; y < GridConstants.SIZE_OF_SECTION; ++y) {
                for (int x = 0; x < GridConstants.SIZE_OF_SECTION; ++x) {
                    allSections.add(SectionPosition.make(x, y));
                }
            }
        }

        return Collections.unmodifiableList(allSections);
    }

    public static List<LineIterator> getAllLineIterators() {
        if (allLineIterators == null) {
            List<Line> allLines = getAllLines();
            allLineIterators = new ArrayList<>();

            for (Line line : allLines) {
                allLineIterators.add(new LineIterator(line));
            }
        }

        return Collections.unmodifiableList(allLineIterators);
    }

    public static List<Line> getAllLines() {
        if (allLines == null) {
            allLines = new ArrayList<>();

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