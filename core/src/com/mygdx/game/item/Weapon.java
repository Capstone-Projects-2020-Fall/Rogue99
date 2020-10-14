package com.mygdx.game.item;

import com.mygdx.game.interactable.Character;

public class Weapon extends Item {

    public Weapon(int rarity, String sprite, int heal) {
        super.rarity = rarity;
        super.sprite = sprite;
    }

    @Override
    public void use(Character character) {
        character.getCurrHP();
    }
}
