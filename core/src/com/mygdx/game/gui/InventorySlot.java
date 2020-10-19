package com.mygdx.game.gui;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Widget;
import com.mygdx.game.item.Item;

public class InventorySlot extends Widget {

    public static final float SIZE = 36;
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
        batch.setColor(1,1,1,parentAlpha);
        patch.draw(batch,this.getX(),this.getY(),this.getWidth(),this.getHeight());
        if(!isEmpty){
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
