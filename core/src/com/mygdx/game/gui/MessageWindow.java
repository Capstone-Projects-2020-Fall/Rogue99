package com.mygdx.game.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.mygdx.game.Rogue99;

public class MessageWindow extends Window {

    public MessageWindow(final Rogue99 game, final String title, Skin skin, String message) {
        super(title, skin);
        this.setResizable(false);
        this.setMovable(false);
        this.setName(title);
        this.setSize(Gdx.graphics.getWidth()/6, Gdx.graphics.getHeight()/10);
        TextField textField = new TextField(message, skin);
        textField.setAlignment(Align.center);
        textField.setDisabled(true);
        TextButton button = new TextButton("Close",skin);
        button.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                game.removeActor(MessageWindow.this);
            }
        });
        this.add(textField).size(this.getWidth() - 100, this.getHeight()/4).pad(6);
        this.row();
        this.add(button).size(this.getWidth()/6, this.getHeight()/10);
    }
}
