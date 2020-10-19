package com.mygdx.game.item;

import com.mygdx.game.interactable.Character;

public class ArmorScroll extends Item{
    private int armorAmt;
    private String sprite;

    public ArmorScroll(int rarity, String sprite, int armor) {
        super.rarity = rarity;
        this.sprite = sprite;

        armorAmt = armor;
    }

    @Override
    public String getSprite() {
        return sprite;
    }

    public boolean use(Character character) {
        character.setArmor( character.getArmor() + armorAmt );
        return true;
    }

    @Override
    public int getId() {
        return Item.ARMORSCROLL;
    }

    @Override
    public void setEquipped(boolean b) {

    }

    @Override
    public int getDmgModifier() {
        return 0;
    }
}
