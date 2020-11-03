package com.mygdx.game.gui;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.mygdx.game.interactable.Hero;

import java.util.ArrayList;

public class GameLobbyGui extends Window {

    private final int NUM_PLAYERS = 36;
    private final int TEXT_WIDTH = 256;
    private final int TEXT_HEIGHT = 54;
    private final int ROW_NUM = 4;

    Skin skin;
    ArrayList<TextField> textFields;

    public GameLobbyGui(String title, Skin skin) {
        super(title, skin);
        this.skin = skin;
        this.setFillParent(true);
        Texture background = new Texture("spritesheets/cave.png");
        Drawable backgroundDrawable = new TextureRegionDrawable(new TextureRegion(background));
        this.setBackground(backgroundDrawable);
        textFields = new ArrayList<>();
        createFields();
    }

    private void createFields(){
        int current_row = 0;
        for(int i = 0; i < NUM_PLAYERS; i++){
            TextField textField = new TextField("", skin);
            textField.setDisabled(true);
            textField.setAlignment(Align.center);
            this.add(textField).size(TEXT_WIDTH, TEXT_HEIGHT).pad(10);
            textFields.add(textField);
            current_row++;
            if(current_row >= ROW_NUM){
                this.row();
                current_row = 0;
            }
        }
    }

    public void addPlayer(Hero player){
        for(int i = 0; i < NUM_PLAYERS; i++){
            if(textFields.get(i).getText().equals("")){
                textFields.get(i).setText(player.getName());
                return;
            }
        }
    }

    public void removePlayer(Hero player){
        for(int i = 0; i < NUM_PLAYERS; i++){
            if(textFields.get(i).getText().equals(player.getName())){
                textFields.get(i).setText("");
                return;
            }
        }
    }

}
