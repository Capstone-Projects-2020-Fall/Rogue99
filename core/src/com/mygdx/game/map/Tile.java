package com.mygdx.game.map;

import com.badlogic.gdx.graphics.Texture;

import javax.swing.text.html.parser.Entity;

public class Tile {

    private int posX;
    private int posY;
    private String type;
    private boolean populated;
    private boolean floodFilled = false;

    //TODO We will come back for this later
    //Entity entity;

    public Tile(int posX, int posY, String type, boolean populated){
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

    public void setType(String type) {
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

    public void flood(){
        floodFilled = true;
    }

    public boolean flooded(){
        return floodFilled;
    }

    public boolean isPopulated() {
        return populated;
    }
}
