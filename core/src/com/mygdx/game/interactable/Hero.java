package com.mygdx.game.interactable;

import com.mygdx.game.Rogue99;
import com.mygdx.game.item.Item;
import com.mygdx.game.item.Potion;

import java.util.ArrayList;

public class Hero extends Character{

    //TODO Weapon object
    // private Weapon weapon;
    private ArrayList<Item> inventory;
    Rogue99 game;
    String sprite;

    public Hero(Rogue99 game, String sprite) {
        this.game = game;
        this.sprite = sprite;
    }

    public void setInventory(ArrayList<Item> inventory) {
        this.inventory = inventory;
    }

    public ArrayList<Item> getInventory() {
        return inventory;
    }

    public void pickup(Item item){

    }

    public void drop(Item item){

    }

    public void useItem(Item item){

    }

    public void usePotion(Potion potion){

    }

    //TODO implement Weapon Object
    public void useWeapon(/*Weapon weapon*/){

    }

    public void useScroll(){}

    @Override
    public void setSprite(String sprite) {
        this.sprite = sprite;
    }

    @Override
    public String getSprite() {
        return sprite;
    }

    public void update(int direction){
        int x = getPosX();
        int y = getPosY();
        switch (direction){
            case DOWN:
                move(x, y + 1);
                break;
            case UP:
                move(x, y -1);
                break;
            case LEFT:
                move(x + 1, y);
                break;
            case RIGHT:
                move(x - 1, y);
                break;
        }
    }

    private void move(int x, int y){
        System.out.println(x + " " + y);
        if(!game.level.getMap()[x][y].getType().equals("wall")){
            if(!game.level.getMap()[x][y].getEntities().isEmpty() && game.level.getMap()[x][y].getEntities().peek() instanceof Enemy){
                // attack

            } else if(!game.level.getMap()[x][y].getEntities().isEmpty() && game.level.getMap()[x][y].getEntities().peek() instanceof Item){
                // pick it up
                if(inventory.size() != 10){
                    inventory.add((Item) game.level.getMap()[x][y].getEntities().pop());
                }
            } else {
                // move to new position
                setPosX(x);
                setPosY(y);
            }
        }
    }


}
