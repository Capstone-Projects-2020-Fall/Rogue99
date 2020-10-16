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
import com.mygdx.game.gui.HUDGui;
import com.mygdx.game.gui.HUDProgressBar;
import com.mygdx.game.gui.InventoryGui;
import com.mygdx.game.map.*;
import com.badlogic.gdx.scenes.scene2d.Stage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Rogue99 extends ApplicationAdapter {

	public final int PAD = 340;
	public final int HEIGHT_PAD = 500;

	SpriteBatch batch;
	OrthographicCamera camera;
	ExtendViewport viewport;

	Texture img;

	//Skin for inventory gui
	Skin skin;

	//InventoryGui Actor
	InventoryGui inventoryGui;
	//HUD Actor
	HUDGui hudGui;
	//HUD bars list
	ArrayList<HUDProgressBar> barList;

	//texture atlas for sprite sheet
	TextureAtlas textureAtlas;
	final HashMap<String, Sprite> sprites = new HashMap<>();

	Level level;
	Stage stage;

	@Override
	public void create () {
		batch = new SpriteBatch();
		img = new Texture("badlogic.jpg");
		camera = new OrthographicCamera();
		viewport = new ExtendViewport(2500, 2160, camera);

		//load sprites and add to hash map
		textureAtlas = new TextureAtlas("spritesheets/sprites.txt");
		addSprites();

		//load skin for Inventory & HUD
		skin = new Skin(Gdx.files.internal("uiskin.json"));

		//initialize first level
		level = new Level(1);
		level.generate();
		stage = new LevelStage(level);
		Gdx.input.setInputProcessor(stage);
		stage.getViewport().setCamera(camera);
		stage.setViewport(viewport);

		//initialize Inventory & HUD gui disabled for now
//		createInventoryGui();
//		createHUDGui();
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();

		drawMap(level);
		stage.act();
		stage.draw();
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
				} else if(k.getType().equals("stair_up")){
					drawTile("stair_up", k.getPosX()*36, k.getPosY()*36);
				} else if(k.getType().equals("stair_down")) {
					drawTile("stair_down", k.getPosX() * 36, k.getPosY() * 36);
				} else if(k.getType().equals("enemy")){
					if(Math.random() < 0.5){
						drawTile("wasp", k.getPosX()*36, k.getPosY()*36);
					} else{
						drawTile("crab", k.getPosX()*36, k.getPosY()*36);
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

	//creates HUD GUI & the map of the stats bars.
	public void createHUDGui(){
		Map<String,Integer> bars = new HashMap<>();
		bars.put("Health", 100);
		bars.put("Armour", 0);
		hudGui = new HUDGui(skin, bars);
		hudGui.setPosition(Gdx.graphics.getWidth() - PAD, inventoryGui.getHeight() + HEIGHT_PAD);
		stage.addActor(hudGui);
		barList = hudGui.getHudBars();
	}

	//creates Inventory GUI
	public void createInventoryGui(){
		inventoryGui = new InventoryGui(skin);
		inventoryGui.setPosition(Gdx.graphics.getWidth() - PAD, 0);
		stage.addActor(inventoryGui);
	}

	//adjust stats bars
	public void changeBarValue(String barName, int newValue){
		for(HUDProgressBar bar : barList){
			if(bar.getName() == barName){
				bar.setValue(newValue);
			}
		}
	}
}
