package com.mygdx.game.gui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.mygdx.game.Packets;
import com.mygdx.game.Rogue99;
import com.mygdx.game.interactable.Hero;

import java.util.ArrayList;

public class GameLobbyGui extends Window {

    private final int NUM_PLAYERS = 36;
    private final int TEXT_WIDTH = 256;
    private final int TEXT_HEIGHT = 54;
    private final int ROW_NUM = 4;

    Skin skin;
    ArrayList<TextField> textFields;
    Rogue99 game;

    public GameLobbyGui(Rogue99 game, String title, Skin skin) {
        super(title, skin);
        this.skin = skin;
        this.game = game;
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
        TextButton textButton = new TextButton("Start Game", skin);
        textButton.setColor(Color.DARK_GRAY);
        textButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Packets.Packet010StartGame startGame = new Packets.Packet010StartGame();
                startGame.start = true;
                game.client.client.sendTCP(startGame);
            }
        });
        this.add(textButton).size(TEXT_WIDTH - 100, TEXT_HEIGHT).pad(10).colspan(4);
    }

    public void addPlayer(Hero player){
        for(int i = 0; i < NUM_PLAYERS; i++){
            if(textFields.get(i).getText().equals("")){
                textFields.get(i).setText(player.getName());
                if(!player.getName().equals(game.hero.getName())){
                    Color color = new Color(player.getSpriteColor());
                    color.a = 1;
                    textFields.get(i).setColor(color);
                }
                return;
            }
        }
    }

    public void removePlayer(Hero player){
        for(int i = 0; i < NUM_PLAYERS; i++){
            if(textFields.get(i).getText().equals(player.getName())){
                textFields.get(i).setText("");
                textFields.get(i).setColor(Color.GRAY);
                return;
            }
        }
    }

}
