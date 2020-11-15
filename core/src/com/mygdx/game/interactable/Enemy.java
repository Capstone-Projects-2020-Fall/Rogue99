package com.mygdx.game.interactable;

import com.mygdx.game.Rogue99;
import com.mygdx.game.item.Item;
import com.mygdx.game.map.Tile;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Stack;

import static java.lang.StrictMath.abs;

public class Enemy extends Character {

    public int visRange;
    public int moveDistance;
    public int difficulty;
    public int diffMod; //modifier set when generated, dependent on level it's generated on
    public int baseHp;
    public int baseStr;
    public String sprite;
    public Tile tile;
    public Rogue99 game;

    //states
    public boolean WANDERING = true;
    public boolean FOLLOWING = false;
    public boolean ATTACKING = false;
    public boolean FRIGHTENED = false;
    public int frightened_timer = 0;
    public boolean WAITING = false;

    public Enemy(){}

    public Enemy(int visRange, double hitChance, int moveDistance, int difficulty, int baseHp, int baseStr, String sprite, Tile tile, Rogue99 game) {
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
            //super.setArmor(getArmor() + 2);
            super.setStr(getStr() + 1);
        }
    }

    public void moveEnemy(Tile[][] map, int[][] intMap, Hero hero) {
        if(FRIGHTENED == true){
            retreat(3);
        }

        Pathing aStar = new Pathing(intMap, tile.getPosX(), tile.getPosY(), true);
        List<Pathing.Node> path = aStar.findPathTo(hero.getPosX(), hero.getPosY());
        if (path != null) {
            for (Pathing.Node n : path) {
                //System.out.print("[" + n.x + ", " + n.y + "] ");
            }
            Pathing.Node n = path.get(0);
            //System.out.print("\nThe enemy is on tile " + "[" + n.x + ", " + n.y + "] \n");
            if (path.size() > 2 && (path.size() <= visRange || FOLLOWING == true)) {
                WANDERING = false;
                ATTACKING = false;
                FOLLOWING = true;
                if(path.size() == 3){
                    n = path.get(1);
                } else{
                    n = path.get(moveDistance);
                }
                //System.out.print("The enemy should move to " + "[" + n.x + ", " + n.y + "] \n\n");
                if( map[n.x][n.y].getEntities().isEmpty() && !(tile.getEntities().isEmpty())) {
                    tile.getEntities().pop();
                    tile = map[n.x][n.y];
                    tile.getEntities().push(this);
                }
            } else if(path.size() <= 2){
                this.attack(hero);
                ATTACKING = true;
            } else if(path.size() > visRange){
                WANDERING = true;
                FOLLOWING = false;
                //System.out.println("Enemy location: " + tile.getPosX() + tile.getPosY());
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
                    System.out.println("ENEMY WANDERED");
                    Random rand = new Random();
                    tile.getEntities().pop();
                    tile = openList.get(rand.nextInt(openList.size()));
                    tile.getEntities().push(this);
                }
            }
        }

    }

    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }

    public int getDifficulty() {
        return difficulty;
    }

    public void attack(Hero hero){
        if(Math.random() < getHitChance()){
            System.out.println(this.sprite + " HIT SUCCESSFUL");
            hero.setCurrHP(hero.getCurrHP() + hero.getArmor() - getStr());
        } else{
            System.out.println(this.sprite + " HIT MISSED");
        }

        System.out.println("enemy health after attack: " + hero.getCurrHP());
        if(hero.getCurrHP() > 0){
            game.setAttacking(true);
            game.changeBarValue(game.HEALTHBAR, hero.getCurrHP());
            game.changeBarValue(game.ARMOURBAR, hero.getArmor());
            game.hudGui.statsNumTexts.get(1).setText(String.valueOf(hero.getCurrHP()));
            game.hudGui.statsNumTexts.get(0).setText(String.valueOf(hero.getArmor()));
        }
    }

    public void retreat(int retreatCount){
        if(retreatCount == 0) {
            FRIGHTENED = false;
            moveEnemy(game.level.getMap(), game.level.getIntMap(), game.hero);
            return;
        }

        Random rand = new Random();
        System.out.println(this.sprite + " retreating");
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
            System.out.println("RETREAT SUCCESSFUL");
            open = openList.get(rand.nextInt(openList.size()));
            this.tile.getEntities().pop();
            this.tile = open;
            this.tile.getEntities().push(this);
        }

        retreat(retreatCount--);
    }

    public void hit(){    }

    public void observe(String event){    }
}
