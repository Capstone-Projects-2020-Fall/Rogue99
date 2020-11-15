package com.mygdx.game.map;

import com.mygdx.game.Rogue99;
import com.mygdx.game.interactable.*;
import com.mygdx.game.item.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;


public class Level implements Serializable {
    Random randGen;
    Random rand = new Random();

    //generation settings/features
    private int floodCount;
    private String seed;
    private final int depth;
    private Tile[][] map;
    public int[][] intMap;
    private final int width = 60;
    private final int height = 60;
    public Tile entrance;
    public Tile exit;
    private Zone[] zones = new Zone[4];
    private int zoneSize;

    //active entities
    public ArrayList<Enemy> enemies = new ArrayList<>();
    public int enemiesNum;
    public Hero hero;
    public ArrayList<Hero> players = new ArrayList<>();

    public boolean doorOpen = false;
    public Double enemiesToOpen;


    private Rogue99 game;


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

    public Level(Rogue99 game, int depth, Hero hero){
        this.game = game;
        this.depth = depth;
        this.hero = hero;
   }

    public Tile[][] getMap() {
        return map;
    }

    public int[][] getIntMap() { return intMap; }

    public int getDepth() {
        return depth;
    }

    public void moveEnemies(){
        for(Enemy enemy : enemies){
            if(enemy.getSprite().equals("ghost")){
                int[][] intMapGhost = new int[this.getWidth()][this.getHeight()];
                Ghost ghost = (Ghost) enemy;
                ghost.moveEnemy(map, intMapGhost, hero);
            } else{
                enemy.moveEnemy(map, intMap, hero);
            }
        }
        game.timerCount = 0;
    }

    public void setSeed(String seed) {
        this.seed = seed;
    }

    public String generateSeed() {
        return String.valueOf(System.currentTimeMillis() + rand.nextInt());
    }

