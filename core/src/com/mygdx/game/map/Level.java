package com.mygdx.game.map;

import com.mygdx.game.interactable.Enemy;
import com.mygdx.game.interactable.Interactable;
import com.mygdx.game.item.*;

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
    private Zone[] zones = new Zone[4];
    private int zoneSize;
    private ArrayList<Enemy> enemies = new ArrayList<>();

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

            //make sure level is connected and initiate zone 0
            floodFill();
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
        //System.out.println("Zone 0: " + zones[0].tiles.size());
        int limit;
        for(int i = 1; i < 4; i++){
            limit = rand.nextInt(200)+400;
            zones[i] = new Zone(i, new ArrayList<Tile>());
            while(zones[i].tiles.size() < 150){
                generateZone(zones[i], limit);
            }
            System.out.println("Zone " + i + ": " + zones[i].tiles.size());
        }
        System.out.println("Zone 0: " + zones[0].tiles.size());


        generateGrass();
        generateStairs();
        generateEnemy();
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
        int sum = 0;
        int index = 1;
        int u = 0;
        int x = 0;
        int y = 0;
        for(int i : diff){
            sum += i;
            if(i == 0) continue;
            for (int j = 0; j < sum; j++) {
                Zone z = zones[u];
                Tile tile;
                do { tile = z.tiles.get(rand.nextInt(z.tiles.size()));
                }
                while (!tile.entities.isEmpty());
                Enemy enemy = new Enemy(index);
                enemies.add(enemy);
                tile.getEntities().push(enemies.get(enemies.size() - 1));
                u++;
                if(u > 3) u = 0;
                }
            index++;
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
        zones[0].tiles.remove(map[x][y]);

        floodZoneUtil(zone, x+1, y, zoneLimit);
        floodZoneUtil(zone, x-1, y, zoneLimit);
        floodZoneUtil(zone, x, y+1, zoneLimit);
        floodZoneUtil(zone, x, y-1, zoneLimit);
    }

    private void generateItems(){
        int c = 100;
        int numItems, itemC;
        for(Zone z : zones){
            numItems = z.id+rand.nextInt(2);
            for(int i = 0; i < numItems; i++){
                itemC = rand.nextInt(c);
                //TODO flesh out item chances once potion classes are finished
                if(itemC < 10){
                    generateItemUtil(new Potion(10, "healthpotion", 10), z);
                } else if(10 <= itemC && itemC < 30){
                    generateItemUtil(new ArmorScroll(20, "scroll", 10), z);
                } else if(30 <= itemC && itemC < 50){
                    generateItemUtil(new HealthScroll(20, "scroll", 10), z);
                } else if(50 <= itemC && itemC < 70){
                    generateItemUtil(new StrengthScroll(20, "scroll", 10), z);
                }
            }
        }

        //generate weapons
    }

    //generates item on random tile in given zone
    private void generateItemUtil(Interactable item, Zone zone){
        Tile tile;
        do{
            tile = zone.tiles.get(rand.nextInt(zone.tiles.size()));
        } while(!tile.getEntities().empty());

        tile.getEntities().push(item);
    }
}
