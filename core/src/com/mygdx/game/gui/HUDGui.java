package com.mygdx.game.gui;

import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Align;

public class HUDGui extends Window {

    private NinePatch patch;
    private Drawable bar;
    private Drawable knob;
    private final int HUD_SIZE = 48 ;
    private final int HUD_WINDOW_HEIGHT_OFFSET = 30;

    public HUDGui(Skin skin){
        super("Stats", skin);
        patch = skin.getPatch("default-round");
        this.setResizable(false);
        this.setMovable(false);
        this.setSize(HUD_SIZE * 3 + HUD_WINDOW_HEIGHT_OFFSET, HUD_SIZE * 3);
        this.scaleBy(1);
        bar = skin.getDrawable("default-slider");
        knob = skin.getDrawable("default-slider-knob");
        ProgressBar.ProgressBarStyle progressBarStyle = new ProgressBar.ProgressBarStyle(bar,knob);

        HUDProgressBar healthBar = CreateStatBar("Health", 100,skin,progressBarStyle);
        HUDProgressBar armourBar = CreateStatBar("Armour", 0,skin,progressBarStyle);

        healthBar.addListener(new EventListener() {
            @Override
            public boolean handle(Event event) {
                return false;
            }
        });

        armourBar.addListener(new EventListener() {
            @Override
            public boolean handle(Event event) {
                return false;
            }
        });
    }

    private HUDProgressBar CreateStatBar(String name, float defaultValue, Skin skin, ProgressBar.ProgressBarStyle progressBarStyle){
        HUDProgressBar bar = new HUDProgressBar(skin ,progressBarStyle);
        bar.setValue(defaultValue);
        TextField textField = new TextField(name, skin);
        textField.scaleBy(.2f);
        textField.setAlignment(Align.center);
        textField.setDisabled(true);
        this.add(textField).pad(4);
        this.row();
        this.add(bar);
        this.row();
        return bar;
    }
}
