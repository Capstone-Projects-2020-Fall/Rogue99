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
        switch (keycode) {
            case Input.Keys.DOWN:
            case Input.Keys.S:
                hero.update(hero.DOWN);
                break;
            case Input.Keys.UP:
            case Input.Keys.W:
                hero.update(hero.UP);
                break;
            case Input.Keys.LEFT:
            case Input.Keys.A:
                hero.update(hero.LEFT);
                break;
            case Input.Keys.RIGHT:
            case Input.Keys.D:
                hero.update(hero.RIGHT);
                break;
            case Input.Keys.Q:
                hero.update(hero.NUMPAD_7);
                break;
            case Input.Keys.E:
                hero.update(hero.NUMPAD_9);
                break;
            case Input.Keys.Z:
                hero.update(hero.NUMPAD_1);
                break;
            case Input.Keys.C:
                hero.update(hero.NUMPAD_3);
                break;
        }
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        switch (keycode){
            case Input.Keys.I:
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
