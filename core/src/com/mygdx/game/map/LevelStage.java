package com.mygdx.game.map;

import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.Stage;

public class LevelStage extends Stage {

    private Level level;

    public LevelStage(Level level) {
        this.level = level;
        createActorsForLevel();
    }

    private void createActorsForLevel() {
        for (int x = 0; x < level.getMap().length; x++) {
            for (int y = 0; y < level.getMap()[0].length; y++) {
                Tile tile = level.getMap()[x][y];
                LevelActor actor = new LevelActor(level, tile);
                // Need To Figure out Sprite Size
                // actor.setBounds(x*spriteWidth, y*spriteHeight, spriteWidth, spriteHeight);
                addActor(actor);
                EventListener eventListener = new LevelClickListener(actor);
                actor.addListener(eventListener);
            }
        }
    }
}