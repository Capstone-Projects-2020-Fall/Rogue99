package com.mygdx.game.interactable;

import com.mygdx.game.Rogue99;
import com.mygdx.game.map.Tile;

//constantly follows player
public class Zombie extends Enemy{
    public Zombie(Tile tile, Rogue99 game){
        super(60, 1,0, 30, 2, "zombie", tile, game);
    }

}