    public void generate(){
        /*
            generate random map with cellular automata- if there is no connected cavern at least as big as 45% of the
            map, regenerate the map and try again
        */
        do {
            System.out.println("New map generated");
            System.out.println("generate() seed: " + seed);
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
        System.out.println("Generating zones");
        int limit;
        for(int i = 1; i < 4; i++){
            limit = rand.nextInt(200)+400;
            zones[i] = new Zone(i, new ArrayList<Tile>());
            while(zones[i].tiles.size() < 150){
                generateZone(zones[i], limit);
            }
            //System.out.println("Zone " + i + ": " + zones[i].tiles.size());
        }
        System.out.println("Zone generation complete");

        System.out.println("Generating grass");
        generateGrass();
        System.out.println("Grass generation complete");
        System.out.println("Generating stairs");
        generateStairs();
        System.out.println("Stair generation complete");
        //generateEnemy();
        System.out.println("Generating enemies");
        generateEnemies();
        System.out.println("Enemy generation complete");
        System.out.println("Generating items");
        generateItems();
        System.out.println("Item generation complete");

        this.entrance.getEntities().push(hero);
        hero.setPosX(this.entrance.getPosX());
        hero.setPosY(this.entrance.getPosY());

        intMap = new int[width][height];
        for (int i = 0; i < width; i++) {
            for (int k = 0; k < height; k++) {
                if( map[i][k].getType().equals("wall") || !(map[i][k].getEntities().isEmpty()) ) {
                    intMap[i][k] = -1;
                }
                else {
                    intMap[i][k] = 0;
                }
            }
        }
        //hero.depth = this.depth;
    }

    public boolean generateFloorPlan(){
        do {
            System.out.println("New map generated");
            this.seed = generateSeed();
            System.out.println("NEW SEED: " + seed);
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
        System.out.println("FINAL SEED: " + this.seed);
        return true;
    }

    //initializes grid to be all wall tiles
    public Tile[][] initialize(Tile[][] map, GenerationSettings gen, String type1, String type2, boolean type1Pop,
                               boolean type2Pop){
        randGen = new Random(Long.valueOf(seed));
        map = new Tile[width][height];
        for(int i = 0; i < width; i++){
            for(int k = 0; k < height; k++){
                if(randGen.nextDouble() < gen.probability) {
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
            startX = randGen.nextInt(width);
            startY = randGen.nextInt(height);
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
        map[x_down][y_down].setType("downstair");
        exit = map[x_down][y_down];
        // picks a random tile that isn't a wall and is far enough away from the other stairs and has at least 1 wall neighbor
        while (!checkDistance(x_down, y_down, x_up, y_up, 50) || map[x_up][y_up].getType().equals("wall")
                || countAliveNeighbors(map[x_up][y_up], "wall") < 1) {
            x_up = (int) (Math.random() * 60);
            y_up = (int) (Math.random() * 60);
        }
        map[x_up][y_up].setType("upstair");
        entrance = map[x_up][y_up];
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
        int[] arr = new int[depth+2];
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
        //System.out.println("in generateEnemy");
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
                //find open tile within zone
                Zone z = zones[u];
                Tile tile;
                do {
                    tile = z.tiles.get(rand.nextInt(z.tiles.size()));
                } while (!tile.entities.isEmpty());

                //get enemy type of difficulty i
                System.out.println();
                String type = game.enemyMap.get(i).get(rand.nextInt(game.enemyMap.get(i).size()));
                Enemy enemy = new Enemy();
                if(type.equals("rat")){
                    enemy = new Rat(tile, game);
                } else if(type.equals("wasp")){
                    enemy = new Wasp(tile, game);
                } else if(type.equals("slime")){
                    enemy = new Slime(tile, game);
                }

                //Enemy enemy = new Enemy(index, "wasp", tile, game);
                //System.out.println("ENEMY GENERATED: " + enemy.getSprite());
                enemies.add(enemy);
                tile.getEntities().push(enemy);
                u++;
                if(u > 3) u = 0;
            }
            index++;
        }
    }

    public void generateEnemies(){
        int[] diffMap = new int[game.enemyMap.size()];
        //initial settings for enemy difficulty distribution
        diffMap[0] = 6;
        diffMap[1] = 1;
        diffMap[2] = 0;
        int diffMod = 0;
        for(int i = 0; i < depth; i++){
            if(1 <= i && i < 3){    //8, 2, 0 by L3
                diffMap[0]++;
                diffMap[1]++;
                diffMap[2]++;
            } else if(3 <= i && i < 6){
                int c = 0;
                diffMap[0]--;
                diffMap[1]++;
                diffMap[2]++;
                if(i % 2 == 1) diffMod++;
            } else if(6 <= i && i < 10){
                diffMap[0]+=2;
                diffMap[1]--;
                diffMap[2]++;
            } else{
                diffMod++;
            }
        }
        int count = 0;
        for (int i : diffMap){
            System.out.println("Difficulty " + count + ": " + i);
            count++;
        }
        System.out.println("diffMod: " + diffMod);

        //iterate through difficulty distribution and spawn evenly across zones; scale if necessary
        int u;
        for(int i = 0; i < diffMap.length; i++){
            System.out.println("SPAWNING DIFFICULTY " + i);
            System.out.println("diffMap[i]: " + diffMap[i]);
            u = 0;
            for(int k = 0; k < diffMap[i]; k++){
                Zone z = zones[u];
                Tile tile;
                do {
                    tile = z.tiles.get(rand.nextInt(z.tiles.size()));
                } while (!tile.entities.isEmpty());

                String type = game.enemyMap.get(i).get(rand.nextInt(game.enemyMap.get(i).size()));
                System.out.println("Type string: " + type);
                Enemy enemy = new Enemy();
                if(type.equals("rat")){
                    System.out.println("Spawned rat");
                    enemy = new Rat(tile, game);
                } else if(type.equals("wasp")){
                    System.out.println("Spawned wasp");
                    enemy = new Wasp(tile, game);
                } else if(type.equals("slime")){
                    System.out.println("Spawned slime");
                    enemy = new Slime(tile, game);
                } else if(type.equals("ghost")){
                    System.out.println("Spawned ghost");
                    enemy = new Ghost(tile, game);
                } else if(type.equals("zombie")){
                    System.out.println("Spawned zombie");
                    enemy = new Zombie(tile, game);
                } else if(type.equals("skeleton")){
                    System.out.println("Spawned skeleton");
                    //enemy = new Skeleton(tile, game);
                }
                enemy.diffMod = diffMod;
                enemy.scaleStats();
                enemies.add(enemy);
                tile.getEntities().push(enemy);
                u++;
                if(u > 3) u = 0;
            }
        }
        enemiesNum = enemies.size();
        enemiesToOpen = enemiesNum * 0.2;
        System.out.println("ENEMIES NUM: " + enemiesNum);
        System.out.println("ENEMIES TO OPEN: " + enemiesToOpen);
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
        int c = 120;
        int numItems, itemC;
        for(Zone z : zones){
            numItems = 2 + z.id+rand.nextInt(2);
            for(int i = 0; i < numItems; i++){
                itemC = rand.nextInt(c);
                //TODO flesh out item chances once potion classes are finished
                if(itemC < 20){
                    generateItemUtil(new HealthPotion(20, "potion_health", 10), z);
                } else if(20 <= itemC && itemC < 40 && game.multiplayer){
                    generateItemUtil(new FreezePotion(20, "potion_damage", 5), z);
                } else if(40 <= itemC && itemC < 60){
                    generateItemUtil(new HealthScroll(20, "scroll_health", 5), z);
                } else if(60 <= itemC && itemC < 70){
                    generateItemUtil(new StrengthScroll(20, "scroll_strength", 1), z);
                    System.out.println("STRENGTH SCROLL SPAWNED");
                } else if(70 <= itemC && itemC < 80){
                    generateItemUtil(new ArmorScroll(20, "scroll_armor", 1), z);
                } else if(80 <= itemC && itemC < 100){
                    System.out.println("weapon generated!");
                    generateItemUtil(new sword(20, "sword", 5), z);
                } else if(100 <= itemC && itemC < 120){
                    System.out.println("weapon generated!");
                    generateItemUtil(new Ax(20, "axe", 8), z);
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
        //System.out.println("STR SCROLL SPAWNED AT: " + tile.getPosX() + " " + tile.getPosY());
    }

    public String getSeed() {
        return seed;
    }

    public int getHeight() { return height; }

    public int getWidth() { return width; }
}
