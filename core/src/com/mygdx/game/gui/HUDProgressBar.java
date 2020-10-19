package com.mygdx.game.gui;

import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class HUDProgressBar extends ProgressBar {

    Skin skin;
    String name;

    public HUDProgressBar(Skin skin, int maxValue,  ProgressBarStyle progressBarStyle, String name){
        super(0,maxValue,1,false,progressBarStyle);
        this.setSize(10, 10);
        this.skin = skin;
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }
}
