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

    public boolean use(Character character) {
        if(character.getCurrHP() == character.getMaxHP()){
            // return to inventory
            return false;
        } else if(character.getCurrHP() + healAmt > character.getMaxHP()) {
            character.setCurrHP(character.getMaxHP());
        }else {
            character.setCurrHP(character.getCurrHP() + healAmt);
        }
        return true;
    }

    @Override
    public int getId() {
        return Item.POTION;
    }

    @Override
    public void setEquipped(boolean b) {

    }

    @Override
    public int getDmgModifier() {
        return 0;
    }
}
