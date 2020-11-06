package com.mygdx.game.interactable;

import com.badlogic.gdx.math.Vector3;
import com.mygdx.game.Packets;
import com.mygdx.game.Rogue99;
import com.mygdx.game.item.Item;
import com.mygdx.game.item.HealthPotion;
import com.mygdx.game.item.SummonScroll;

import java.util.ArrayList;

public class Hero extends Character{


    private ArrayList<Item> inventory;
    Rogue99 game;
    String sprite;
    public int depth;
    public Vector3 pos3 = new Vector3();
    private String name;

    public Hero(Rogue99 game, String sprite) {
        depth = 0;
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


    public ArrayList<Item> getInventory() {
        return inventory;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

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
            case NUMPAD_7:
                move(x - 1, y + 1);
                break;
            case NUMPAD_9:
                move(x + 1, y + 1);
                break;
            case NUMPAD_1:
                move(x - 1, y - 1);
                break;
            case NUMPAD_3:
                move(x + 1, y - 1);
                break;
        }
    }

    private void move(int x, int y){
        System.out.println("Hero Position: [" + x + "][" + y + "]");
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
            } else if (game.level.getMap()[x][y].getType().equals("stair_down")){
                depth++;
                game.newLevel(depth);
            }  else {
                // move to new position
                game.level.getMap()[getPosX()][getPosY()].getEntities().pop();
                setPosX(x);
                setPosY(y);
                game.level.getMap()[x][y].getEntities().push(this);
                game.setAttacking(false);
            }
            if(game.multiplayer){
                Packets.Packet003Movement movement = new Packets.Packet003Movement();
                movement.xPos = x;
                movement.yPos = y;
                movement.name = getName();
                movement.depth = depth;
                game.client.client.sendTCP(movement);
            }
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
            enemy.attack(this);
        }
        else {
            game.level.getMap()[x][y].getEntities().push( new SummonScroll(1, "scroll", enemy.getDifficulty()) );
            game.removeActor(game.enemyHud);
            game.removeActor(game.hudGui);
        }
    }


}
