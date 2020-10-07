package com.mygdx.game.map;

import com.badlogic.gdx.scenes.scene2d.Actor;

public class LevelActor extends Actor {

    private Level level;

    private Tile tile;

    public LevelActor(Level level, Tile tile) {
        this.level = level;
        this.tile = tile;
    }

}
