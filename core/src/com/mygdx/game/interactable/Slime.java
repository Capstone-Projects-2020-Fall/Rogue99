package com.mygdx.game.interactable;

import com.mygdx.game.Rogue99;
import com.mygdx.game.item.Item;
import com.mygdx.game.map.Tile;

import java.util.ArrayList;
import java.util.Random;

//lower visibility range, chance to split when hit

public class Slime extends Enemy {

    public Slime(Tile tile, Rogue99 game){
        super(8, 0.45, 1, 2, 40, 3, "slime", tile, game);
    }

    @Override
    public void hit() {
        System.out.println("Slime hit");
        Random rand = new Random();
        double splitChance = Math.random();

        //if slime splits
        if(splitChance > 0.5){
            System.out.println("SLIME SPLIT");
            int x = this.tile.getPosX();
            int y = this.tile.getPosY();

            //get all open adjacent tiles
            Tile open;
            ArrayList<Tile> openList = new ArrayList<>();
            for(int i = -1; i < 2; i++){
                for(int k = -1; k < 2; k++){
                    if(!this.game.level.getMap()[x+i][y+k].getType().equals("wall") &&
                            (this.game.level.getMap()[x+i][y+k].getEntities().empty() ||
                                    this.game.level.getMap()[x+i][y+k].getEntities().peek() instanceof Item)){
                        openList.add(this.game.level.getMap()[x+i][y+k]);
                    }
                }
            }
            //choose random adjacent tile
            System.out.println("OPEN LIST: " + openList.size());
            open = openList.get(rand.nextInt(openList.size()));

            Slime newSlime = new Slime(open, this.game);
            open.getEntities().push(newSlime);
            game.level.enemies.add(newSlime);
        }
    }
}
