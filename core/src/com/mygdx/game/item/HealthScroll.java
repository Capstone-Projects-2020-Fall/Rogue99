package com.mygdx.game.item;

import com.mygdx.game.interactable.Character;

public class HealthScroll extends Item{
    private int hpAmt;

    public HealthScroll(int rarity, String sprite, int hp) {
        super.rarity = rarity;
        super.sprite = sprite;

        hpAmt = hp;
    }
    public void use(Character character) {
        character.setMaxHP(character.getMaxHP() + hpAmt  );
    }
}
