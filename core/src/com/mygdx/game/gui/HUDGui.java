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
    private final int HUD_SIZE = 50 ;

    public HUDGui(Skin skin){
        super("Stats", skin);
        patch = skin.getPatch("default-round");
        this.setResizable(false);
        this.setMovable(false);
        this.scaleBy(1);
        bar = skin.getDrawable("default-slider");
        knob = skin.getDrawable("default-slider-knob");
        ProgressBar.ProgressBarStyle progressBarStyle = new ProgressBar.ProgressBarStyle(bar,knob);
        final HUDProgressBars widgets = new HUDProgressBars(skin, progressBarStyle);
        widgets.setValue(100);
        widgets.addListener(new EventListener() {
            @Override
            public boolean handle(Event event) {
                widgets.setValue(widgets.getValue() - 10);
                return true;
            }
        });
        TextArea textArea = new TextArea("Health",skin);
        textArea.scaleBy(.2f);
        textArea.setAlignment(Align.right);
        textArea.setDisabled(true);
        this.add(textArea);
        this.row();
        this.add(widgets);
    }
}
