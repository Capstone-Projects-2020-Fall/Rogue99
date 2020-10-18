package com.mygdx.game.item;

import com.mygdx.game.interactable.Character;

public class Potion extends Item{
    private int healAmt;

    public Potion(int rarity, String sprite, int heal) {
        super.rarity = rarity;
        super.sprite = sprite;

        healAmt = heal;
    }
    public void use(Character character) {
            character.setCurrHP(Math.min( character.getMaxHP(), character.getCurrHP() + healAmt ) );
    }
}
