package com.mygdx.game.interactable;

public class Character extends Interactable  {

    private int maxHP;
    private int currHP;
    private int armor;
    private int str;
    private double hitChance;

    public final int DOWN = 0;
    public final int UP = 1;
    public final int LEFT = 2;
    public final int RIGHT = 3;
    
    public final int UP_LEFT = 4;
    public final int UP_RIGHT = 5;
    public final int DOWN_LEFT = 6;
    public final int DOWN_RIGHT = 7;

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

    public void setHitChance(double hitChance){
        this.hitChance = hitChance;
    }

    public double getHitChance(){
        return hitChance;
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
