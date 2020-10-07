package com.mygdx.game.map;

import java.util.ArrayList;

public class Level {

    private int depth;
    private Tile[][] map;
    private int width = 50;
    private int height = 50;
    private double probability = 0.47;
    private int deathLimit = 4;
    private int birthLimit = 4;
    private int iterations = 5;


    public Level(int depth){
        this.depth = depth;
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
                if(Math.random() < probability) {
                    map[i][k] = new Tile(i, k, "floor", true);
                } else{
                    map[i][k] = new Tile(i, k, "wall", false);
                }
            }
        }
    }

    public void generate(){
        initialize();

        //run initialized map through cellular automata algorithm
        for(int i = 0; i < iterations; i++){
            iterate();
        }

        //TODO place entrance
        //TODO place exit
        //TODO link rooms and clean up generation
        //TODO add environment
    }

    public int countAliveNeighbors(Tile tile){
        int count = 0;
        for(int x = -1; x < 2; x++){
            for(int y = -1; y < 2; y++){
                //check if neighbor is off the map
                if(x+tile.getPosX() < 0 || y+tile.getPosY() < 0 ||
                        x+tile.getPosX() == width || y+tile.getPosY() == height) continue;

                if(map[x+tile.getPosX()][y+tile.getPosY()].getType().equals("floor")){
                    count++;
                }
            }
        }
        return count;
    }

    public void iterate(){
        for(int i = 0; i < width; i++){
            for(int k = 0; k < height; k++){
                int count = countAliveNeighbors(map[i][k]);

                if(map[i][k].getType().equals("floor")){
                    if(count < deathLimit){
                        map[i][k].setType("wall");
                    }
                } else if(map[i][k].getType().equals("wall")){
                    if(count > birthLimit){
                        map[i][k].setType("floor");
                    }
                }
            }
        }
    }
}
