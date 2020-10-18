package com.mygdx.game.item;

import com.mygdx.game.interactable.Character;

public class StrengthScroll extends Item{
    private int strAmt;

    public StrengthScroll(int rarity, String sprite, int str) {
        super.rarity = rarity;
        super.sprite = sprite;

        strAmt = str;
    }
    public boolean use(Character character) {
            character.setStr( character.getStr() + strAmt );
            return true;
    }
}
