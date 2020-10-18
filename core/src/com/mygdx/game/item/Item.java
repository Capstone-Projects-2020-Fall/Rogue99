package com.mygdx.game.item;

import com.mygdx.game.interactable.Character;
import com.mygdx.game.interactable.Interactable;

public abstract class Item extends Interactable {
    protected int rarity;
    protected String sprite;
    public abstract boolean use(Character character);
}
