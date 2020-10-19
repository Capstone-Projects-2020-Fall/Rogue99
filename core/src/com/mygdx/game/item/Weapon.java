package com.mygdx.game.item;

import com.mygdx.game.interactable.Character;

public class Weapon extends Item{

    boolean equipped;
    private String sprite;
    private int dmgModifier;

    public Weapon(int rarity, String sprite, int dmgModifier){
        super.rarity = rarity;
        this.sprite = sprite;
        this.dmgModifier = dmgModifier;
    }

    @Override
    public boolean use(Character character) {
        return true;
    }

    @Override
    public int getId() {
        return Item.WEAPON;
    }

    @Override
    public void setEquipped(boolean equipped) {
        this.equipped = equipped;
    }

    public boolean isEquipped() {
        return equipped;
    }

    @Override
    public int getDmgModifier() {
        return dmgModifier;
    }

    @Override
    public String getSprite() {
        return sprite;
    }

    /* weapons sprites
     * s143
     * s252 to s260
     * */
}
