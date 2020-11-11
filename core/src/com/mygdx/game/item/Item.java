package com.mygdx.game.item;

import com.mygdx.game.interactable.Character;
import com.mygdx.game.interactable.Interactable;

public abstract class Item extends Interactable {
    public static final int HEALTHPOTION = 1;
    public static final int DAMAGEPOTION = 2;
    public static final int ARMORSCROLL = 3;
    public static final int HEALTHSCROLL = 4;
    public static final int STRENGTHSCROLL = 5;
    public static final int WEAPON = 6;
    public static final int SUMMONSCROLL = 7;
    public static final int BANESCROLL = 8;
    public static final int FREEZEPOTION = 9;

    protected int rarity;
    protected String sprite;
    protected int id;
    public abstract boolean use(Character character);
    public abstract int getId();
    public abstract void setEquipped(boolean b);
    public abstract int getDmgModifier();
}
