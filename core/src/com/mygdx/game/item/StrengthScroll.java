package com.mygdx.game.item;

import com.mygdx.game.interactable.Character;

public class StrengthScroll extends Item{
    private int strAmt;
    private String sprite;

    public StrengthScroll(int rarity, String sprite, int str) {
        super.rarity = rarity;
        this.sprite = sprite;

        strAmt = str;
    }
    public boolean use(Character character) {
            character.setStr( character.getStr() + strAmt );
            System.out.println("STRENGTH CHANGED");
            return true;
    }

    @Override
    public String getSprite() {
        return sprite;
    }

    @Override
    public int getId() {
        return Item.STRENGTHSCROLL;
    }

    @Override
    public void setEquipped(boolean b) {

    }

    @Override
    public int getDmgModifier() {
        return 0;
    }
}
