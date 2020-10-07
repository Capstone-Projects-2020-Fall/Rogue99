package com.mygdx.game.map;

import java.util.ArrayList;

public class Level {

    private int depth;
    private Tile[][] map;
    private int width = 50;
    private int height = 50;
    private int deathLimit = 3;
    private int birthLimit = 4;


    public Level(int depth, Tile[][] map){
        this.depth = depth;
        this.map = map;
    }

    public void setDepth(int depth) {
        this.depth = depth;
    }

    public void setMap(Tile[][] map) {
        this.map = map;
    }

    public Tile[][] getMap() {
        return map;
    }

    public int getDepth() {
        return depth;
    }


    //initializes grid to be all wall tiles
    public void initialize(){
        map = new Tile[width][height];
        for(int i = 0; i < width; i++){
            for(int k = 0; k < height; k++){
                if(Math.random() < 0.5) {
                    map[i][k] = new Tile(i, k, "wall", true);
                } else{
                    map[i][k] = new Tile(i, k, "floor", false);
                }
            }
        }
    }

    public void generate(){
        initialize();

        for(int i = 0; i < 5; i++){
            iterate();
        }
    }

    public int countWallNeighbors(Tile tile){
        int count = 0;
        for(int x = -1; x < 2; x++){
            for(int y = -1; y < 2; y++){
                //check if neighbor is off the map
                if(x+tile.getPosX() < 0 || y+tile.getPosY() < 0 ||
                        x+tile.getPosX() == width || y+tile.getPosY() == height) continue;

                if(map[x+tile.getPosX()][y+tile.getPosY()].getType().equals("wall")){
                    count++;
                }
            }
        }
        return count;
    }

    public void iterate(){
        for(int i = 0; i < width; i++){
            for(int k = 0; k < height; k++){
                int count = countWallNeighbors(map[i][k]);

                if(map[i][k].getType().equals("wall")){
                    if(count < deathLimit){
                        map[i][k].setType("floor");
                    }
                } else{
                    if(count > birthLimit){
                        map[i][k].setType("wall");
                    }
                }
            }
        }
    }
}
