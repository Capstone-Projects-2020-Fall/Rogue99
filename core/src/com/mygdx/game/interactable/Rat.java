package com.mygdx.game.interactable;

import com.mygdx.game.Rogue99;
import com.mygdx.game.map.Tile;

//basic enemy, moves and attacks like normal

public class Rat extends Enemy {

    public Rat(Tile tile, Rogue99 game){
        super(10, 0.7, 1, 0, 10, 2, "rat", tile, game);
        super.mobs = true;
        super.mobbingNumber = 3;
    }
}
