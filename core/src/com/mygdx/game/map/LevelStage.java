package com.mygdx.game.map;

import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.mygdx.game.Rogue99;

public class LevelStage extends Stage {

    private Level level;
    private Rogue99 game;

    public LevelStage(Level level, Rogue99 game) {
        this.level = level;
        this.game = game;
        createActorsForLevel();
    }

    private void createActorsForLevel() {
        for (int x = 0; x < level.getMap().length; x++) {
            for (int y = 0; y < level.getMap()[0].length; y++) {
                Tile tile = level.getMap()[x][y];
                LevelActor actor = new LevelActor(level, tile);
                actor.setBounds(tile.getPosX()*36, tile.getPosY()*36, 36, 36);
                addActor(actor);
                EventListener eventListener = new LevelClickListener(actor, level, game);
                actor.addListener(eventListener);
            }
        }
    }
}