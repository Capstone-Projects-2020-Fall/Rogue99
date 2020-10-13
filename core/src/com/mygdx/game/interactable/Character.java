package com.mygdx.game.interactable;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;

public class Character extends Interactable implements InputProcessor {

    private int maxHP;
    private int currHP;
    private int armor;
    private int str;
    public boolean up;
    public boolean down;
    public boolean left;
    public boolean right;

    @Override
    public void setPosX(int posX) {
        super.setPosX(posX);
    }

    @Override
    public void setPosY(int posY) {
        super.setPosY(posY);
    }

    public void setArmor(int armor) {
        this.armor = armor;
    }

    public void setCurrHP(int currHP) {
        this.currHP = currHP;
    }

    public void setMaxHP(int maxHP) {
        this.maxHP = maxHP;
    }

    public void setStr(int str) {
        this.str = str;
    }

    @Override
    public int getPosX() {
        return super.getPosX();
    }

    @Override
    public int getPosY() {
        return super.getPosY();
    }


    public int getArmor() {
        return armor;
    }

    public int getCurrHP() {
        return currHP;
    }

    public int getMaxHP() {
        return maxHP;
    }

    public int getStr() {
        return str;
    }

    @Override
    public boolean keyDown(int keycode)
    {
        switch (keycode) {
            case Input.Keys.DOWN:
                down = true;
                break;
            case Input.Keys.UP:
                up = true;
                break;
            case Input.Keys.LEFT:
                left = true;
                break;
            case Input.Keys.RIGHT:
                right = true;
                break;
        }
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        switch (keycode) {
            case Input.Keys.DOWN:
                down = false;
                break;
            case Input.Keys.UP:
                up = false;
                break;
            case Input.Keys.LEFT:
                left = false;
                break;
            case Input.Keys.RIGHT:
                right = false;
                break;
        }
        return false;
    }

    @Override
    //will not use, only keyDown and keyUp
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    //will not use, only keyDown and keyUp
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    //will not use, only keyDown and keyUp
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    //will not use, only keyDown and keyUp
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    //will not use, only keyDown and keyUp
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    //will not use, only keyDown and keyUp
    public boolean scrolled(int amount) {
        return false;
    }
}
