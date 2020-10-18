package com.mygdx.game.item;

import com.mygdx.game.interactable.Character;

public class ArmorScroll extends Item{
    private int armorAmt;

    public ArmorScroll(int rarity, String sprite, int armor) {
        super.rarity = rarity;
        super.sprite = sprite;

        armorAmt = armor;
    }
    public void use(Character character) {
            character.setArmor( character.getArmor() + armorAmt );
    }
}
