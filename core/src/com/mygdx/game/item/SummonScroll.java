package com.mygdx.game.item;

import com.mygdx.game.interactable.Character;

//Summons an enemy on a random player's map
public class SummonScroll extends Item{
    private int difficulty;

    public SummonScroll(int rarity, String sprite, int difficulty) {
        super.rarity = rarity;
        super.sprite = sprite;

        this.difficulty = difficulty;
    }

    @Override
    public String getSprite() {
        return sprite;
    }

    public boolean use(Character character) { return true; }

    @Override
    public int getId() {
        return Item.SUMMONSCROLL;
    }

    @Override
    public void setEquipped(boolean b) { }

    @Override
    public int getDmgModifier() {
        return 0;
    }

    public int getDifficulty() { return difficulty; }
}
