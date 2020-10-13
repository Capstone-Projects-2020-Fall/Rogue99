package com.mygdx.game.map;

import com.badlogic.gdx.graphics.Texture;
import com.mygdx.game.interactable.Interactable;

import javax.swing.text.html.parser.Entity;
import java.util.Stack;

public class Tile {

    private int posX;
    private int posY;
    private String type;
    private boolean populated;
    private boolean floodFilled = false;
    private Stack<Interactable> entities;

    public Tile(int posX, int posY, String type, boolean populated, int zone){
        this.posX = posX;
        this.posY = posY;
        this.type = type;
        this.populated = populated;
    }

    public Stack<Interactable> getEntities(){
        return entities;
    }

    public void addEntity(Interactable entity){
        entities.push(entity);
    }

    public Interactable removeEntity(Interactable entity){
        return entities.pop();
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
