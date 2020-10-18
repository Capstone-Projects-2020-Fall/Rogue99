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

    public void use(Character character) {
        character.setMaxHP(character.getMaxHP() + hpAmt  );
    }
}
