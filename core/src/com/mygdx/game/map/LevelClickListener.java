package com.mygdx.game.map;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.mygdx.game.Rogue99;
import com.mygdx.game.interactable.Pathing;

import java.util.List;

public class LevelClickListener extends ClickListener{

    private LevelActor actor;
    private Level level;
    private Rogue99 game;

    public LevelClickListener(LevelActor actor, Level level, Rogue99 game) {
        this.level = level;
        this.actor = actor;
        this.game = game;

    }

    @Override
    public void clicked(InputEvent event, float x, float y) {
        System.out.println("A tile has been clicked. [" + actor.getTile().getPosX() + "][" + actor.getTile().getPosY() + "]");
        Pathing aStar = new Pathing(level.getIntMap(), (int) x, (int) y, true);
        List<Pathing.Node> path = aStar.findPathTo(game.getHero().getPosX(), game.getHero().getPosY());
        if (path.size() < 3) {
            game.getHero().attack( (int) x, (int) y);
            game.setRangeMode(false);
        }

    }

}
