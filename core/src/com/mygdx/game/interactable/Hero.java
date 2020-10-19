package com.mygdx.game.interactable;

import com.badlogic.gdx.math.Vector3;
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
    public Vector3 pos3 = new Vector3();

    public Hero(Rogue99 game, String sprite) {
        this.game = game;
        this.sprite = sprite;
        inventory = new ArrayList<>();
        this.setMaxHP(100);
        this.setCurrHP(100);
        this.setStr(15);
    }

    @Override
    public void setPosX(int posX) {
        super.setPosX(posX);
        pos3.x = getPosX()*36;
    }

    @Override
    public void setPosY(int posY) {
        super.setPosY(posY);
        pos3.y = getPosY()*36;
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
                move(x, y - 1);
                break;
            case UP:
                move(x, y + 1);
                break;
            case LEFT:
                move(x - 1, y);
                break;
            case RIGHT:
                move(x + 1, y);
                break;
        }
    }

    private void move(int x, int y){
        //System.out.println(x + " " + y);
        if(!game.level.getMap()[x][y].getType().equals("wall")){
            if(!game.level.getMap()[x][y].getEntities().isEmpty() && game.level.getMap()[x][y].getEntities().peek() instanceof Enemy){
                // attack
                game.setAttacking(true);
                attack(x,y);
            } else if(!game.level.getMap()[x][y].getEntities().isEmpty() && game.level.getMap()[x][y].getEntities().peek() instanceof Item){
                // pick it up
                if(inventory.size() != 10){
                    inventory.add((Item) game.level.getMap()[x][y].getEntities().pop());
                    game.inventoryGui.addItemToInventory(inventory.get(inventory.size()-1));
                }
                System.out.println(inventory.get(0).getSprite());
                game.level.getMap()[getPosX()][getPosY()].getEntities().pop();
                setPosX(x);
                setPosY(y);
                game.level.getMap()[x][y].getEntities().push(this);
            } else {
                // move to new position
                game.level.getMap()[getPosX()][getPosY()].getEntities().pop();
                setPosX(x);
                setPosY(y);
                game.level.getMap()[x][y].getEntities().push(this);
                game.setAttacking(false);
            }
            game.level.moveEnemies();
        }
    }

    public void attack(int x, int y){
        Enemy enemy = (Enemy) game.level.getMap()[x][y].getEntities().pop();
        System.out.println("enemy health: " + enemy.getCurrHP() + " enemy armor: " + enemy.getArmor());
        if (getStr() > enemy.getArmor()) {
            enemy.setCurrHP(enemy.getCurrHP() + enemy.getArmor() - getStr());
        }
        System.out.println("enemy health after attack: " + enemy.getCurrHP());
        game.changeBarValue("EnemyHP", enemy.getCurrHP());
        game.changeBarValue("EnemyAR", enemy.getArmor());
        game.enemyHud.statsNumTexts.get(0).setText(String.valueOf(enemy.getCurrHP()));
        game.enemyHud.statsNumTexts.get(1).setText(String.valueOf(enemy.getArmor()));
        if(enemy.getCurrHP() > 0){
            game.level.getMap()[x][y].getEntities().push(enemy);
        }
    }


}
