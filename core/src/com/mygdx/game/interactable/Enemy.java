package com.mygdx.game.interactable;

import com.mygdx.game.map.Tile;

import java.util.ArrayList;

public class Enemy extends Character {

    private int difficulty;
    private String sprite;
    private Tile tile;

    public Enemy(int difficulty, String sprite, Tile tile) {
        this.difficulty = difficulty;
        for (int i = 0; i < difficulty; i++) {
            super.setMaxHP(getMaxHP() + 10);
            super.setArmor(getArmor() + 5);
            super.setStr(getStr() + 2);
        }
        this.sprite = sprite;
        this.tile = tile;
    }

    @Override
    public String getSprite() {
        return this.sprite;
    }

    public void moveEnemy(Tile target, Tile[][] map){
        ArrayList<Tile> open = new ArrayList<>();
        ArrayList<Tile> closed = new ArrayList<>();

        this.tile.f = 0;
        open.add(this.tile);

        while(!open.isEmpty()){
            int ind = 0;
            int leastF = 0;
            for(int i = 0; i < open.size(); i++){
                if(open.get(i).f >= leastF){
                    ind = i;
                }
            }
            Tile q = open.remove(ind);

            //generate q's successors
            ArrayList<Tile> successors = new ArrayList<>();
            for(int m = -1; m < 2; m++){
                for(int n = -1; n < 2; n++){
                    successors.add(map[q.getPosX()-m][q.getPosY()-n]);
                    map[q.getPosX()-m][q.getPosY()-n].parent = q;
                }
            }

            for(Tile s : successors){

            }
        }
    }

    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }

    public int getDifficulty() {
        return difficulty;
    }
}
