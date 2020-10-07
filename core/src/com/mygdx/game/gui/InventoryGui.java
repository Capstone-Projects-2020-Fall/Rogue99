package com.mygdx.game.gui;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.mygdx.game.item.Item;

public class InventoryGui extends Window {

    private static final int INVENTORY_ROWS = 6;
    private static final int INVENTORY_SIZE = 24;


    public InventoryGui(Skin skin) {
        super("Inventory", skin);
        this.setResizable(false);
        this.setMovable(true);
        this.setSize(350,250);
        int row = 0;
        for(int i = 0; i < INVENTORY_SIZE; i ++){
            InventorySlot inventorySlot = new InventorySlot(skin, new Item());
            this.add(inventorySlot).width(InventorySlot.SIZE).height(InventorySlot.SIZE).pad(2);
            row++;
            if(row >= INVENTORY_ROWS){
                this.row();
                row = 0;
            }
        }

    }

}
