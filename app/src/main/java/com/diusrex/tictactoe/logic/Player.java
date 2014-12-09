package com.diusrex.tictactoe.logic;

public enum Player {
    Unowned("0"), Player_1("1"), Player_2("2");

    String name;

    Player(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
