package com.mygdx.game.gui;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.NinePatch;
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
    private Texture img;
    private boolean isEmpty;


    public InventorySlot(Skin skin, Item item) {
        this.skin = skin;
        this.item = item;
        if(item == null){
            isEmpty = true;
        } else {
            isEmpty = false;
        }
        patch = skin.getPatch("default-round");
        img = new Texture("badlogic.jpg");
        this.setTouchable(Touchable.enabled);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.setColor(1,1,1,parentAlpha);
        patch.draw(batch,this.getX(),this.getY(),this.getWidth(),this.getHeight());
        //TODO change img in next line to be the item icon
        if(!isEmpty){
            batch.draw(/* place holder item.getTexture()*/ img, this.getX() + OFFSET, this.getY() + OFFSET, this.getWidth() - OFFSET * 2, this.getHeight() - OFFSET * 2);
            batch.setColor(1,1,1,1);
        }
        super.draw(batch, parentAlpha);
    }

    public boolean isEmpty() {
        return isEmpty;
    }

    public void setEmpty(boolean empty) {
        isEmpty = empty;
    }

    public void setItem(Item item){
        this.item = item;
        isEmpty = false;
    }

    public Item getItem() {
        return item;
    }

}
