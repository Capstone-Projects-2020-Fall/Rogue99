package com.mygdx.game.gui;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.mygdx.game.Rogue99;
import com.mygdx.game.interactable.Hero;
import com.mygdx.game.item.Item;

import java.util.ArrayList;

public class InventoryGui extends Window {

    private static final int INVENTORY_ROWS = 2;
    private static final int INVENTORY_COLUMNS = 5;
    private static final int INVENTORY_SIZE = 10;
    private static final int INVENTORY_WINDOW_WIDTH_SIZE = 26;
    private static final int INVENTORY_WINDOW_WIDTH_OFFSET = 30;
    private static final int INVENTORY_WINDOW_HEIGHT_OFFSET = 60;

    ArrayList<Item> inventory;
    ArrayList<InventorySlot> inventorySlots;
    Rogue99 game;


    public InventoryGui(Skin skin, Hero hero, final Rogue99 game) {
        super("Inventory", skin);
        this.setResizable(false);
        this.setMovable(false);
        this.setSize(INVENTORY_WINDOW_WIDTH_SIZE*3+INVENTORY_WINDOW_WIDTH_OFFSET, InventorySlot.SIZE*INVENTORY_COLUMNS + INVENTORY_WINDOW_HEIGHT_OFFSET);
        //this.scaleBy(1);
        this.inventory = hero.getInventory();
        this.game = game;
        inventorySlots = new ArrayList<>();
        int row = 0;
        for(int i = 0; i < INVENTORY_SIZE; i++){
            final InventorySlot slot = new InventorySlot(skin,null, null);
            slot.addListener(new ClickListener(){
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    System.out.println("Item Clicked!");
                    if(!slot.isEmpty()){
                        System.out.println("Item picked up");
                        //TODO figure out how to pass the item back
                        game.usedItem(slot.getItem());
                        inventory.remove(slot.getItem());
                        slot.setEmpty(true);
                    }
                    super.clicked(event, x, y);
                }
            });
            inventorySlots.add(slot);
            this.add(slot).width(InventorySlot.SIZE).height(InventorySlot.SIZE).pad(4);
            row++;
            if(row >= INVENTORY_ROWS){
                this.row();
                row = 0;
            }
        }
        if(inventory != null){
            for(Item item : inventory){
                for(int i = 0; i < inventorySlots.size(); i++){
                    if (inventorySlots.get(i).isEmpty()){
                        inventorySlots.get(i).setItem(item, game.sprites.get(item.getSprite()));
                        return;
                    }
                }
            }
        }
    }

    public ArrayList<InventorySlot> getInventorySlots() {
        return inventorySlots;
    }

    public void addItemToInventory(Item item) {
            for (int i = 0; i < inventorySlots.size(); i++) {
                if (inventorySlots.get(i).isEmpty()) {
                    inventorySlots.get(i).setItem(item, game.sprites.get(item.getSprite()));
                    System.out.println("added item");
                    return;
                }
            }
            // if reached here inventory is full
    }
}

