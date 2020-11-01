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
    public NameInputWindow(final Rogue99 game, String title, Skin skin) {
        super(title, skin);
        this.setResizable(true);
        this.setMovable(true);
        this.setName(title);
        this.align(Align.center);
        this.setSize(game.mainMenu.getWidth()/4, game.mainMenu.getHeight()/6);
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
        this.add(textField).size(this.getWidth() - 100, this.getHeight()/4).pad(6);
        this.row();
        this.add(inputField).size(this.getWidth() - 100, this.getHeight()/4).pad(6);
        this.row();
        this.add(button).size(this.getWidth()/6, this.getHeight()/10);
    }
}
