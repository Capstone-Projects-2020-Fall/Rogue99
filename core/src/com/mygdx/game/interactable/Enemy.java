package com.mygdx.game.interactable;

public class Enemy extends Character {

    private int difficulty;

    public Enemy(int difficulty){
        this.difficulty = difficulty;
    }

    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }

    public int getDifficulty() {
        return difficulty;
    }
}
