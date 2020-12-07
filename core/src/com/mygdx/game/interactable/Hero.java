package com.mygdx.game.interactable;

import com.badlogic.gdx.math.Vector3;
import com.mygdx.game.Packets;
import com.mygdx.game.Rogue99;
import com.mygdx.game.item.FreezePotion;
import com.mygdx.game.item.Item;
import com.mygdx.game.item.SummonScroll;

import java.util.ArrayList;

public class Hero extends Character{


    private ArrayList<Item> inventory;
    Rogue99 game;
    String sprite;
    int spriteColor;
    public int depth;
    public Vector3 pos3 = new Vector3();
    private String name;
    private int frozen;
    public int score;

    public Hero(Rogue99 game, String sprite) {
        depth = 0;
        score = 0;
        this.game = game;
        this.sprite = sprite;
        inventory = new ArrayList<>();
        this.setMaxHP(100);
        this.setCurrHP(100);
        this.setStr(10);
        super.setHitChance(0.7);
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

    public void setSpriteColor(int spriteColor) {
        this.spriteColor = spriteColor;
    }

    public int getSpriteColor() {
        return spriteColor;
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
        if(frozen > 0) { return; }
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
            case UP_LEFT:
                move(x - 1, y + 1);
                break;
            case UP_RIGHT:
                move(x + 1, y + 1);
                break;
            case DOWN_LEFT:
                move(x - 1, y - 1);
                break;
            case DOWN_RIGHT:
                move(x + 1, y - 1);
                break;
        }
    }

    private void move(int x, int y){
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
                game.level.getMap()[getPosX()][getPosY()].getEntities().pop();
                setPosX(x);
                setPosY(y);
                game.level.getMap()[x][y].getEntities().push(this);
            } else if (game.level.getMap()[x][y].getType().equals("downstair")){
                if(!game.level.doorOpen){
                    game.level.getMap()[getPosX()][getPosY()].getEntities().pop();
                    setPosX(x);
                    setPosY(y);
                    game.level.getMap()[x][y].getEntities().push(this);
                    game.setAttacking(false);
                } else{
                    game.level.getMap()[getPosX()][getPosY()].getEntities().pop();
                    if(game.levels.size() > depth+1) {
                        game.nextLevel(depth);
                        setPosX(game.level.entrance.getPosX());
                        setPosY(game.level.entrance.getPosY());
                        game.level.getMap()[getPosX()][getPosY()].getEntities().push(this);
                    }
                    else
                        game.newLevel(depth);
                    depth++;
                }
            } else if (game.level.getMap()[x][y].getType().equals("upstair")){
                if(depth == 0) {
                    game.level.getMap()[getPosX()][getPosY()].getEntities().pop();
                    setPosX(x);
                    setPosY(y);
                    game.level.getMap()[x][y].getEntities().push(this);
                    game.setAttacking(false);
                }else{
                    game.level.getMap()[getPosX()][getPosY()].getEntities().pop();
                    game.prevLevel();
                    depth--;
                    setPosX(game.level.exit.getPosX());
                    setPosY(game.level.exit.getPosY());
                    game.level.getMap()[getPosX()][getPosY()].getEntities().push(this);
                }
            } else {
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
        Enemy enemy = (Enemy) game.level.getMap()[x][y].getEntities().peek();
        if(Math.random() < getHitChance()){
            enemy.setCurrHP(enemy.getCurrHP() - this.getStr());
        } else{

        }
        game.changeBarValue("EnemyHP", enemy.getCurrHP());
        game.enemyHud.statsNumTexts.get(0).setText(String.valueOf(enemy.getCurrHP()));
        game.enemyHud.getTitleLabel().setText(enemy.getSprite().toUpperCase());
        if(enemy.getCurrHP() > 0){
            enemy.attack(this);
            //hit() performs an action that the enemy performs when hit, such as slimes splitting or wasps moving away
            if(enemy.getSprite().equals("wasp")){
                Wasp wasp = (Wasp) enemy;
                wasp.hit();
            } else if(enemy.getSprite().equals("slime")){
                Slime slime = (Slime) enemy;
                slime.hit();
            }
        }else {
            if(enemy.tile.getEntities().size() > 0) {
                enemy.tile.getEntities().pop();
                game.level.enemies.remove(enemy);
                game.enemyHud.getTitleLabel().setText("EnemyStats");
                score += (int) ((Math.random() * 100) + 100);
                game.getScoreboard().getPlayerScore().setText("Score: " + score + " Health: " + game.hero.getCurrHP()
                        + " Armor: " + game.hero.getArmor() + " Level: " + game.hero.depth);
                if (game.isMultiplayer()) {
                    Packets.Packet005Stats stats = new Packets.Packet005Stats();
                    stats.name = getName();
                    stats.score = score;
                    stats.health = getCurrHP();
                    stats.armor = getArmor();
                    stats.depth = depth;
                    game.client.client.sendTCP(stats);
                }
                if (Math.random() < 0.4 && game.multiplayer) {
                    game.level.getMap()[x][y].getEntities().push(new SummonScroll(1, "scroll_summon", enemy.getSprite()));
                } else if (Math.random() > 0.4 && game.multiplayer) {
                    game.level.getMap()[x][y].getEntities().push(new FreezePotion(1, "potion_freeze", 5));
                }
                if (game.level.enemies.size() <= (game.level.enemiesToOpen)) {
                    game.level.doorOpen = true;
                }
            }
        }
    }
    public void takeDamage(int dmg) {

        if (getCurrHP() - dmg > 0) {
            setCurrHP( getCurrHP() - dmg );
            game.changeBarValue(game.HEALTHBAR, getCurrHP());
            if(game.isMultiplayer()){
                Packets.Packet005Stats stats = new Packets.Packet005Stats();
                stats.name = getName();
                stats.health = getCurrHP();
                stats.armor = getArmor();
                stats.score = score;
                stats.depth = depth;
                game.client.client.sendTCP(stats);
            }
        } else {
            setCurrHP(0);
        }
    }

    public int freezeTime(int time) {
        if(frozen + time >= 0) {
            frozen += time;
        }
        return frozen;
    }

    public boolean isFrozen(){
        if(frozen > 0){
            return true;
        } else{
            return false;
        }
    }

    public ArrayList<Interactable> getAdjacentEnemies(String type){
        ArrayList<Interactable> enemies = new ArrayList<>();
        for(int i = -1; i < 2; i++){
            for(int k = -1; k < 2; k++){
                if(!game.level.getMap()[this.getPosX() + i][this.getPosY() + k].getEntities().empty() &&
                        game.level.getMap()[this.getPosX() + i][this.getPosY() + k].getEntities().peek().getSprite().equals(type)){
                    enemies.add(game.level.getMap()[this.getPosX() + i][this.getPosY() + k].getEntities().peek());
                }
            }
        }
        return enemies;
    }
}
