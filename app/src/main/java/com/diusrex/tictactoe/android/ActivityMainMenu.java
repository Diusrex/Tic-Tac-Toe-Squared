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
        Intent intent = new Intent(this, HowToPlayActivity.class);
        startActivity(intent);
    }
}
