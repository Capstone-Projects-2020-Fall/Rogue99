package com.mygdx.game.interactable;

import com.mygdx.game.Rogue99;
import com.mygdx.game.map.Tile;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import static java.lang.StrictMath.abs;

public class Enemy extends Character {

    private int difficulty;
    private String sprite;
    private Tile tile;
    private Rogue99 game;

    public Enemy(int difficulty, String sprite, Tile tile, Rogue99 game) {
        this.difficulty = difficulty;
        for (int i = 0; i < difficulty; i++) {
            super.setMaxHP(getMaxHP() + 10);
            super.setArmor(getArmor() + 5);
            super.setStr(getStr() + 2);
        }
        this.setCurrHP(getMaxHP());
        this.sprite = sprite;
        this.tile = tile;
        this.game = game;
    }

    @Override
    public String getSprite() {
        return this.sprite;
    }

    public void moveEnemy(Tile[][] map, int[][] intMap, Hero hero) {
        Pathing aStar = new Pathing(intMap, tile.getPosX(), tile.getPosY(), true);
        List<Pathing.Node> path = aStar.findPathTo(hero.getPosX(), hero.getPosY());
        if (path != null) {
            for (Pathing.Node n : path) {
                System.out.print("[" + n.x + ", " + n.y + "] ");
            }
            Pathing.Node n = path.get(0);
            System.out.print("\nThe enemy is on tile " + "[" + n.x + ", " + n.y + "] \n");
            if (path.size() > 2) {
                n = path.get(1);
                System.out.print("The enemy should move to " + "[" + n.x + ", " + n.y + "] \n\n");
                if( map[n.x][n.y].getEntities().isEmpty() && !(tile.getEntities().isEmpty())) {
                    tile.getEntities().pop();
                    tile = map[n.x][n.y];
                    tile.getEntities().push(this);
                }
            } else{
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
        if (getStr() > hero.getArmor()) {
            hero.setCurrHP(hero.getCurrHP() + hero.getArmor() - getStr());
        }
        System.out.println("enemy health after attack: " + hero.getCurrHP());
        game.changeBarValue(game.HEALTHBAR, hero.getCurrHP());
        game.changeBarValue(game.ARMOURBAR, hero.getArmor());
        game.hudGui.statsNumTexts.get(1).setText(String.valueOf(hero.getCurrHP()));
        game.hudGui.statsNumTexts.get(0).setText(String.valueOf(hero.getArmor()));
    }
}
