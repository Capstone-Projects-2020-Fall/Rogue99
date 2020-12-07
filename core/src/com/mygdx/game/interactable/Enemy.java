package com.mygdx.game.interactable;

import com.mygdx.game.Packets;
import com.mygdx.game.Rogue99;
import com.mygdx.game.item.Item;
import com.mygdx.game.map.Tile;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Enemy extends Character {

    public int visRange;
    public int moveDistance;
    public int difficulty;
    public int diffMod; //modifier set when generated, dependent on level it's generated on
    public String sprite;
    public Tile tile;
    public Rogue99 game;

    //states
    public boolean WANDERING = true;
    public boolean FOLLOWING = false;
    public boolean ATTACKING = false;
    public boolean FRIGHTENED = false;
    public boolean mobs = false;
    public int mobbingNumber = 0;

    public Enemy(){}

    public Enemy(int visRange, double hitChance, int moveDistance, int difficulty,
                 int baseHp, int baseStr, String sprite, Tile tile, Rogue99 game) {
        this.visRange = visRange;
        this.moveDistance = moveDistance;
        this.difficulty = difficulty;
        diffMod = 0;
        super.setMaxHP(baseHp);
        super.setStr(baseStr);
        super.setHitChance(hitChance);
        this.setCurrHP(getMaxHP());
        this.sprite = sprite;
        this.tile = tile;
        this.game = game;
    }

    @Override
    public String getSprite() {
        return this.sprite;
    }

    public void scaleStats(){
        for (int i = 0; i < diffMod; i++) {
            super.setMaxHP(getMaxHP() + 5);
            super.setStr(getStr() + 1);
        }
    }

    public int moveEnemy(Tile[][] map, int[][] intMap, int enemiesInRange, Hero hero) {
        if(FRIGHTENED == true){
            retreat(3);
        }

        Pathing aStar = new Pathing(intMap, tile.getPosX(), tile.getPosY(), true);
        List<Pathing.Node> path = aStar.findPathTo(hero.getPosX(), hero.getPosY());
        if (path != null) {
            for (Pathing.Node n : path) {

            }
            Pathing.Node n = path.get(0);

            if (path.size() > 2 && (path.size() <= visRange || FOLLOWING == true)) {
                if(this.mobs == true && game.aiEnabled){
                    if((enemiesInRange > this.mobbingNumber && path.size() < 8) || path.size() >= 8) {
                        moveTowards(map, path, n);
                    } else {
                        retreat(1);
                    }
                } else{
                    moveTowards(map, path, n);
                }
            } else if(path.size() <= 2){
                this.attack(hero);
                ATTACKING = true;
            } else if(path.size() > visRange){
                WANDERING = true;
                FOLLOWING = false;
                ArrayList<Tile> openList = new ArrayList<>();
                for(int i = -1; i < 2; i++){
                    for(int k = -1; k < 2; k++){
                        if(!map[tile.getPosX() + i][tile.getPosY() + k].getType().equals("wall") &&
                                (!map[tile.getPosX() + i][tile.getPosY() + k].getEntities().empty() && map[tile.getPosX() + i][tile.getPosY() + k].getEntities().peek() instanceof Enemy) &&
                                (i != 0 && k != 0)){
                            openList.add(map[tile.getPosX() + i][tile.getPosY() + k]);
                        }
                    }
                }
                if(openList.size() != 0){
                    Random rand = new Random();
                    game.level.intMap[tile.getPosX()][tile.getPosY()] = 0;
                    tile.getEntities().pop();
                    tile = openList.get(rand.nextInt(openList.size()));
                    tile.getEntities().push(this);
                    game.level.intMap[tile.getPosX()][tile.getPosY()] = -1;
                }
            }
            if(path.size() < 10) {
                return 1;
            }
            else {
                return 0;
            }
        }
        return 0;
    }

    private void moveTowards(Tile[][] map, List<Pathing.Node> path, Pathing.Node n){
        WANDERING = false;
        ATTACKING = false;
        FOLLOWING = true;
        if (path.size() == 3) {
            n = path.get(1);
        } else {
            n = path.get(moveDistance);
        }
        //System.out.print("The enemy should move to " + "[" + n.x + ", " + n.y + "] \n\n");
        if (!(tile.getEntities().isEmpty())) {
            game.level.intMap[tile.getPosX()][tile.getPosY()] = 0;
            game.level.intMap[n.x][n.y] = -1;
            tile.getEntities().pop();
            tile = map[n.x][n.y];
            tile.getEntities().push(this);
        }
    }

    public void attack(Hero hero){
        if(Math.random() < getHitChance()){
            if( getStr() - hero.getArmor() > 0 ) {
                hero.takeDamage(getStr() - hero.getArmor());
            }
            else {
                hero.takeDamage(1);
            }
        } else{
        }
        if(hero.getCurrHP() > 0){
            game.setAttacking(true);
            game.getScoreboard().getPlayerScore().setText("Score: " + game.hero.score + " Health: " + game.hero.getCurrHP()
                    + " Armor: " + game.hero.getArmor() + " Level: "+ game.hero.depth);
            if (game.multiplayer){
                Packets.Packet005Stats stats = new Packets.Packet005Stats();
                stats.name = hero.getName();
                stats.score = hero.score;
                stats.health = hero.getCurrHP();
                stats.armor = hero.getArmor();
                stats.depth = hero.depth;
                game.client.client.sendTCP(stats);
            }
        }
    }

    public void retreat(int retreatCount){
        if(retreatCount == 0) {
            FRIGHTENED = false;
            //moveEnemy(game.level.getMap(), game.level.getIntMap(), game.level.getEnemiesInRange(), game.hero);
            return;
        }

        Random rand = new Random();
        int thisX = this.tile.getPosX();
        int thisY = this.tile.getPosY();

        int diffX = game.hero.getPosX()-thisX;
        int diffY = game.hero.getPosY()-thisY;
        Tile open;

        ArrayList<Tile> openList = new ArrayList<>();
        if(diffY >= 1){
            //move down
            for(int i = -1; i < 2; i++){
                if(!this.game.level.getMap()[thisX + i][thisY - 1].getType().equals("wall") &&
                        (this.game.level.getMap()[thisX + i][thisY - 1].getEntities().empty() ||
                                this.game.level.getMap()[thisX + i][thisY - 1].getEntities().peek() instanceof Item)){
                    openList.add(this.game.level.getMap()[thisX + i][thisY - 1]);
                }
            }
        } else if(diffY <= -1){
            //move up
            for(int i = -1; i < 2; i++){
                if(!this.game.level.getMap()[thisX + i][thisY + 1].getType().equals("wall") &&
                        (this.game.level.getMap()[thisX + i][thisY + 1].getEntities().empty() ||
                                this.game.level.getMap()[thisX + i][thisY + 1].getEntities().peek() instanceof Item)){
                    openList.add(this.game.level.getMap()[thisX + i][thisY + 1]);
                }
            }
        } else if(diffX >= 1){
            //move left
            for(int i = -1; i < 2; i++){
                if(!this.game.level.getMap()[thisX - 1][thisY + i].getType().equals("wall") &&
                        (this.game.level.getMap()[thisX - 1][thisY + i].getEntities().empty() ||
                                this.game.level.getMap()[thisX - 1][thisY + i].getEntities().peek() instanceof Item)){
                    openList.add(this.game.level.getMap()[thisX - 1][thisY + i]);
                }
            }
        } else if(diffX <= -1){
            //move right
            for(int i = -1; i < 2; i++){
                if(!this.game.level.getMap()[thisX + 1][thisY + i].getType().equals("wall") &&
                        (this.game.level.getMap()[thisX + 1][thisY + i].getEntities().empty() ||
                                this.game.level.getMap()[thisX + 1][thisY + i].getEntities().peek() instanceof Item)){
                    openList.add(this.game.level.getMap()[thisX + 1][thisY + i]);
                }
            }
        }

        if(openList.size() >= 1){
            open = openList.get(rand.nextInt(openList.size()));
            this.tile.getEntities().pop();
            this.tile = open;
            this.tile.getEntities().push(this);
        }

        retreat(--retreatCount);
    }

    public void hit(){    }

    public void observe(String event){    }
}
