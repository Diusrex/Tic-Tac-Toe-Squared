package com.diusrex.tictactoe.logic;

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
}
