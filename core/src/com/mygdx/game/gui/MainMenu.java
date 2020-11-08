package com.mygdx.game.gui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.mygdx.game.Rogue99;

public class MainMenu extends Window {

    final int BUTTONS_NUM = 3;

    public MainMenu(final Rogue99 game, String title, Skin skin) {
        super(title, skin);
        this.setFillParent(true);
        Texture background = new Texture("spritesheets/cave.png");
        Drawable backgroundDrawable = new TextureRegionDrawable(new TextureRegion(background));
        this.setBackground(backgroundDrawable);
        String[] buttonNames = {"Single Player", "Multiplayer", "Exit"};
        for (int i = 0; i < BUTTONS_NUM; i++){
            final TextButton button = new TextButton(buttonNames[i], skin);
            button.setName(buttonNames[i]);
            button.setColor(Color.DARK_GRAY);
            button.addListener(new ClickListener(){
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    game.menuButtonClicked(button.getName());
                    super.clicked(event, x, y);
                }
            });
            this.add(button).size(getWidth(), getHeight()/4).pad(6);
            this.row();
        }
    }

}