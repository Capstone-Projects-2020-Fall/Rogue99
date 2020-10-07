package com.mygdx.game.map;

import javax.swing.text.html.parser.Entity;

public class Tile {

    private int posX;
    private int posY;
    private String type;
    private String texture;
    private boolean populated;

    //TODO We will come back for this later
    //Entity entity;

    public Tile(int posX, int posY, String type, boolean populated){
        this.posX = posX;
        this.posY = posY;
        this.type = type;
        //this.texture = texture;
        this.populated = populated;
    }

    public void setPosX(int posX) {
        this.posX = posX;
    }

    public void setPosY(int posY) {
        this.posY = posY;
    }

    public void setType(String type) {
        //TODO texture change

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

    public String getType() {
        return type;
    }

    public boolean isPopulated() {
        return populated;
    }
}
