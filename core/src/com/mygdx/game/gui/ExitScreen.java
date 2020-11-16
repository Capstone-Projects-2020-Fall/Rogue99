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
import org.w3c.dom.Text;

public class ExitScreen extends Window {
    public final int WINDOW_WIDTH = 60;
    public final int WINDOW_HEIGH = 80;

    public ExitScreen(final Rogue99 game, String title, Skin skin) {
        super(title, skin);
        this.setResizable(false);
        this.setMovable(false);
        this.setName(title);
        this.setSize(WINDOW_WIDTH*3, WINDOW_HEIGH*3);
        String[] buttonNames = {"Resume", "Main Menu", "Exit"};



        for(int i = 0; i < buttonNames.length; i++) {
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
