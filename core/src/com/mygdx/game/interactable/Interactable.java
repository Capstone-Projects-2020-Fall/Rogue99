package com.mygdx.game.interactable;

public abstract class Interactable {

    private int posX;
    private int posY;

    public void setPosY(int posY) {
        this.posY = posY;
    }

    public void setPosX(int posX) {
        this.posX = posX;
    }

    public int getPosY() {
        return posY;
    }

    public int getPosX() {
        return posX;
    }
}
