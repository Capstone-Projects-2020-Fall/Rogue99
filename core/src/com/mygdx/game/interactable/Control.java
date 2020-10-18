package com.mygdx.game.interactable;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputProcessor;
import com.mygdx.game.Rogue99;

public class Control extends InputAdapter implements InputProcessor {

    Hero hero;
    Rogue99 game;

    public Control(Hero hero, Rogue99 game){
        this.hero = hero;
        this.game = game;
    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        switch (keycode) {
            case Input.Keys.DOWN:
                System.out.println("DOWN!");
                hero.update(hero.DOWN);
                break;
            case Input.Keys.UP:
                System.out.println("UP!");
                hero.update(hero.UP);
                break;
            case Input.Keys.LEFT:
                System.out.println("LEFT!");
                hero.update(hero.LEFT);
                break;
            case Input.Keys.RIGHT:
                System.out.println("RIGHT!");
                hero.update(hero.RIGHT);
                break;
            case Input.Keys.I:
                System.out.println("I");
                if(game.isShowInventory()){
                    game.setShowInventory(false);
                } else {
                    game.setShowInventory(true);
                }
                break;
        }
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        System.out.println(screenX + " " + screenY + " " + pointer + " " + button);
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}
