package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.mygdx.game.gui.InventoryGui;
import com.mygdx.game.map.*;
import com.badlogic.gdx.scenes.scene2d.Stage;

import java.util.HashMap;

public class Rogue99 extends ApplicationAdapter {
	SpriteBatch batch;
	OrthographicCamera camera;
	ExtendViewport viewport;

	Texture img;

	//Skin for inventory gui
	Skin skin;

	//InventoryGui Actor
	InventoryGui inventoryGui;

	//texture atlas for sprite sheet
	TextureAtlas textureAtlas;
	final HashMap<String, Sprite> sprites = new HashMap<>();

	Level level;
	Stage stage;

	@Override
	public void create () {
		batch = new SpriteBatch();
		img = new Texture("badlogic.jpg");
		skin = new Skin(Gdx.files.internal("uiskin.json"));
		inventoryGui = new InventoryGui(skin);
		camera = new OrthographicCamera();
		viewport = new ExtendViewport(2160, 2160, camera);

		//load sprites and add to hash map
		textureAtlas = new TextureAtlas("spritesheets/sprites.txt");
		addSprites();

		//initialize first level
		level = new Level(1);
		level.generate();
		stage = new LevelStage(level);
		Gdx.input.setInputProcessor(stage);
		stage.getViewport().setCamera(camera);
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();

		drawMap(level);
		stage.act();

		batch.end();
	}


	@Override
	public void dispose () {
		batch.dispose();
		textureAtlas.dispose();
		sprites.clear();
	}

	@Override
	public void resize(int width, int height) {
		viewport.update(width, height, true);
		batch.setProjectionMatrix(camera.combined);
	}

	//adds sprites to hash map for more efficient use
	public void addSprites(){
		Array<TextureAtlas.AtlasRegion> regions = textureAtlas.getRegions();

		for(TextureAtlas.AtlasRegion region : regions){
			Sprite sprite = textureAtlas.createSprite(region.name);
			sprites.put(region.name, sprite);
		}
	}

	//draws map for given level
	public void drawMap(Level level) {
		Tile[][] map = level.getMap();
		for(Tile[] i : map){
			for(Tile k : i){
				//check type of tile and draw sprite
				if(k.getType().equals("floor")){
					drawTile("floor", k.getPosX()*36, k.getPosY()*36);
				} else if(k.getType().equals("wall")){
					drawTile("crackedwall", k.getPosX()*36, k.getPosY()*36);
				} else if(k.getType().equals("grass")){
					if(Math.random() < 0.5){
						drawTile("shortgrass1", k.getPosX()*36, k.getPosY()*36);
					} else{
						drawTile("longgrass", k.getPosX()*36, k.getPosY()*36);
					}
				}
			}
		}
	}

	//draws tile on specified spot in screen
	public void drawTile(String name, float x, float y) {
		Sprite sprite = sprites.get(name);
		sprite.setPosition(x, y);
		sprite.draw(batch);
	}
}
