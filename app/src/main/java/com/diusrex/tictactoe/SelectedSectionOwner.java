package com.diusrex.tictactoe;

import android.app.Activity;
import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;

import com.diusrex.tictactoe.logic.BoardStatus;
import com.diusrex.tictactoe.logic.BoxPosition;
import com.diusrex.tictactoe.logic.SectionPosition;

public class SelectedSectionOwner extends SectionOwner {
    static private final int GRID_LINE_WIDTH = 30;
    static private final int GRID_LINE_COLOR = Color.GREEN;
    private final GameEventHandler handler;


    SelectedSectionOwner(SectionPosition sectionPosition, MyGrid grid, GameEventHandler handler) {
        super(sectionPosition, grid);
        this.handler = handler;
    }

    @Override
    protected void setUpGridLines(MyGrid grid) {
        grid.setLineWidth(GRID_LINE_WIDTH);
        grid.setLineColor(GRID_LINE_COLOR);
    }

    @Override
    protected ImageView generateBox(BoardStatus board, BoxPosition posInBoard, Activity activity) {
        ImageView image = new ImageView(activity);

        setBoxImage(board, posInBoard, image);

        image.setTag(posInBoard);
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handler.boxSelected((BoxPosition) v.getTag());
            }
        });

        return image;
    }

    @Override
    protected void setBoxImage(BoardStatus board, BoxPosition posInBoard, ImageView image) {
        switch (board.getBoxOwner(posInBoard)) {
            case Player_1:
                image.setImageResource(R.drawable.o_pressed_large);
                break;

            case Player_2:
                image.setImageResource(R.drawable.x_pressed_large);
                break;

            case Unowned:
                image.setImageResource(R.drawable.blank_large);
                break;
        }
    }
}
