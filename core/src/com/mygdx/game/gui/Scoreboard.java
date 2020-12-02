package com.mygdx.game.gui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.utils.Align;
import com.mygdx.game.Rogue99;
import com.mygdx.game.interactable.Hero;

import java.util.ArrayList;

public class Scoreboard extends Window {

    public final int WINDOW_WIDTH = 132;
    public final int WINDOW_HEIGHT = 42;
    Skin skin;
    Rogue99 game;
    TextField playerScore;
    ArrayList<TextField> scores;

    public Scoreboard(Skin skin, Rogue99 game) {
        super("Scoreboard", skin);
        this.skin = skin;
        this.game = game;
        this.setResizable(false);
        this.setMovable(false);
        init_scoreboard();
    }


    private void init_scoreboard(){
        playerScore = new TextField("Score: " + game.hero.score + " Health: " + game.hero.getCurrHP()
                + " Armor: " + game.hero.getArmor() + " Level: "+ game.hero.depth, skin);
        playerScore.setAlignment(Align.center);
        playerScore.setDisabled(true);
        scores = new ArrayList<>();
        this.setSize(WINDOW_WIDTH*3, WINDOW_HEIGHT*3);
        this.add(playerScore).size(this.getWidth(), (WINDOW_HEIGHT*3)/4);
    }

    public TextField getPlayerScore() {
        return playerScore;
    }

    public void changePlayerScore(String name, int score, int health, int armor, int depth){
        for(TextField textField : scores){
            if(textField.getName().equals(name)){
                textField.setText(name + "'s Score: " + score + " Health: " + health + " Armor: " + armor + " Level: "+ depth);
            }
        }
    }

    public void addPlayer(Hero player){
        TextField textField = new TextField(player.getName() + "'s Score: " + player.score + " Health: " + player.getCurrHP()
                + " Armor: " + player.getArmor() + " Level: "+ player.depth, skin);
        Color color = new Color(player.getSpriteColor());
        color.a = 1;
        textField.setColor(color);
        textField.setAlignment(Align.center);
        textField.setName(player.getName());
        scores.add(textField);
        this.row();
        this.add(textField).size(this.getWidth(), (WINDOW_HEIGHT*3)/4);
    }
}
