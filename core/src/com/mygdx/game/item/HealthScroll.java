package com.mygdx.game.item;

import com.mygdx.game.interactable.Character;

public class HealthScroll extends Item{
    private int hpAmt;
    private String sprite;

    public HealthScroll(int rarity, String sprite, int hp) {
        super.rarity = rarity;
        this.sprite = sprite;

        hpAmt = hp;
    }

    @Override
    public String getSprite() {
        return sprite;
    }

    public boolean use(Character character) {
        character.setMaxHP(character.getMaxHP() + hpAmt  );
        return true;
    }

    @Override
    public int getId() {
        return Item.HEALTHSCROLL;
    }

    @Override
    public void setEquipped(boolean b) {

    }

    @Override
    public int getDmgModifier() {
        return 0;
    }
}
