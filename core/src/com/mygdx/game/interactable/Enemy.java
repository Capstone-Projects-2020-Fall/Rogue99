package com.mygdx.game.interactable;

public class Enemy extends Character {

    private int difficulty;

    public Enemy(int difficulty) {
        this.difficulty = difficulty;
        for (int i = 0; i < difficulty; i++) {
            super.setMaxHP(getMaxHP() + 10);
            super.setArmor(getArmor() + 5);
            super.setStr(getStr() + 2);
        }
    }

    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }

    public int getDifficulty() {
        return difficulty;
    }
}
