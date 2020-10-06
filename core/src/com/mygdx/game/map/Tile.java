package com.mygdx.game.map;

import javax.swing.text.html.parser.Entity;

public class Tile {

    private int posX;
    private int posY;
    private int type;
    private boolean populated;

    //TODO We will come back for this later
    //Entity entity;

    public Tile(int posX, int posY, int type, boolean populated){
        this.posX = posX;
        this.posY = posY;
        this.type = type;
        this.populated = populated;
    }

    public void setPosX(int posX) {
        this.posX = posX;
    }

    public void setPosY(int posY) {
        this.posY = posY;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setPopulated(boolean populated) {
        this.populated = populated;
    }

    public int getPosX() {
        return posX;
    }

    public int getPosY() {
        return posY;
    }

    public int getType() {
        return type;
    }

    public boolean isPopulated() {
        return populated;
    }
}
