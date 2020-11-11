package com.mygdx.game.item;

import com.mygdx.game.interactable.Character;

//Makes a player stop moving
public class FreezePotion extends Item{
    private int freeze;

    public FreezePotion(int rarity, String sprite, int freeze) {
        super.rarity = rarity;
        super.sprite = sprite;

        this.freeze = freeze;
    }

    @Override
    public String getSprite() {
        return sprite;
    }

    public boolean use(Character character) { return true; }

    @Override
    public int getId() {
        return Item.FREEZEPOTION;
    }

    @Override
    public void setEquipped(boolean b) { }

    @Override
    public int getDmgModifier() {
        return 0;
    }

    public int getFreeze() {
        return freeze;
    }
}
