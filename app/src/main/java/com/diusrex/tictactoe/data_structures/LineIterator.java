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



public class LineIterator {
    private final Line line;
    private final BoxPosition[] allPositions;
    private final int lineSize;

    public LineIterator(Line line) {
        this.line = line;
        BoxPosition difference = line.getEnd().decreaseBy(line.getStart());

        int xChange = 0;
        if (difference.getGridX() > 0)
            xChange = 1;
        else if (difference.getGridX() < 0)
            xChange = -1;

        int yChange = 0;
        if (difference.getGridY() > 0)
            yChange = 1;
        else if (difference.getGridY() < 0)
            yChange = -1;

        BoxPosition increase = BoxPosition.make(xChange, yChange);
        lineSize = 1 + Math.max((xChange != 0) ? difference.getGridX() / xChange : 0,
                            (yChange != 0) ? difference.getGridY() / yChange : 0);
        
        allPositions = new BoxPosition[lineSize];
        
        setUpPositions(increase);
        
    }
    
    private void setUpPositions(BoxPosition increase) {
        allPositions[0] = line.getStart();
        for (int i = 1; i < lineSize; ++i) {
            allPositions[i] = allPositions[i - 1].increaseBy(increase);
        }
    }
    
    public boolean isDone(int iterationNum) {
        return iterationNum >= lineSize;
    }

    public Position getCurrent(int iterationNum) {
        return allPositions[iterationNum];
    }

    public Line getLine() {
        return line;
    }
}
