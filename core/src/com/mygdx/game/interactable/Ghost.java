package com.mygdx.game.interactable;

import com.mygdx.game.Rogue99;
import com.mygdx.game.map.Tile;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

//moves through walls
public class Ghost extends Enemy {
    public Ghost(Tile tile, Rogue99 game){ super(14, 0.5, 1, 3, 40, 5, "ghost", tile, game);}

    @Override
    public void moveEnemy(Tile[][] map, int[][] intMap, Hero hero) {
        for (int i = 0; i < game.level.getWidth(); i++) {
            for (int k = 0; k < game.level.getHeight(); k++) {
                    intMap[i][k] = 0;
            }
        }

        super.moveEnemy(game.level.getMap(), intMap, hero);
    }

    @Override
    public void observe(String event) {
        if(event.equals("grouped")){
            //TODO set following = true and move toward player/attack
        }
    }
}
