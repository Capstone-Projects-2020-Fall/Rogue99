package com.mygdx.game.interactable;

import com.mygdx.game.Rogue99;
import com.mygdx.game.map.Tile;

import java.util.ArrayList;
import java.util.List;
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
        Pathing aStar = new Pathing(intMap, tile.getPosX(), tile.getPosY(), true);
        List<Pathing.Node> path = aStar.findPathTo(hero.getPosX(), hero.getPosY());
        if (path != null) {
            for (Pathing.Node n : path) {
                //System.out.print("[" + n.x + ", " + n.y + "] ");
            }
            Pathing.Node n = path.get(0);
            //System.out.print("\nThe enemy is on tile " + "[" + n.x + ", " + n.y + "] \n");
            if (path.size() > 2 && path.size() <= visRange) {
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
//        if (getStr() > hero.getArmor()) {
//            hero.setCurrHP(hero.getCurrHP() + hero.getArmor() - getStr());
//        }
        System.out.println("enemy health after attack: " + hero.getCurrHP());
        game.setAttacking(true);
        game.changeBarValue(game.HEALTHBAR, hero.getCurrHP());
        game.changeBarValue(game.ARMOURBAR, hero.getArmor());
        game.hudGui.statsNumTexts.get(1).setText(String.valueOf(hero.getCurrHP()));
        game.hudGui.statsNumTexts.get(0).setText(String.valueOf(hero.getArmor()));
    }

    public void hit(){    }
}
