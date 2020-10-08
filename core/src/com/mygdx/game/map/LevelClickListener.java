package com.mygdx.game.map;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class LevelClickListener extends ClickListener{

    private LevelActor actor;

    public LevelClickListener(LevelActor actor) {
        this.actor = actor;
    }

    @Override
    public void clicked(InputEvent event, float x, float y) {
        System.out.println("A tile has been clicked. [" + actor.getTile().getPosX() + "][" + actor.getTile().getPosY() + "]");
    }

}
