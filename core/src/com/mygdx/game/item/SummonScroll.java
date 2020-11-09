package com.mygdx.game.item;

import com.mygdx.game.interactable.Character;

//Summons an enemy on a random player's map
public class SummonScroll extends Item{
    private String enemyType;

    public SummonScroll(int rarity, String sprite, String type) {
        super.rarity = rarity;
        super.sprite = sprite;

        this.enemyType = type;
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

    public String getEnemyType(){
        return this.enemyType;
    }
}
