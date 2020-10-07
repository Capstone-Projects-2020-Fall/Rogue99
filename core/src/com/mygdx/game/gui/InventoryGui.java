package com.mygdx.game.gui;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class InventoryGui extends Window {

    private static final int INVENTORY_COLUMNS = 4;
    private static final int INVENTORY_ROWS = 5;
    private static final int SQUARE_WIDTH = 30;
    private static final int SQUARE_HEIGHT = 30;
    Texture gray_square;
    Image gray_square_image;

    public InventoryGui(Skin skin) {
        super("Inventory", skin);
        gray_square = new Texture("gray_square.png");
        gray_square_image = new Image(gray_square);
        gray_square_image.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                setVisible(false);
                System.out.println("button clicked!");
            }
        });
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        int x = 0;
        int y = 0;
        for(int i = 0; i < INVENTORY_COLUMNS; i++){
            for(int j = 0; j < INVENTORY_ROWS; j++){
                batch.draw(gray_square, x, y, SQUARE_WIDTH, SQUARE_HEIGHT);
                x = x + 30;
            }
            x = 0;
            y = y + 30;
        }
    }
}
