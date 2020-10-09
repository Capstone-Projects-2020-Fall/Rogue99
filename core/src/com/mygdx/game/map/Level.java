package com.mygdx.game.map;

import java.util.ArrayList;
import java.util.Random;

public class Level {
    Random rand = new Random();
    private int floodCount;

    private final int depth;
    private Tile[][] map;
    private final int width = 60;
    private final int height = 60;

    /*
    GENERATION SETTINGS
    0.47, 4, 4, 5 (open, large connected caves)
    0.39, 4, 4, 7 (closed caves)
    0.44, 4, 4, 4,

    with rooms
    0.5, 5, 4, 8, 20
     */

    GenerationSettings levelSettings = new GenerationSettings(0.44, 4, 4, 3);
    //grass alive floor dead
    GenerationSettings grassSettings = new GenerationSettings(0.83, 4, 4, 3);

    private final int numRooms = 5;


    public Level(int depth){
        this.depth = depth;
    }

    public Tile[][] getMap() {
        return map;
    }

    public int getDepth() {
        return depth;
    }

    public void generate(){
        /*
            generate random map with cellular automata- if there is no connected cavern at least as big as 45% of the
            map, regenerate the map and try again
        */
        do {
            System.out.println("New map generated");
            floodCount = 0;
            this.map = initialize(this.map, levelSettings, "floor", "wall", false, true);

            //run initialized map through cellular automata algorithm
            for (int i = 0; i < levelSettings.iterations; i++) {
                this.map = iterate(this.map, levelSettings, "floor", "wall");
            }

            encloseMap();
            floodFill();
            System.out.println(floodCount);
            System.out.println(width*height);
        }while((double)floodCount/(width*height) < 0.44);

        //loop through map- if tile is not filled, turn to wall
        for(Tile[] x : map){
            for(Tile y : x){
                if(!y.flooded() && y.getType().equals("floor")){
                    y.setType("wall");
                }
            }
        }

        //generateRoom();

        //generate grass
        generateGrass();

        //generateStairs
        //iterateStairs();

        //generate rectangular rooms
//        for(int i = 0; i < numRooms; i++){
//            generateRooms();
//        }
        //TODO place entrance
        //TODO place exit
        //TODO add environment
    }

    //initializes grid to be all wall tiles
    public Tile[][] initialize(Tile[][] map, GenerationSettings gen, String type1, String type2, boolean type1Pop,
                               boolean type2Pop){
        map = new Tile[width][height];
        for(int i = 0; i < width; i++){
            for(int k = 0; k < height; k++){
                if(Math.random() < gen.probability) {
                    map[i][k] = new Tile(i, k, type1, type1Pop);
                } else{
                    map[i][k] = new Tile(i, k, type2, type2Pop);
                }
            }
        }
        return map;
    }

    public int countAliveNeighbors(Tile tile, String alive){
        int count = 0;
        for(int x = -1; x < 2; x++){
            for(int y = -1; y < 2; y++){
                //check if neighbor is off the map
                if(x+tile.getPosX() < 0 || y+tile.getPosY() < 0 ||
                        x+tile.getPosX() == width || y+tile.getPosY() == height) continue;
                //alive = "floor"
                if(map[x+tile.getPosX()][y+tile.getPosY()].getType().equals(alive)){
                    count++;
                }
            }
        }
        return count;
    }

    public Tile[][] iterate(Tile[][] map, GenerationSettings gen, String alive, String dead){
        for(int i = 0; i < width; i++){
            for(int k = 0; k < height; k++){
                int count = countAliveNeighbors(map[i][k], alive);

                if(map[i][k].getType().equals(alive)){
                    if(count < gen.deathLimit){
                        map[i][k].setType(dead);
                    }
                } else if(map[i][k].getType().equals(dead)){
                    if(count > gen.birthLimit || count == 0){
                        map[i][k].setType(alive);
                    }
                }
            }
        }
        return map;
    }

