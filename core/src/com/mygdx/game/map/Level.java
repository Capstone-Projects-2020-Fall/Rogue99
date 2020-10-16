package com.mygdx.game.map;

import com.mygdx.game.interactable.Interactable;
import com.mygdx.game.item.Item;
import com.mygdx.game.item.Potion;

import java.util.ArrayList;
import java.util.Random;

public class Level {
    Random rand = new Random();
    private int floodCount;

    private final int depth;
    private Tile[][] map;
    private final int width = 60;
    private final int height = 60;
    private Zone[] zones = new Zone[4];
    private int zoneSize;

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

        //generate zones
        int limit;
        for(int i = 1; i < 4; i++){
            limit = rand.nextInt(200)+400;
            zones[i] = new Zone(i, new ArrayList<Tile>());
            while(zones[i].tiles.size() < 150){
                generateZone(zones[i], limit);
            }
            System.out.println("Zone " + i + ": " + zones[i].tiles.size());
        }

        //generate grass
        generateGrass();
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
        int startX, startY;
        do{
            startX = rand.nextInt(width);
            startY = rand.nextInt(height);
        }while(map[startX][startY].getType().equals("wall"));

        //initialize Zone 0
        zones[0] = new Zone(0, new ArrayList<Tile>());

        floodFillUtil(startX, startY);
    }

    public void floodFillUtil(int x, int y){
        if(map[x][y].getType().equals("wall") || map[x][y].flooded()) return;

        map[x][y].flood();
        floodCount++;

        zones[0].tiles.add(map[x][y]);
        map[x][y].setZone(0);

        floodFillUtil(x+1, y);
        floodFillUtil(x-1, y);
        floodFillUtil(x, y+1);
        floodFillUtil(x, y-1);
    }

    //generate grass
    public void generateGrass(){
        Tile[][] grassMap = new Tile[width][height];

        grassMap = initialize(grassMap, grassSettings, "grass", "floor", false, false);
        //run grass map through cellular automata algorithm
        for (int i = 0; i < grassSettings.iterations; i++) {
            grassMap = iterate(grassMap, grassSettings, "grass", "floor");
        }

        mergeGrass(grassMap);
    }

    public void mergeGrass(Tile[][] grassMap){
        for(int i = 0; i < width; i++){
            for(int k = 0; k < height; k++){
                if(this.map[i][k].getType().equals("floor") && grassMap[i][k].getType().equals("grass")){
                    this.map[i][k].setType("grass");
                }
            }
        }
    }

    private void generateZone(Zone zone, int zoneLimit){
        int startX, startY;
        do{
            startX = rand.nextInt(width);
            startY = rand.nextInt(height);
        }while(map[startX][startY].getType().equals("wall") && map[startX][startY].getZone() != 0);

        floodZoneUtil(zone, startX, startY, zoneLimit);
    }

    private void floodZoneUtil(Zone zone, int x, int y, int zoneLimit){
        if(map[x][y].getType().equals("wall") || map[x][y].getZone() != 0 || zone.tiles.size() == zoneLimit) return;

        map[x][y].setZone(zone.id);
        zone.tiles.add(map[x][y]);

        floodZoneUtil(zone, x+1, y, zoneLimit);
        floodZoneUtil(zone, x-1, y, zoneLimit);
        floodZoneUtil(zone, x, y+1, zoneLimit);
        floodZoneUtil(zone, x, y-1, zoneLimit);
    }

//    private void generateItems(){
//        //generate potions
//        int c = 100;
//        for(int i = 0; i < 10; i++){
//            int itemC = rand.nextInt(c);
//            //if statement with odds of each potion type
//            if(itemC < 10){
//                generateItemUtil(new Potion(10, "healthpotion", 10));
//            }
//        }
//
//        //generate weapons
//    }
//
//    private void generateItemUtil(Interactable item){
//        int itemX, itemY;
//
//        do{
//            itemX = rand.nextInt(60);
//            itemY = rand.nextInt(60);
//        }while(map[itemX][itemY].getType().equals("wall") && !map[itemX][itemY].getEntities().empty());
//
//        map[itemX][itemY].getEntities().push(item);
//    }
}
