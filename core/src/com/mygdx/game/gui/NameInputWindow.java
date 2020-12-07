package com.mygdx.game.gui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.mygdx.game.Rogue99;

public class NameInputWindow extends Window {

    public final int WINDOW_WIDTH = 132;
    public final int WINDOW_HEIGHT = 42;

    public NameInputWindow(final Rogue99 game, String title, Skin skin) {
        super(title, skin);
        this.setResizable(true);
        this.setMovable(true);
        this.setName(title);
        this.setSize(WINDOW_WIDTH*3, WINDOW_HEIGHT*5);
        TextField textField = new TextField("Input a Name", skin);
        textField.setAlignment(Align.center);
        textField.setDisabled(true);
        textField.setColor(Color.DARK_GRAY);
        final TextField inputField = new TextField("", skin);
        inputField.setAlignment(Align.center);
        inputField.setColor(Color.WHITE);
        TextButton button = new TextButton("Set",skin);
        button.setColor(Color.DARK_GRAY);
        button.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                game.setUserName(inputField.getText());
            }
        });
        TextButton closeButton = new TextButton("Close", skin);
        closeButton.setColor(Color.DARK_GRAY);
        closeButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                NameInputWindow.this.remove();
            }
        });
        this.add(textField).size(this.getWidth() - 100, this.getHeight()/4).pad(2);
        this.row();
        this.add(inputField).size(this.getWidth() - 100, this.getHeight()/4).pad(2);
        this.row();
        this.add(button).size(this.getWidth()/6, this.getHeight()/10).pad(2);
        this.row();
        this.add(closeButton).size(this.getWidth()/6, this.getHeight()/10);

    }
}
