package com.mygdx.game.interactable;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputProcessor;

public class Control extends InputAdapter implements InputProcessor {

    Hero hero;

    public Control(Hero hero){
        this.hero = hero;
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
