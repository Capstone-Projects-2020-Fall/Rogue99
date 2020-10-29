package com.mygdx.game.item;

import com.mygdx.game.interactable.Character;

public class DamagePotion extends Item{
    private int dmgAmt;
    private String sprite;

    public DamagePotion(int rarity, String sprite, int dmg) {
        super.rarity = rarity;
        this.sprite = sprite;
        dmgAmt = dmg;
    }

    @Override
    public String getSprite() {
        return sprite;
    }

    public boolean use(Character character) {
        return true;
    }

    @Override
    public int getId() {
        return Item.DAMAGEPOTION;
    }

    @Override
    public void setEquipped(boolean b) {

    }

    @Override
    public int getDmgModifier() {
        return 0;
    }

    public int getDmgAmt() {
        return dmgAmt;
    }
}
