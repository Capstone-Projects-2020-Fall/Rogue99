package com.mygdx.game.item;

import com.mygdx.game.interactable.Character;

//Decreases a random stat for a random player
public class BaneScroll extends Item{
    private String sprite;
    int value;

    public BaneScroll(int rarity, String sprite, int value) {
        super.rarity = rarity;
        this.sprite = sprite;

        this.value = value;
    }

    @Override
    public String getSprite() {
        return sprite;
    }

    public boolean use(Character character) { return true; }

    @Override
    public int getId() {
        return Item.BANESCROLL;
    }

    @Override
    public void setEquipped(boolean b) { }

    @Override
    public int getDmgModifier() {
        return 0;
    }
}