    public void generateRoom(){
        //find top left corner
        int cornerX;
        int cornerY;
        do {
            cornerX = rand.nextInt(width);
            cornerY = rand.nextInt(height);
            if(cornerX <= 0) cornerX++;
            if(cornerY <= 0) cornerY++;
        }while(!map[cornerX][cornerY].getType().equals("wall") && !canPlaceRoom(cornerX, cornerY));
        System.out.println("cornerX" + cornerX);
        System.out.println("cornerY" + cornerY);


        //carve out room
        int dX = 0;
        int dY = 0;
        while(map[cornerX+dX][cornerY].getType().equals("wall") && map[cornerX+dX+2][cornerY].getType().equals("floor")){
            while(map[cornerX+dX][cornerY+dY].getType().equals("wall") && map[cornerX][cornerY+dY+2].getType().equals("floor")){
                map[cornerX+dX][cornerY+dY].setType("grass");
            }
        }
    }

    //determines whether a room can be placed at the starting coordinates
    public boolean canPlaceRoom(int cornerX, int cornerY){
        int roomWidth = 0;
        int roomHeight = 0;
        int d = 1;

        while(cornerX+d < width-1 && map[cornerX+d][cornerY].getType().equals("wall") &&
                !map[cornerX+d+2][cornerY].getType().equals("floor")){
            roomWidth++;
            d++;
        }
        d = 1;
        while(cornerY+d < height-1 && map[cornerX][cornerY+d].getType().equals("wall") &&
                !map[cornerX][cornerY+d+2].getType().equals("floor")){
            roomHeight++;
            d++;
        }

        if(roomWidth >= 5 && roomHeight >= 5){
            return true;
        } else{
            return false;
        }
    }

    //encloses map with layer of wall tiles
    public void encloseMap(){
        //top and bottom
        for(int i = 0; i < width; i++){
            if(map[i][0].getType().equals("floor")){
                map[i][0].setType("wall");
            }
            if(map[i][height-1].getType().equals("floor")){
                map[i][height-1].setType("wall");
            }
        }
        //left and right
        for(int i = 0; i < height; i++){
            if(map[0][i].getType().equals("floor")){
                map[0][i].setType("wall");
            }
            if(map[width-1][i].getType().equals("floor")){
                map[width-1][i].setType("wall");
            }
        }
    }

    //finds size of random room- if under a certain size, a new map will be generated
    public void floodFill(){
        int startX, startY, count;
        do{
            startX = rand.nextInt(width);
            startY = rand.nextInt(height);
        }while(map[startX][startY].getType().equals("wall"));

        floodFillUtil(startX, startY);
    }

    public void floodFillUtil(int x, int y){
        if(map[x][y].getType().equals("wall") || map[x][y].flooded()) return;

        map[x][y].flood();
        floodCount++;

        floodFillUtil(x+1, y);
        floodFillUtil(x-1, y);
        floodFillUtil(x, y+1);
        floodFillUtil(x, y-1);
    }

    //generate grass 97
    public void generateGrass(){
        Tile[][] grassMap = new Tile[width][height];

        grassMap = initialize(grassMap, grassSettings, "grass", "floor", false, false);
        //run grass map through cellular automata algorithm
        for (int i = 0; i < grassSettings.iterations; i++) {
            grassMap = iterate(grassMap, grassSettings, "grass", "floor");
        }

        //TODO merge level map and grass map
        mergeGrass(grassMap);
    }

