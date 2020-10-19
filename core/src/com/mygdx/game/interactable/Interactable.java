package com.mygdx.game.interactable;

public abstract class Interactable {

    private int posX;
    private int posY;
    private String sprite;

    public void setPosY(int posY) {
        this.posY = posY;
    }

    public void setPosX(int posX) {
        this.posX = posX;
    }

    public void setSprite(String sprite) {
        this.sprite = sprite;
    }

    public int getPosY() {
        return posY;
    }

    public int getPosX() {
        return posX;
    }

    public String getSprite() {
        return sprite;
    }
}
