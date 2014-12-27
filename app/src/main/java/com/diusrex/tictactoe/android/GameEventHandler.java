package com.diusrex.tictactoe.android;

import com.diusrex.tictactoe.logic.BoxPosition;
import com.diusrex.tictactoe.logic.SectionPosition;

/**
 * Created by Diusrex on 2014-12-14.
 */
public interface GameEventHandler {
    void sectionSelected(SectionPosition position);

    void boxSelected(BoxPosition position);
}