    public void mergeGrass(Tile[][] grassMap){
        for(int i = 0; i < width; i++){
            for(int k = 0; k < height; k++){
//                System.out.println("level map type: " + this.map[i][k].getType());
//                System.out.println("grass map type: " + grassMap[i][k].getType());

                if(this.map[i][k].getType().equals("floor") && grassMap[i][k].getType().equals("grass")){
                    this.map[i][k].setType("grass");
                }
            }
        }
    }
    //if distance between stair is too small, find a new spot for them
    public void iterateStairs(){
        while(generateStairs() < 30){
            removeStairs();
            generateStairs();
        }
    }
    private void removeStairs(){
        for(int i = 0; i < width; i++){
            for(int j = 0; j < height; j++){
                if(map[i][j].getType().equals("stair_down") || map[i][j].getType().equals("stair_up"))
                    map[i][j].setType("floor");
            }
        }
    }
    // place up and down stairs on map
    private int generateStairs(){
        int x_down = (int)(Math.random()*60);
        int x_up = (int)(Math.random()*60);
        int y_up = (int)(Math.random()*60);
        int y_down = (int)(Math.random()*60);
        //if random tile is floor, travels randomly until neighboring wall is found and places stair_down next to wall
        while(map[x_down][y_down].getType().equals("floor")) {
            for (int x1 = -1; x1 < 2; x1++) {
                for (int y1 = -1; y1 < 2; y1++){
                    if(x1+x_down < 0 || y1+y_down < 0 ||
                            x1+x_down == width || y1+y_down == height) continue;
                    if(map[x1+x_down][y1+y_down].getType().equals("wall"))
                        map[x_down][y_down].setType("stair_down");
                        //if no neighboring walls, travel to a random neighboring tile
                    else {
                        x_down += (int) (Math.random() * 2) - 1;
                        y_down += (int) (Math.random() * 2) - 1;

                    }
                }
            }
        }
        // if random tile is wall, travels randomly until neighboring floor is found and places stair_down on floor
        while(map[x_down][y_down].getType().equals("wall")){
            for (int x1 = -1; x1 < 2; x1++) {
                for (int y1 = -1; y1 < 2; y1++) {
                    if (x1 + x_down < 0 || y1 + y_down < 0 ||
                            x1 + x_down == width || y1 + y_down == height) continue;
                    if(map[x1+x_down][y1+y_down].getType().equals("floor")) {
                        x_down += x1;
                        y_down += y1;
                        map[x_down][y_down].setType("stair_down");
                    } else {
                        chooseRandomNeighbor(map[x_down][y_down]);

                    }
                }
            }
        }
        // if random tile is floor, travels randomly until neighboring wall is found and places stair_up next to wall
        while(map[x_up][y_up].getType().equals("floor")) {
            for (int x2 = -1; x2 < 2; x2++) {
                for (int y2 = -1; y2 < 2; y2++){
                    if(x2+x_up < 0 || y2+y_up < 0 ||
                            x2+x_up == width || y2+y_up == height) continue;
                    if(map[x2+x_up][y2+y_up].getType().equals("wall"))
                        map[x_up][y_up].setType("stair_up");
                    else {
                        chooseRandomNeighbor(map[x_up][y_up]);
                    }
                }
            }
        }
        //if random tile is wall, travels randomly until neighboring floor is found and places stair_down on floor
        while(map[x_up][y_up].getType().equals("wall")){
            for (int x2 = -1; x2 < 2; x2++) {
                for (int y2 = -1; y2 < 2; y2++) {
                    if (x2 + x_up < 0 || y2 + y_up < 0 ||
                            x2 + x_up == width || y2 + y_up == height) continue;
                    if(map[x2+x_up][y2+y_up].getType().equals("floor")) {
                        x_up += x2;
                        y_up += y2;
                        map[x_up][y_up].setType("stair_up");
                    } else {
                        chooseRandomNeighbor(map[x_up][y_up]);
                    }
                }
            }
        }
        //calculates and returns the distance between the two stairs
        double ab = Math.abs(y_up - y_down);
        double ac = Math.abs(x_up - x_down);
        int distance = (int)(Math.hypot(ab, ac));
        return distance;
    }
    // chooses a random neighbor of a tile and moves to it
    private void chooseRandomNeighbor(Tile tile) {
        for(int x = -1; x < 2; x++) {
            for (int y = -1; y < 2; y++) {
                //check if neighbor is off the map
                if (x + tile.getPosX() < 0 || y + tile.getPosY() < 0 ||
                        x + tile.getPosX() == width || y + tile.getPosY() == height) continue;

            }
        }

    }
}
