package com.diusrex.tictactoe;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class ActivityMainMenu extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        disableContinueGameButtonIfNoGame();
    }

    private void disableContinueGameButtonIfNoGame() {
        // For now, will not disable it (works the same)
    }

    public void startNewGame(View v) {
        Intent intent = new Intent(this, GameActivity.class);
        intent.putExtra(GameActivity.IS_NEW_GAME, true);
        startActivity(intent);
    }

    public void continueGame(View v) {
        Intent intent = new Intent(this, GameActivity.class);
        intent.putExtra(GameActivity.IS_NEW_GAME, false);
        startActivity(intent);
    }

    public void showHelpScreen(View v) {

    }
}
