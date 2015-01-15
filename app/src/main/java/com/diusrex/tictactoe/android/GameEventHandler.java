package com.diusrex.tictactoe.android;

import com.diusrex.tictactoe.logic.BoxPosition;
import com.diusrex.tictactoe.logic.SectionPosition;

public interface GameEventHandler {
    void sectionSelected(SectionPosition position);

    void boxSelected(BoxPosition position);
}
