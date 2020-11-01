package com.mygdx.game.gui;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.mygdx.game.Rogue99;

public class MessageWindow extends Window {

    public final int WINDOW_WIDTH = 132;
    public final int WINDOW_HEIGHT = 42;

    public MessageWindow(final Rogue99 game, final String title, Skin skin, String message) {
        super(title, skin);
        this.setResizable(false);
        this.setMovable(false);
        this.setName(title);
        this.setSize(WINDOW_WIDTH*3, WINDOW_HEIGHT*3);
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
        this.add(textField).size(this.getWidth(), this.getHeight()/4).pad(6);
        this.row();
        this.add(button).size(this.getWidth()/4, this.getHeight()/4);
    }
}
