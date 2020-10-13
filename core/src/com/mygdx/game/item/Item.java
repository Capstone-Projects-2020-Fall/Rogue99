package com.mygdx.game.item;

import com.mygdx.game.interactable.Character;

public abstract class Item{
    protected int rarity;
    protected String sprite;
    public abstract void use(Character character);
}
