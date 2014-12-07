package com.diusrex.tictactoe.textbased;

import com.diusrex.tictactoe.logic.BoardStatus;
import com.diusrex.tictactoe.logic.BoxPosition;
import com.diusrex.tictactoe.logic.Player;

public interface PlayerController {
    public BoxPosition getPositionToPlay(BoardStatus board);
    
    public Player getWhichPlayer();

    public void alertInvalidMove();
}
