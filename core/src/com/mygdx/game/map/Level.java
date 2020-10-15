package com.mygdx.game.map;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class Level {
    Random rand = new Random();
    private int floodCount;

    private final int depth;
    private Tile[][] map;
    private final int width = 60;
    private final int height = 60;
    private Tile entrance;

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
        generateStairs();

        generateEnemy();


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
    // place up and down stairs on map
    private void generateStairs() {
        int x_down = (int) (Math.random() * 60);
        int y_down;
        if (Math.random() < 0.5) y_down = (int) (Math.random() * 15);
        else y_down = (int) (Math.random() * 15) + 45;
        int x_up = (int) (Math.random() * 60);
        int y_up = (int) (Math.random() * 60);
        // picks a random tile that isn't a wall and has at least 1 wall neighbor
        while (map[x_down][y_down].getType().equals("wall") || countAliveNeighbors(map[x_down][y_down], "wall") < 1) {
            x_down = (int) (Math.random() * 60);
            if (Math.random() < 0.5) y_down = (int) (Math.random() * 15);
            else y_down = (int) (Math.random() * 15) + 45;
        }
        map[x_down][y_down].setType("stair_down");
        // picks a random tile that isn't a wall and is far enough away from the other stairs and has at least 1 wall neighbor
        while (!checkDistance(x_down, y_down, x_up, y_up, 50) || map[x_up][y_up].getType().equals("wall")
                || countAliveNeighbors(map[x_up][y_up], "wall") < 1) {
            x_up = (int) (Math.random() * 60);
            y_up = (int) (Math.random() * 60);
        }
        map[x_up][y_up].setType("stair_up");
        entrance = map[x_down][y_down];
    }
    // returns false if distance between points is less than d
    private boolean checkDistance(double x1, double y1, double x2, double y2, int d){
        double ac = Math.abs(y2 - y1);
        double cb = Math.abs(x2 - x1);
        if (!(Math.sqrt((y2 - y1) * (y2 - y1) + (x2 - x1) * (x2 - x1)) < d)) return true;
        return false;
    }
    // decides difficulty and number of enemies spawned based on depth of level. Returns an array where index is difficulty and value is
    // number of enemies with that difficulty
    public int[] iterateEnemy() {
        int[] arr = new int[depth+1];
        int n = 0;
        arr[0] = 4;
        arr[1] = 1;
        for (int i = 1; i <= depth - 1; i++) {
            arr[n]--;
            arr[n + 1]++;
            if(arr[n+1] == 4) {
                arr[n + 2] = 1;
                n++;
            }
            if(n >= 6) arr[n-6] = 0;
        }
        return arr;
    }
    public void generateEnemy(){
        int[] diff = iterateEnemy();
        int x = 0;
        int y = 0;
        for(int i : diff){
            if(i == 0) continue;
            for(int j = 0; j < i; j++) {
                while (!(map[x][y].getType().equals("floor") || map[x][y].getType().equals("grass"))) {
                    x = (int) (Math.random() * width);
                    y = (int) (Math.random() * height);
                }
                map[x][y].setType("enemy");
            }
        }
    }
}
