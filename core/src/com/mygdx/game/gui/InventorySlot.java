package com.mygdx.game.gui;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Widget;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.mygdx.game.item.Item;

public class InventorySlot extends Widget {

    public static final float SIZE = 48;
    public static final float OFFSET = 4;

    private Skin skin;
    private NinePatch patch;
    private Item item;
    private Texture img;
    private Texture itemIcon;


    public InventorySlot(Skin skin /*Item item*/) {
        this.skin = skin;
        //this.item = item;
        patch = skin.getPatch("default-round");
        //TODO change img to be item icon
        //itemIcon = item.getTexture();
        img = new Texture("badlogic.jpg");
        this.setTouchable(Touchable.enabled);
        this.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("Item Clicked!");
                super.clicked(event, x, y);
            }
        });
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.setColor(1,1,1,parentAlpha);
        patch.draw(batch,this.getX(),this.getY(),this.getWidth(),this.getHeight());
        //TODO change img in next line to be the item icon
        if(itemIcon != null){
            batch.draw(img, this.getX() + OFFSET, this.getY() + OFFSET, this.getWidth() - OFFSET * 2, this.getHeight() - OFFSET * 2);
            batch.setColor(1,1,1,1);
        }
        super.draw(batch, parentAlpha);
    }


}
