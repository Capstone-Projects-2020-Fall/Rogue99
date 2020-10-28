package com.mygdx.game.interactable;

import com.mygdx.game.Rogue99;
import com.mygdx.game.map.Tile;

import java.util.ArrayList;
import java.util.Stack;

import static java.lang.StrictMath.abs;

public class Enemy extends Character {

    private int difficulty;
    private String sprite;
    private Tile tile;
    private Rogue99 game;

    public Enemy(int difficulty, String sprite, Tile tile, Rogue99 game) {
        this.difficulty = difficulty;
        for (int i = 0; i < difficulty; i++) {
            super.setMaxHP(getMaxHP() + 10);
            super.setArmor(getArmor() + 5);
            super.setStr(getStr() + 2);
        }
        this.setCurrHP(getMaxHP());
        this.sprite = sprite;
        this.tile = tile;
        this.game = game;
    }

    @Override
    public String getSprite() {
        return this.sprite;
    }

    public void moveEnemy(Tile[][] map, Hero hero){
        //find shortest path to hero
        //System.out.println("HERO X: " + hero.getPosX() + " HERO Y: " + hero.getPosY());
        Stack<Tile> path = getShortestPath(map[hero.getPosX()][hero.getPosY()], map);
        if(path == null){
            return;
        }
        path.pop();
        Tile newTile = path.pop();

        //remove enemy from current tile and push to new
        tile.getEntities().pop();
        tile = newTile;
        newTile.getEntities().push(this);
        //System.out.println("ENEMY MOVED");
    }

    public Stack<Tile> getShortestPath(Tile target, Tile[][] map){
        ArrayList<Tile> open = new ArrayList<>();
        ArrayList<Tile> closed = new ArrayList<>();

        //System.out.println("STARTING TILE: " + tile.getPosX() + " " + tile.getPosY());

        //add starting tile to open list
        this.tile.f = 0;
        open.add(this.tile);

        while(!open.isEmpty()){
            //get lowest F cost tile (q) in open list
            int ind = 0;
            int leastF = 0;
            for(int i = 0; i < open.size(); i++){
                if(open.get(i).f >= leastF){
                    ind = i;
                }
            }
            //switch it to the closed list
            Tile q = open.remove(ind);
            closed.add(q);

            //generate q's successors
            ArrayList<Tile> successors = new ArrayList<>();
            for(int m = -1; m < 2; m++){
                for(int n = -1; n < 2; n++){
                    if(m == 0 && n == 0) continue;
                    if((q.getPosX()+m < 0 || q.getPosX()+m > 59) || (q.getPosY()+n < 0 || q.getPosY()+n > 59)){
                        continue;
                    }
                    Tile neighbor = map[q.getPosX()+m][q.getPosY()+n];
                    if(neighbor.getType().equals("wall") || closed.contains(neighbor)){
                        continue;
                    } else if(!open.contains(neighbor)){
                        open.add(neighbor);
                        neighbor.parent = q;
                        //calculate f g and h
                        neighbor.g = neighbor.parent.g + neighborDistance(neighbor.parent, neighbor);
                        neighbor.h = abs(neighbor.getPosX() - target.getPosX()) + abs(neighbor.getPosY() - target.getPosY());
                        neighbor.f = neighbor.g + neighbor.h;
                    } else if(open.contains(neighbor)){
                        if(q.g + neighborDistance(q, neighbor) < neighbor.g){
                            neighbor.parent = q;
                            neighbor.g = q.g + neighborDistance(q, neighbor);
                            neighbor.f = neighbor.g + neighbor.h;
                        }
                    }
                }
            }
            if(closed.contains(target)){    //closed.contains(target)
                //System.out.println("Shortest path found");
                //save the path
                Stack<Tile> path = new Stack<>();
                Tile c = target;
                while(c.parent != null){
                    //System.out.println("path: " + c.parent.getPosX() + " " + c.parent.getPosY());
                    path.push(c);
                    c = c.parent;
                    if(path.size() >= 15){
                        return null;
                    }
                }
                //System.out.println("Shortest path found");
                return path;
            }
        }
        //System.out.println("No shortest path found");
        return null;
    }

    private int neighborDistance(Tile parent, Tile neighbor){
        //if diagonal, else orthogonal
        if(neighbor.getPosX() != parent.getPosX() && neighbor.getPosY() != parent.getPosY()){
            return 14;
        } else{
            return 10;
        }
    }

    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }

    public int getDifficulty() {
        return difficulty;
    }

    public void attack(Hero hero){
        if (getStr() > hero.getArmor()) {
            hero.setCurrHP(hero.getCurrHP() + hero.getArmor() - getStr());
        }
        System.out.println("enemy health after attack: " + hero.getCurrHP());
        game.changeBarValue(game.HEALTHBAR, hero.getCurrHP());
        game.changeBarValue(game.ARMOURBAR, hero.getArmor());
        game.hudGui.statsNumTexts.get(1).setText(String.valueOf(hero.getCurrHP()));
        game.hudGui.statsNumTexts.get(0).setText(String.valueOf(hero.getArmor()));
    }
}
