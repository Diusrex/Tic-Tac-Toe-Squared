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

import com.diusrex.tictactoe.data_structures.position.BoxPosition;



public class Line {
    private final BoxPosition start;
    private final BoxPosition end;

    public Line(BoxPosition start, BoxPosition end) {
        this.start = start;
        this.end = end;
    }

    public BoxPosition getStart() {
        return start;
    }

    public BoxPosition getEnd() {
        return end;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Line other = (Line) obj;

        if (start != null && !start.equals(other.start))
            return false;
        if (end != null && !end.equals(other.end))
            return false;

        return true;
    }
}
