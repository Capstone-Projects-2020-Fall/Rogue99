package com.mygdx.game.gui;

import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class HUDProgressBar extends ProgressBar {

    Skin skin;
    String name;

    public HUDProgressBar(Skin skin, ProgressBarStyle progressBarStyle, String name){
        super(0,100,1,false,progressBarStyle);
        this.skin = skin;
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }
}
