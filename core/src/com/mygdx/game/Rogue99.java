package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.mygdx.game.gui.HUDGui;
import com.mygdx.game.gui.HUDProgressBar;
import com.mygdx.game.gui.InventoryGui;
import com.mygdx.game.interactable.Enemy;
import com.mygdx.game.item.ArmorScroll;
import com.mygdx.game.item.HealthScroll;
import com.mygdx.game.item.Item;
import com.mygdx.game.interactable.Character;
import com.mygdx.game.interactable.Control;
import com.mygdx.game.interactable.Hero;
import com.mygdx.game.item.Potion;
import com.mygdx.game.map.*;
import com.badlogic.gdx.scenes.scene2d.Stage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Rogue99 extends ApplicationAdapter {

	public final int PAD = 340;
	public final int HEIGHT_PAD = 132;

	Hero hero;
	SpriteBatch batch;	public OrthographicCamera camera;

	ExtendViewport viewport;

	Texture img;

	//Skin for inventory gui
	Skin skin;

	//InventoryGui Actor
	public InventoryGui inventoryGui;
	//HUD Actor
	public HUDGui hudGui;
	//HUD bars list
	ArrayList<HUDProgressBar> barList;

	//texture atlas for sprite sheet
	TextureAtlas textureAtlas;
	public final HashMap<String, Sprite> sprites = new HashMap<>();

	public Level level;
	Stage stage;
	Control control;

	boolean mapDrawn;
	boolean showInventory;



	@Override
	public void create () {
		batch = new SpriteBatch();



		camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

		viewport = new ExtendViewport(2500, 2160, camera);

		camera.zoom = 0.4f;
		camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

		mapDrawn = false;
		showInventory = false;

		//load sprites and add to hash map
		textureAtlas = new TextureAtlas("spritesheets/sprites.txt");
		addSprites();


		//load skin for Inventory & HUD
		skin = new Skin(Gdx.files.internal("uiskin.json"));

		hero = new Hero(this, "tile169");
		//initialize first level
		level = new Level(this,1, hero);
		level.generate();
		stage = new LevelStage(level);
		stage.getViewport().setCamera(camera);
		stage.setViewport(viewport);

		//initialize Inventory & HUD gui disabled for now
		createInventoryGui();
		createHUDGui();

		control = new Control(hero, this);

		Gdx.input.setInputProcessor(control);
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		batch.setProjectionMatrix(camera.combined);

		batch.begin();


		drawMap(level);

		camera.position.lerp(hero.pos3, 0.1f);
		camera.update();

		stage.act();
		if(isShowInventory()){
			Gdx.input.setInputProcessor(stage);
			inventoryGui.setPosition(hero.getPosX()*36 + 72, hero.getPosY()*36 - 108);
			hudGui.setPosition(hero.getPosX()*36 + 72, hero.getPosY()*36 + HEIGHT_PAD);
			stage.draw();
			stage.addListener(new InputListener(){
				@Override
				public boolean keyUp(InputEvent event, int keycode) {
					if(keycode == Input.Keys.I){
						setShowInventory(false);
					}
					return super.keyDown(event, keycode);
				}
			});
		} else {
			Gdx.input.setInputProcessor(control);
		}

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
		hudGui.setPosition(Gdx.graphics.getWidth(), inventoryGui.getHeight() + HEIGHT_PAD);
		inventoryGui.setPosition(Gdx.graphics.getWidth(), 0);
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
					drawTile(k,"floor", k.getPosX()*36, k.getPosY()*36);
				} else if(k.getType().equals("wall")){
					drawTile(k,"crackedwall", k.getPosX()*36, k.getPosY()*36);
				} else if(k.getType().equals("grass")){
					if(Math.random() < 0.5){
						drawTile(k,"shortgrass1", k.getPosX()*36, k.getPosY()*36);
					} else{
						drawTile(k,"longgrass", k.getPosX()*36, k.getPosY()*36);
					}
				} else if(k.getType().equals("stair_up")){
					drawTile(k,"stair_up", k.getPosX()*36, k.getPosY()*36);
				} else if(k.getType().equals("stair_down")) {
					drawTile(k,"stair_down", k.getPosX() * 36, k.getPosY() * 36);
				} else if(k.getType().equals("enemy")){
					if(Math.random() < 0.5){
						drawTile(k,"wasp", k.getPosX()*36, k.getPosY()*36);
					} else{
						drawTile(k,"crab", k.getPosX()*36, k.getPosY()*36);
					}
				} else if(k.getType().equals("stair_up")){
					drawTile(k,"stair_up", k.getPosX()*36, k.getPosY()*36);
				} else if(k.getType().equals("stair_down")) {
					drawTile(k,"stair_down", k.getPosX() * 36, k.getPosY() * 36);
				} else if(k.getType().equals("enemy")){
					if(Math.random() < 0.5){
						drawTile(k,"wasp", k.getPosX()*36, k.getPosY()*36);
					} else{
						drawTile(k,"crab", k.getPosX()*36, k.getPosY()*36);
					}
				}
			}
		}
	}

	//draws tile on specified spot in screen
	public void drawTile(Tile tile, String name, float x, float y) {
		Sprite sprite;
		if(!tile.getEntities().isEmpty() && tile.getEntities().peek() instanceof  Hero){
			sprite = sprites.get(tile.getEntities().peek().getSprite());
			sprite.setPosition(x,y);
			sprite.draw(batch);
		} else if(!tile.getEntities().isEmpty() && tile.getEntities().peek() instanceof Enemy){
			sprite = sprites.get(tile.getEntities().peek().getSprite());
			System.out.println("ENEMY SPRITE" + tile.getEntities().peek().getSprite());
			sprite.setPosition(x,y);
			sprite.draw(batch);
		} else if(!tile.getEntities().isEmpty() && tile.getEntities().peek() instanceof HealthScroll){
			sprite = sprites.get(tile.getEntities().peek().getSprite());
			//System.out.println("HEALTH SCROLL SPRITE" + tile.getEntities().peek().getSprite());
			sprite.setPosition(x,y);
			sprite.draw(batch);
		} else if(!tile.getEntities().isEmpty() && tile.getEntities().peek() instanceof ArmorScroll) {
			sprite = sprites.get(tile.getEntities().peek().getSprite());
			//System.out.println("ARMOR SCROLL SPRITE" + tile.getEntities().peek().getSprite());
			sprite.setPosition(x, y);
			sprite.draw(batch);
		}
		else if(!tile.getEntities().isEmpty() && tile.getEntities().peek() instanceof Potion) {
			sprite = sprites.get(tile.getEntities().peek().getSprite());
			//System.out.println("POTION SPRITE" + tile.getEntities().peek().getSprite());
			sprite.setPosition(x, y);
			sprite.draw(batch);
		}
			else {
			sprite = sprites.get(name);
			sprite.setPosition(x, y);
			sprite.draw(batch);
		}

	}

	//creates HUD GUI & the map of the stats bars.
	public void createHUDGui(){
		Map<String,Integer> bars = new HashMap<>();
		bars.put("Health", 100);
		bars.put("Armour", 0);
		hudGui = new HUDGui(skin, bars);
		hudGui.setPosition(Gdx.graphics.getWidth(), inventoryGui.getHeight() + HEIGHT_PAD);
		hudGui.getColor().a = .8f;
		stage.addActor(hudGui);
		barList = hudGui.getHudBars();
	}

	//creates Inventory GUI
	public void createInventoryGui(){
		inventoryGui = new InventoryGui(skin, /*place holder*/ hero, this);
		inventoryGui.setPosition(Gdx.graphics.getWidth(), 0);
		inventoryGui.getColor().a = .8f;
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

	//a function that is called when a player clicks on an item from inventory to use
	public void usedItem(Item item){
		System.out.println("Player used x item");
		item.use(this.hero);
	}

	public void setShowInventory(boolean showInventory) {
		this.showInventory = showInventory;
	}

	public boolean isShowInventory() {
		return showInventory;
	}
}
