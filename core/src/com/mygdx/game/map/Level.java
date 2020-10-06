package com.mygdx.game.map;

import java.util.ArrayList;

public class Level {

    private int depth;
    private ArrayList<ArrayList<Tile>> map;


    public Level(int depth, ArrayList<ArrayList<Tile>> map){
        this.depth = depth;
        this.map = map;
    }

    public void setDepth(int depth) {
        this.depth = depth;
    }

    public void setMap(ArrayList map) {
        this.map = map;
    }

    public ArrayList getMap() {
        return map;
    }

    public int getDepth() {
        return depth;
    }
}
