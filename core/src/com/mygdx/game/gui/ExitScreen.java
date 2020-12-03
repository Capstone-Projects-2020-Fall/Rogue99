package com.mygdx.game.gui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.mygdx.game.Rogue99;
import org.w3c.dom.Text;

public class ExitScreen extends Window {
    public final int WINDOW_WIDTH = 60;
    public final int WINDOW_HEIGH = 80;
    String[] buttonNames = {"Resume", "Enable AI", "Main Menu", "Exit"};

    public ExitScreen(final Rogue99 game, String title, Skin skin) {
        super(title, skin);
        this.setResizable(false);
        this.setMovable(false);
        this.setName(title);
        this.setSize(WINDOW_WIDTH*3, WINDOW_HEIGH*3);
        //String[] buttonNames = {"Resume", "Disable AI", "Main Menu", "Exit"};



        for(int i = 0; i < buttonNames.length; i++) {
            if(buttonNames[i].equals("Enable AI")){
                final CheckBox checkBox = new CheckBox("Enable AI", skin);
                checkBox.setChecked(true);
                checkBox.addListener(new ChangeListener(){
                    @Override
                    public void changed(ChangeEvent changeEvent, Actor actor) {
                        if(checkBox.isChecked()){
                            game.aiEnabled = true;
                            System.out.println(game.aiEnabled);
                        } else{
                            game.aiEnabled = false;
                            System.out.println(game.aiEnabled);
                        }
                    }
                });
                this.add(checkBox);
                this.row();
            } else{
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
                this.add(button).size(getWidth()/1.5f, getHeight()/6).pad(6);
                this.row();
            }
        }
    }

    public void changeAIButton(boolean newState){
        if(newState){
            buttonNames[1] = "Enable AI";
        }
    }
}
