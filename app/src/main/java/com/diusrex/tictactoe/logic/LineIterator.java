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

public class LineIterator {
    private final Line line;
    private final BoxPosition increase;

    private BoxPosition currentPosition;

    public LineIterator(Line line) {
        this.line = line;
        BoxPosition difference = line.getEnd().decreaseBy(line.getStart());
        
        int xChange = 0;
        if (difference.getX() > 0)
            xChange = 1;
        else if (difference.getX() < 0)
            xChange = -1;
        
        int yChange = 0;
        if (difference.getY() > 0)
            yChange = 1;
        else if (difference.getY() < 0)
            yChange = -1;
        
        increase = BoxPosition.make(xChange, yChange);
    }

    public LineIterator(BoxPosition startPos, BoxPosition increase) {
        line = new Line(startPos, increase);
        this.increase = increase;
    }

    public void reset() {
        currentPosition = line.getStart();
    }

    public void next() {
        currentPosition = currentPosition.increaseBy(increase);
    }

    public boolean isDone(BoxPosition sizeOfBoard) {
        return currentPosition.getX() < 0 || currentPosition.getX() >= sizeOfBoard.getX()
                || currentPosition.getY() < 0 || currentPosition.getY() >= sizeOfBoard.getY();
    }
    
    public BoxPosition getCurrent() {
        return currentPosition;
    }
}
