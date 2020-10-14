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
        final HUDProgressBars healthBar = new HUDProgressBars(skin, progressBarStyle);
        healthBar.setValue(100);
        healthBar.addListener(new EventListener() {
            @Override
            public boolean handle(Event event) {
                healthBar.setValue(healthBar.getValue() - 10);
                return true;
            }
        });
        final HUDProgressBars armourBar = new HUDProgressBars(skin, progressBarStyle);
        armourBar.setValue(0);
        armourBar.addListener(new EventListener() {
            @Override
            public boolean handle(Event event) {
                armourBar.setValue(100);
                return true;
            }
        });
        TextArea healthText = new TextArea("Health",skin);
        healthText.scaleBy(.2f);
        healthText.setAlignment(Align.right);
        healthText.setDisabled(true);
        TextArea armourText = new TextArea("Armour",skin);
        armourText.scaleBy(.2f);
        armourText.setAlignment(Align.right);
        armourText.setDisabled(true);
        this.add(healthText).pad(4);
        this.row();
        this.add(healthBar);
        this.row();
        this.add(armourText).pad(4);
        this.row();
        this.add(armourBar);

    }
}
