package com.mygdx.game.gui;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.mygdx.game.item.Item;

public class InventoryGui extends Window {

    private static final int INVENTORY_ROWS = 3;
    private static final int INVENTORY_COLUMNS = 8;
    private static final int INVENTORY_SIZE = 24;
    private static final int INVENTORY_WINDOW_WIDTH_OFFSET = 30;
    private static final int INVENTORY_WINDOW_HEIGHT_OFFSET = 100;


    public InventoryGui(Skin skin) {
        super("Inventory", skin);
        this.setResizable(false);
        this.setMovable(false);
        this.setSize(InventorySlot.SIZE*INVENTORY_ROWS + INVENTORY_WINDOW_WIDTH_OFFSET,InventorySlot.SIZE*INVENTORY_COLUMNS + INVENTORY_WINDOW_HEIGHT_OFFSET);
        this.scaleBy(1);
        int row = 0;
        for(int i = 0; i < INVENTORY_SIZE; i ++){
            InventorySlot inventorySlot = new InventorySlot(skin, new Item());
            this.add(inventorySlot).width(InventorySlot.SIZE).height(InventorySlot.SIZE).pad(4);
            row++;
            if(row >= INVENTORY_ROWS){
                this.row();
                row = 0;
            }
        }

    }

}
