package com.mygdx.game.map;

import java.util.ArrayList;

public class Zone {
    public int id;
    public ArrayList<Tile> tiles;

    public Zone(int id, ArrayList<Tile> tiles){
        this.id = id;
        this.tiles = tiles;
    }
}
