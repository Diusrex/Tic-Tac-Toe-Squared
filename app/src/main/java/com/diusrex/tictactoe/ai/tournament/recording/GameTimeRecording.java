package com.diusrex.tictactoe.ai.tournament.recording;

public class GameTimeRecording {
    private final long times[];

    public GameTimeRecording() {
        times = new long[2];
        times[0] = times[1] = 0;
    }

    public void addTime(long time, int player) {
        times[player] += time;
    }

    public long getPlayerOneTime() {
        return times[0];
    }

    public long getPlayerTwoTime() {
        return times[1];
    }
}
