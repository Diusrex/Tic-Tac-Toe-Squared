package com.diusrex.tictactoe.android;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.diusrex.tictactoe.R;

public class ActivityMainMenu extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

    }

    @Override
    protected void onResume() {
        super.onResume();

        updateContinueButtonEnabled();
    }

    private void updateContinueButtonEnabled() {
        BoardStateSaverAndLoader loader = new BoardStateSaverAndLoader(this);
        Button continueGameButton = (Button) findViewById(R.id.continueGameButton);

        continueGameButton.setEnabled(loader.saveGameExists());
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
