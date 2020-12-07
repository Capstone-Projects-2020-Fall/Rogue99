package com.mygdx.game.gui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Widget;
import com.mygdx.game.item.Item;
import com.mygdx.game.item.Weapon;

public class InventorySlot extends Widget {

    public static final int SIZE = 36;
    public static final float OFFSET = 2;

    private Skin skin;
    private NinePatch patch;
    private Item item;
    private Sprite itemIcon;
    private boolean isEmpty;


    public InventorySlot(Skin skin, Item item, Sprite itemIcon) {
        this.skin = skin;
        this.item = item;
        if(item == null){
            isEmpty = true;
        } else {
            isEmpty = false;
        }
        patch = skin.getPatch("default-round");
        this.itemIcon = itemIcon;
        this.setTouchable(Touchable.enabled);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.setColor(Color.GRAY);
        patch.draw(batch,this.getX(),this.getY(),this.getWidth(),this.getHeight());
        if(!isEmpty){
            if(item.getId() == Item.HEALTHSCROLL){
                batch.setColor(Color.CYAN);
            } else if(item.getId() == Item.ARMORSCROLL){
                batch.setColor(Color.GOLDENROD);
            } else if(item.getId() == Item.HEALTHPOTION){
                batch.setColor(Color.CYAN);
            } else if(item.getId() == Item.DAMAGEPOTION){
                batch.setColor(Color.RED);
            } else if(item.getId() == Item.SUMMONSCROLL){
                batch.setColor(Color.RED);
            } else if(item.getId() == Item.STRENGTHSCROLL){
                batch.setColor(Color.SLATE);
            } else if(item.getId() == Item.WEAPON){
                Weapon weapon = (Weapon)item;
                if(weapon.equipped == true){
                    batch.setColor(Color.RED);
                }
            }
            batch.draw(itemIcon, this.getX() + OFFSET, this.getY() + OFFSET, this.getWidth() - OFFSET * 2, this.getHeight() - OFFSET * 2);
            batch.setColor(0,0,0,0);
        }
        super.draw(batch, parentAlpha);
    }

    public boolean isEmpty() {
        return isEmpty;
    }

    public void setEmpty(boolean empty) {
        isEmpty = empty;
    }

    public void setItem(Item item, Sprite itemIcon){
        this.item = item;
        this.itemIcon = itemIcon;
        isEmpty = false;
    }

    public Item getItem() {
        return item;
    }

}
