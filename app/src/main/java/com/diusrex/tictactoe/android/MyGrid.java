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
package com.diusrex.tictactoe.android;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.widget.GridLayout;

public class MyGrid extends GridLayout {
    View lineStart;
    View lineEnd;
    Paint paint;

    public MyGrid(Context context) {
        super(context);
        paint = new Paint();
    }

    public MyGrid(Context context, AttributeSet attrs) {
        super(context, attrs);
        paint = new Paint();
    }

    public MyGrid(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public boolean hasLine() {
        return lineStart != null && lineEnd != null;
    }

    public void setLine(View lineStart, View lineEnd) {
        this.lineStart = lineStart;
        this.lineEnd = lineEnd;
        invalidate();
    }

    public void removeLine() {
        this.lineStart = null;
        this.lineEnd = null;
        invalidate();
    }

    public void setLineWidth(int width) {
        paint.setStrokeWidth(width);
    }

    public void setLineColor(int color) {
        paint.setColor(color);
    }


    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);

        drawLine(canvas);
    }

    private void drawLine(Canvas canvas) {

        if (hasLine()) {
            int offset = lineStart.getHeight() / 2;
            canvas.drawLine(lineStart.getX() + offset, lineStart.getY() + offset, lineEnd.getX() + offset, lineEnd.getY() + offset, paint);

            // Have the line be rounded
            canvas.drawCircle(lineStart.getX() + offset, lineStart.getY() + offset, paint.getStrokeWidth() / 2, paint);
            canvas.drawCircle(lineEnd.getX() + offset, lineEnd.getY() + offset, paint.getStrokeWidth() / 2, paint);
        }
    }
}
