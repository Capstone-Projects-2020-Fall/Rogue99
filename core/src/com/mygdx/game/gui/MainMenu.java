package com.mygdx.game.gui;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.mygdx.game.Rogue99;


public class MainMenu extends ApplicationAdapter {

    private Rogue99 gameScreen;
    private SpriteBatch sb;
    private Texture bg;
    private Texture play;
    private Texture play_hover;
    private Texture multi;
    private Texture multi_hover;
    private Texture setting;
    private Texture setting_hover;
    private Texture exit;
    private Texture exit_hover;
    private Texture Title;

    private Sprite sp;
    public static int WIDTH;
    public static int HEIGHT;

    public static OrthographicCamera cam;

    private final String title = "Rogue99";


    private int currentItem;
    private String[] menuItems;

    @Override
    public void create() {
        sb = new SpriteBatch();
        WIDTH = Gdx.graphics.getWidth();
        HEIGHT = Gdx.graphics.getHeight();
        cam = new OrthographicCamera(WIDTH, HEIGHT);
        cam.translate(WIDTH/2, HEIGHT/2);
        cam.update();
        bg = new Texture("spritesheets/cave.png");
        sp = new Sprite(bg);
        play = new Texture("spritesheets/play.png");
        play_hover = new Texture("spritesheets/play_hover.png");
        multi = new Texture("spritesheets/multiplay.png");
        multi_hover = new Texture("spritesheets/mutliplay_hover.png");
        setting = new Texture("spritesheets/setting.png");
        setting_hover = new Texture("spritesheets/setting_hover.png");
        exit = new Texture("spritesheets/exit.png");
        exit_hover = new Texture("spritesheets/exit_hover.png");
        Title = new Texture("spritesheets/title.png");


    }

    @Override
    public void render() {
        // clears screen to black
        Gdx.gl.glClearColor(0,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        sb.begin();
        sp.draw(sb);
        sb.draw(play, WIDTH/2 - 100 / 2, 350, 100, 100);
        sb.draw(multi, WIDTH/2 - 170/2, 290, 170, 100);
        sb.draw(setting, WIDTH/2 - 170/2, 230, 170, 100);
        sb.draw(exit, WIDTH/2 - 100/2, 170, 100, 100);
        sb.draw(Title, WIDTH/2 - 500/2, 600, 500, 400);

        if (Gdx.input.getX() < WIDTH/2 - 100/2 + 100 && Gdx.input.getX() > WIDTH/2 -100/2 && HEIGHT - Gdx.input.getY() <
        350 + 100 && HEIGHT - Gdx.input.getY() > 350) { //cuts screen to make active when in tha zone
            sb.draw(play_hover,WIDTH/2 - 100 / 2, 350, 100, 100);
            if(Gdx.input.isTouched()) {
                dispose();
                gameScreen.create();

            }

        } else if (Gdx.input.getX() < WIDTH/2 - 170/2 + 170 && Gdx.input.getX() > WIDTH/2 - 170/2 && HEIGHT - Gdx.input.getY() <
        290 + 100 && HEIGHT - Gdx.input.getY() > 290) {
            sb.draw(multi_hover, WIDTH/2 - 170/2, 290, 170, 100);
        } else if(Gdx.input.getX() < WIDTH/2 - 170/2 + 170 && Gdx.input.getX() > WIDTH/2 - 170/2 && HEIGHT - Gdx.input.getY() <
        230 + 100 && HEIGHT - Gdx.input.getY() > 230) {
            sb.draw(setting_hover, WIDTH/2 - 170/2, 230, 170, 100);
        } else if(Gdx.input.getX() <WIDTH/2 - 100/2 + 100 && Gdx.input.getX() > WIDTH/2 - 100/2 && HEIGHT - Gdx.input.getY() <
        170 + 100 && HEIGHT - Gdx.input.getY() > 170) {
            sb.draw(exit_hover, WIDTH/2 - 100/2, 170, 100, 100);
            if(Gdx.input.isTouched()) {
                Gdx.app.exit();
            }
        }


        sb.end();
    }
    @Override
    public void dispose() {
        sb.dispose();

    }

}
