package com.mygdx.game.map;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.mygdx.game.Rogue99;
import com.mygdx.game.interactable.Enemy;
import com.mygdx.game.interactable.Hero;
import com.mygdx.game.item.*;

public class LevelStage extends Stage {

    private Level level;
    private Rogue99 game;
    private Batch batch;

    public LevelStage(Level level, Rogue99 game) {
        this.level = level;
        this.game = game;
        createActorsForLevel();
    }

    public void setStageLevel(Level level) {
        this.level = level;
    }

    public Level getStageLevel() {
        return level;
    }

    private void createActorsForLevel() {
        for (int x = 0; x < level.getMap().length; x++) {
            for (int y = 0; y < level.getMap()[0].length; y++) {
                Tile tile = level.getMap()[x][y];
                LevelActor actor = new LevelActor(level, tile);
                actor.setBounds(tile.getPosX()*36, tile.getPosY()*36, 36, 36);
                addActor(actor);
                EventListener eventListener = new LevelClickListener(actor, level, game);
                actor.addListener(eventListener);
            }
        }
    }

    @Override
    public void draw() {
        this.batch = getBatch();
        batch.begin();
        drawHeroes();
        drawMap(level);
        batch.end();
        super.draw();
    }

    @Override
    public void dispose() {
        super.dispose();
        batch.dispose();
    }

    public void drawMap(Level level) {
        Tile[][] map = level.getMap();
        for(Tile[] i : map){
            for(Tile k : i){
                //check type of tile and draw sprite
                if(k.getType().equals("floor")){
                    drawTile(k,"floor", k.getPosX()*36, k.getPosY()*36);
                } else if(k.getType().equals("wall")){
                    drawTile(k,"wall", k.getPosX()*36, k.getPosY()*36);
                } else if(k.getType().equals("grass")){
                    drawTile(k,"grass1", k.getPosX()*36, k.getPosY()*36);
                } else if(k.getType().equals("upstair")){
                    drawTile(k,"upstair", k.getPosX()*36, k.getPosY()*36);
                } else if(k.getType().equals("downstair")) {
                    if(level.doorOpen == true){
                        drawTile(k,"downstair", k.getPosX() * 36, k.getPosY() * 36);
                    } else{
                        drawTile(k,"downstair_closed", k.getPosX() * 36, k.getPosY() * 36);
                    }
                }
            }
        }
    }

    //draws tile on specified spot in screen
    public void drawTile(Tile tile, String name, float x, float y) {
        Sprite sprite;
        if(!tile.getEntities().isEmpty() && tile.getEntities().peek() instanceof Hero){
            sprite = game.sprites.get(tile.getEntities().peek().getSprite());
            sprite.setPosition(x,y);
            sprite.draw(batch);
        } else if(!tile.getEntities().isEmpty() && tile.getEntities().peek() instanceof Enemy){
            //System.out.println("Drawing" + sprites.get(tile.getEntities().peek().getSprite()));
            sprite = game.sprites.get(tile.getEntities().peek().getSprite());
            if(tile.getEntities().peek().getSprite().equals("ghost")){
                //System.out.println("ghost sprite set alpha");
                sprite.setAlpha(0.2f);
            }
            //System.out.println("ENEMY SPRITE" + tile.getEntities().peek().getSprite());
            sprite.setPosition(x,y);
            sprite.draw(batch);
        } else if(!tile.getEntities().isEmpty() && tile.getEntities().peek() instanceof HealthScroll){
            sprite = game.sprites.get(tile.getEntities().peek().getSprite());
            //System.out.println("HEALTH SCROLL SPRITE" + tile.getEntities().peek().getSprite());
            sprite.setColor(Color.CYAN);
            sprite.setPosition(x,y);
            sprite.draw(batch);
        } else if(!tile.getEntities().isEmpty() && tile.getEntities().peek() instanceof ArmorScroll) {
            sprite = game.sprites.get(tile.getEntities().peek().getSprite());
            //System.out.println("ARMOR SCROLL SPRITE" + tile.getEntities().peek().getSprite());
            sprite.setColor(Color.GOLDENROD);
            sprite.setPosition(x, y);
            sprite.draw(batch);
        }
        else if(!tile.getEntities().isEmpty() && tile.getEntities().peek() instanceof StrengthScroll) {
            sprite = game.sprites.get(tile.getEntities().peek().getSprite());
            //System.out.println(tile.getEntities().peek().getSprite());
            sprite.setColor(Color.SLATE);
            sprite.setPosition(x, y);
            sprite.draw(batch);
        }
        else if(!tile.getEntities().isEmpty() && tile.getEntities().peek() instanceof HealthPotion) {
            sprite = game.sprites.get(tile.getEntities().peek().getSprite());
            //System.out.println("POTION SPRITE" + tile.getEntities().peek().getSprite());
            sprite.setColor(Color.CYAN);
            sprite.setPosition(x, y);
            sprite.draw(batch);
        }
        else if(!tile.getEntities().isEmpty() && tile.getEntities().peek() instanceof SummonScroll){
            sprite = game.sprites.get(tile.getEntities().peek().getSprite());
            sprite.setColor(Color.RED);
            sprite.setPosition(x, y);
            sprite.draw(batch);
        }
        else if(!tile.getEntities().isEmpty() && tile.getEntities().peek() instanceof DamagePotion) {
            sprite = game.sprites.get(tile.getEntities().peek().getSprite());
            //System.out.println("POTION SPRITE" + tile.getEntities().peek().getSprite());
            sprite.setColor(Color.RED);
            sprite.setPosition(x, y);
            sprite.draw(batch);
        } else if(!tile.getEntities().isEmpty() && tile.getEntities().peek() instanceof FreezePotion) {
            sprite = game.sprites.get(tile.getEntities().peek().getSprite());
            //System.out.println("POTION SPRITE" + tile.getEntities().peek().getSprite());
            sprite.setColor(Color.PURPLE);
            sprite.setPosition(x, y);
            sprite.draw(batch);
        }
        else if(!tile.getEntities().isEmpty() && tile.getEntities().peek() instanceof Weapon) {
            sprite = game.sprites.get(tile.getEntities().peek().getSprite());
            //System.out.println("POTION SPRITE" + tile.getEntities().peek().getSprite());
            sprite.setPosition(x, y);
            sprite.draw(batch);
        } else {
            sprite = game.sprites.get(name);
            sprite.setPosition(x, y);
            sprite.draw(batch);
        }
    }

    private void drawHeroes(){
        //System.out.println("Drawing hero");
        for(Hero player : game.players) {
            if(player.depth == game.hero.depth){
                Sprite sprite = game.sprites.get(player.getSprite());
                sprite.setPosition(player.getPosX()*36, player.getPosY()*36);
                Color color = new Color(player.getSpriteColor());
                color.a = 1;
                sprite.setColor(color);
                if(player.getSprite().equals("gravestone")){
                    System.out.println("gravestone");
                    sprite.setAlpha(1);
                } else {
                    sprite.setAlpha(.5f);
                }
                sprite.draw(batch);
            }
        }
    }
}