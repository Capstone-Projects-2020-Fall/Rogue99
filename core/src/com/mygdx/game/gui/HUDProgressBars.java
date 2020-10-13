package com.mygdx.game.gui;

import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class HUDProgressBars extends ProgressBar {

    Skin skin;

    public HUDProgressBars(Skin skin, ProgressBarStyle progressBarStyle){
        super(0,100,1,false,progressBarStyle);
        this.skin = skin;
    }


}
