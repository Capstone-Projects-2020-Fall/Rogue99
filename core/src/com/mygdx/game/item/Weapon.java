package com.mygdx.game.item;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.interactable.Character;
import com.badlogic.gdx.graphics.Texture;

public class Weapon extends Item {

    public static final int SPEED = 200;
    private static Texture tecture;
    float x, y;

    public Weapon(float x, float y) {
        this.x = x;
        this.y = y;

        tecture = new Texture("spritesheets/sprites.png");
    }

    @Override
    public void use(Character character) {
        character.getCurrHP();
    }

    public void update(float deltaTime){
        y += SPEED * deltaTime;
    }

    public void render(SpriteBatch batch){
        batch.draw(tecture, x, y);
    }
    
    /* weapons sprites
    * s143
    * s252 to s260
    * */
}

