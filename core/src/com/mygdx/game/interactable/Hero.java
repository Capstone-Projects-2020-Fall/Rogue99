package com.mygdx.game.interactable;

import com.mygdx.game.item.Item;
import com.mygdx.game.item.Potion;

import java.util.ArrayList;

public class Hero extends Character{

    //TODO Weapon object
    // private Weapon weapon;
    private ArrayList<Item> inventory;

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
}
