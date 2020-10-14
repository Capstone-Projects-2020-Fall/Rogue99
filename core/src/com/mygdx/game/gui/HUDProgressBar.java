package com.mygdx.game.gui;

import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class HUDProgressBar extends ProgressBar {

    Skin skin;

    public HUDProgressBar(Skin skin, ProgressBarStyle progressBarStyle){
        super(0,100,1,false,progressBarStyle);
        this.skin = skin;
    }


}
