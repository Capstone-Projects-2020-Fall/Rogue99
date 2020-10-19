package com.mygdx.game.gui;

import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Align;

import java.util.ArrayList;
import java.util.Map;

public class HUDGui extends Window {

    private NinePatch patch;
    private Drawable bar;
    private Drawable knob;
    private final int HUD_SIZE = 26 ;
    private final int HUD_WINDOW_WIDTH_OFFSET = 30;
    private final int HUD_WINDOW_HEIGHT_OFFSET = 80;
    public int Window_Width;
    public ArrayList<HUDProgressBar> hudBars;
    public ArrayList<TextField> statsNumTexts;

    public HUDGui(Skin skin, Map<String, Integer> bars){
        super("Stats", skin);
        patch = skin.getPatch("default-round");
        this.setResizable(false);
        this.setMovable(false);
        this.align(Align.center);
        this.setSize(HUD_SIZE * 3 + HUD_WINDOW_WIDTH_OFFSET, HUD_SIZE * (bars.size() + 1) + HUD_WINDOW_HEIGHT_OFFSET);
        Window_Width = HUD_SIZE * 3 + HUD_WINDOW_WIDTH_OFFSET;
        bar = skin.getDrawable("default-slider");
        knob = skin.getDrawable("default-slider-knob");
        ProgressBar.ProgressBarStyle progressBarStyle = new ProgressBar.ProgressBarStyle(bar,knob);
        hudBars = new ArrayList<>();
        statsNumTexts = new ArrayList<>();

        for(String name: bars.keySet()){
            HUDProgressBar bar = CreateStatBar(name, bars.get(name), 100,skin,progressBarStyle);
            hudBars.add(bar);
        }
    }

    private HUDProgressBar CreateStatBar(String name, float defaultValue,int maxValue , Skin skin, ProgressBar.ProgressBarStyle progressBarStyle){
        HUDProgressBar bar = new HUDProgressBar(skin, maxValue,progressBarStyle, name);
        bar.setValue(defaultValue);
        TextField textField = new TextField(name, skin);
        textField.setAlignment(Align.center);
        textField.setDisabled(true);

        TextField numTextfield = new TextField(String.valueOf(defaultValue), skin);
        numTextfield.setAlignment(Align.center);
        numTextfield.setDisabled(true);
        statsNumTexts.add(numTextfield);

        this.add(textField).size(82,20).pad(4);
        this.row();
        this.add(numTextfield).size(72,20).pad(1);
        this.row();
        this.add(bar).size(84,10);
        this.row();
        return bar;
    }

    public ArrayList<HUDProgressBar> getHudBars() {
        return hudBars;
    }
}
