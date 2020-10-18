package com.mygdx.game.interactable;

import com.badlogic.gdx.InputProcessor;

public class Character extends Interactable  {

    private int maxHP;
    private int currHP;
    private int armor;
    private int str;
    private Character character;
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
}
