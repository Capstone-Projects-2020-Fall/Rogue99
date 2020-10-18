package com.mygdx.game.item;

import com.mygdx.game.interactable.Character;

public class Potion extends Item{
    private int healAmt;
    private String sprite;

    public Potion(int rarity, String sprite, int heal) {
        super.rarity = rarity;
        this.sprite = sprite;

        healAmt = heal;
    }

    @Override
    public String getSprite() {
        return sprite;
    }

    public void use(Character character) {
            character.setCurrHP(Math.min( character.getMaxHP(), character.getCurrHP() + healAmt ) );
    }
}
