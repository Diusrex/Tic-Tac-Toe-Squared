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
package com.diusrex.tictactoe.logic;

public enum Player {
    Unowned("0") {
        @Override
        public Player opposite() {
            return Unowned;
        }
    }, Player_1("1") {
        @Override
        public Player opposite() {
            return Player_2;
        }
    }, Player_2("2") {
        @Override
        public Player opposite() {
            return Player_1;
        }
    };

    String name;

    Player(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }

    public abstract Player opposite();

    public static Player fromString(String string) {
        if (string.equals("0"))
            return Unowned;
        else if (string.equals("1"))
            return Player_1;
        else
            return Player_2;
    }
}
