package com.mygdx.game.item;

import com.mygdx.game.interactable.Character;
import com.mygdx.game.interactable.Interactable;

public abstract class Item extends Interactable {
    public static final int POTION = 1;
    public static final int ARMORSCROLL = 2;
    public static final int HEALTHSCROLL = 3;
    public static final int STRENGTHSCROLL = 4;
    protected int rarity;
    protected String sprite;
    protected int id;
    public abstract boolean use(Character character);
    public abstract int getId();
}
