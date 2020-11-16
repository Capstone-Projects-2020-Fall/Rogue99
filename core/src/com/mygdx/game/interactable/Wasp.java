package com.mygdx.game.interactable;

import com.mygdx.game.Rogue99;
import com.mygdx.game.item.Item;
import com.mygdx.game.map.Tile;

import java.util.ArrayList;
import java.util.Random;

//moves double the speed as a normal enemy
public class Wasp extends Enemy{

    public Wasp(Tile tile, Rogue99 game){
        super(13, 0.6, 2,1, 20, 3, "wasp", tile, game);
    }

    @Override
    public void hit() {
        Random rand = new Random();
        System.out.println("Wasp hit");

        int thisX = this.tile.getPosX();
        int thisY = this.tile.getPosY();

        int diffX = game.hero.getPosX()-thisX;
        int diffY = game.hero.getPosY()-thisY;
        Tile open;

        if(diffY == 1){
            //move down
            ArrayList<Tile> openList = new ArrayList<>();
            for(int i = -1; i < 2; i++){
                if(!this.game.level.getMap()[thisX + i][thisY - 1].getType().equals("wall") &&
                        (this.game.level.getMap()[thisX + i][thisY - 1].getEntities().empty() ||
                                this.game.level.getMap()[thisX + i][thisY - 1].getEntities().peek() instanceof Item)){
                    openList.add(this.game.level.getMap()[thisX + i][thisY - 1]);
                }
            }
            if(openList.size() >= 1){
                System.out.println("WASP MOVE SUCCESSFUL");
                open = openList.get(rand.nextInt(openList.size()));
                this.tile.getEntities().pop();
                this.tile = open;
                this.tile.getEntities().push(this);
            }
        } else if(diffY == -1){
            //move up
            ArrayList<Tile> openList = new ArrayList<>();
            for(int i = -1; i < 2; i++){
                if(!this.game.level.getMap()[thisX + i][thisY + 1].getType().equals("wall") &&
                        (this.game.level.getMap()[thisX + i][thisY + 1].getEntities().empty() ||
                                this.game.level.getMap()[thisX + i][thisY + 1].getEntities().peek() instanceof Item)){
                    openList.add(this.game.level.getMap()[thisX + i][thisY + 1]);
                }
            }
            if(openList.size() >= 1){
                System.out.println("WASP MOVE SUCCESSFUL");
                open = openList.get(rand.nextInt(openList.size()));
                this.tile.getEntities().pop();
                this.tile = open;
                this.tile.getEntities().push(this);
            }
        } else if(diffX == 1){
            //move left
            ArrayList<Tile> openList = new ArrayList<>();
            for(int i = -1; i < 2; i++){
                if(!this.game.level.getMap()[thisX - 1][thisY + i].getType().equals("wall") &&
                        (this.game.level.getMap()[thisX - 1][thisY + i].getEntities().empty() ||
                                this.game.level.getMap()[thisX - 1][thisY + i].getEntities().peek() instanceof Item)){
                    openList.add(this.game.level.getMap()[thisX - 1][thisY + i]);
                }
            }
            if(openList.size() >= 1){
                System.out.println("WASP MOVE SUCCESSFUL");
                open = openList.get(rand.nextInt(openList.size()));
                this.tile.getEntities().pop();
                this.tile = open;
                this.tile.getEntities().push(this);
            }
        } else if(diffX == -1){
            //move right
            ArrayList<Tile> openList = new ArrayList<>();
            for(int i = -1; i < 2; i++){
                if(!this.game.level.getMap()[thisX + 1][thisY + i].getType().equals("wall") &&
                        (this.game.level.getMap()[thisX + 1][thisY + i].getEntities().empty() ||
                                this.game.level.getMap()[thisX + 1][thisY + i].getEntities().peek() instanceof Item)){
                    openList.add(this.game.level.getMap()[thisX + 1][thisY + i]);
                }
            }
            if(openList.size() >= 1){
                System.out.println("WASP MOVE SUCCESSFUL");
                open = openList.get(rand.nextInt(openList.size()));
                this.tile.getEntities().pop();
                this.tile = open;
                this.tile.getEntities().push(this);
            }
        }
    }

//    @Override
//    public void moveEnemy(Tile[][] map, int[][] intMap, Hero hero) {
//        super.moveEnemy(map, intMap, hero);
//    }
}
