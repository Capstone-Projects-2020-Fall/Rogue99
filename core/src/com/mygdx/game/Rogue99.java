package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.mygdx.game.client.MPClient;
import com.mygdx.game.gui.HUDGui;
import com.mygdx.game.gui.HUDProgressBar;
import com.mygdx.game.gui.InventoryGui;
import com.mygdx.game.interactable.Enemy;
import com.mygdx.game.item.*;
import com.mygdx.game.interactable.Control;
import com.mygdx.game.interactable.Hero;
import com.mygdx.game.map.*;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.sun.java.swing.action.AlignRightAction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Rogue99 extends ApplicationAdapter {

	public final int PAD = 340;
	public final int HEIGHT_PAD = 132;
	public final String HEALTHBAR = "Health";
	public final String ARMOURBAR = "Armour";

	private Texture bg;
	private Texture play;
	private Texture play_hover;
	private Texture multi;
	private Texture multi_hover;
	private Texture setting;
	private Texture setting_hover;
	private Texture exit;
	private Texture exit_hover;
	private Texture Title;

	private Sprite sp;
	public static int WIDTH;
	public static int HEIGHT;







	public Hero hero;
	SpriteBatch batch;	public OrthographicCamera camera;
	public MPClient client;
	ExtendViewport viewport;

	Texture img;

	//Skin for inventory gui
	Skin skin;

	//InventoryGui Actor
	public InventoryGui inventoryGui;
	//HUD Actor
	public HUDGui hudGui;
	public HUDGui enemyHud;
	//HUD bars list
	ArrayList<HUDProgressBar> barList;
	public ArrayList<HUDProgressBar> enemyBarList;

	//texture atlas for sprite sheet
	TextureAtlas textureAtlas;
	public final HashMap<String, Sprite> sprites = new HashMap<>();

	//level objects
	//level list contains all levels generated so far
	public Level level;
	public ArrayList<Level> levels;

	//list of other players
	public ArrayList<Hero> players;

	Stage stage;
	Control control;

	boolean mapDrawn;
	boolean showInventory;
	boolean attacking;
	boolean mapGenerated;
	boolean seedReceived;
	boolean showMainMenu;
	public boolean multiplayer;

	Item EquippedWeapon;
	String serverSeed;
	int serverDepth;


	@Override
	public void create () {
		batch = new SpriteBatch();

		players = new ArrayList<>();

		//initialize camera and viewport
		camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		viewport = new ExtendViewport(2500, 2160, camera);
		camera.zoom = 0.4f;
		camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

		mapDrawn = false;
		showInventory = false;
		attacking = false;
		mapGenerated = false;
		seedReceived = false;

		//load sprites and add to hash map
		textureAtlas = new TextureAtlas("spritesheets/sprites.txt");
		addSprites();

		//load skin for Inventory & HUD
		skin = new Skin(Gdx.files.internal("uiskin.json"));

		//create player's hero
		hero = new Hero(this, "tile169");

		levels = new ArrayList<>();

//		showMainMenu = true;
//		mainMenu();
		//init_single_player();
		init_multiplayer();
	}
	private void mainMenu() {
		WIDTH = Gdx.graphics.getWidth();
		HEIGHT = Gdx.graphics.getHeight();
		camera = new OrthographicCamera(WIDTH, HEIGHT);
		camera.translate(WIDTH/2, HEIGHT/2);
		camera.update();
		bg = new Texture("spritesheets/cave.png");
		sp = new Sprite(bg);
		play = new Texture("spritesheets/play.png");
		play_hover = new Texture("spritesheets/play_hover.png");
		multi = new Texture("spritesheets/multiplay.png");
		multi_hover = new Texture("spritesheets/mutliplay_hover.png");
		setting = new Texture("spritesheets/setting.png");
		setting_hover = new Texture("spritesheets/setting_hover.png");
		exit = new Texture("spritesheets/exit.png");
		exit_hover = new Texture("spritesheets/exit_hover.png");
		Title = new Texture("spritesheets/title.png");
	}
	private void init_single_player(){
		Level tempLevel = new Level(null, 0, null);
		tempLevel.generateFloorPlan();
		generateLevel(tempLevel.getSeed(), 0);

		control = new Control(hero, this);

		Gdx.input.setInputProcessor(control);
	}

	private void init_multiplayer() {
		multiplayer = true;
		//initialize client
		client = new MPClient(this);

		//get seed from server
		Packets.Packet006RequestSeed seedRequest = new Packets.Packet006RequestSeed();
		seedRequest.depth = 0;
		System.out.println("Sending seed request for level 0");
		client.client.sendTCP(seedRequest);

		control = new Control(hero, this);
		Gdx.input.setInputProcessor(control);
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		batch.setProjectionMatrix(camera.combined);

		batch.begin();
		if (showMainMenu) {
			sp.draw(batch);
			batch.draw(play, WIDTH/2 - 100 / 2, 350, 100, 100);
			batch.draw(multi, WIDTH/2 - 170/2, 290, 170, 100);
			batch.draw(setting, WIDTH/2 - 170/2, 230, 170, 100);
			batch.draw(exit, WIDTH/2 - 100/2, 170, 100, 100);
			batch.draw(Title, WIDTH/2 - 500/2, 600, 500, 400);

			if (Gdx.input.getX() < WIDTH/2 - 100/2 + 100 && Gdx.input.getX() > WIDTH/2 -100/2 && HEIGHT - Gdx.input.getY() <
					350 + 100 && HEIGHT - Gdx.input.getY() > 350) { //cuts screen to make active when in tha zone
				batch.draw(play_hover,WIDTH/2 - 100 / 2, 350, 100, 100);
				if(Gdx.input.isTouched()) {
					init_single_player();
					showMainMenu = false;
				}

			} else if (Gdx.input.getX() < WIDTH/2 - 170/2 + 170 && Gdx.input.getX() > WIDTH/2 - 170/2 && HEIGHT - Gdx.input.getY() <
					290 + 100 && HEIGHT - Gdx.input.getY() > 290) {
				batch.draw(multi_hover, WIDTH/2 - 170/2, 290, 170, 100);
				if(Gdx.input.isTouched()) {
					init_multiplayer();
					showMainMenu = false;
				}
			} else if(Gdx.input.getX() < WIDTH/2 - 170/2 + 170 && Gdx.input.getX() > WIDTH/2 - 170/2 && HEIGHT - Gdx.input.getY() <
					230 + 100 && HEIGHT - Gdx.input.getY() > 230) {
				batch.draw(setting_hover, WIDTH/2 - 170/2, 230, 170, 100);
			} else if(Gdx.input.getX() <WIDTH/2 - 100/2 + 100 && Gdx.input.getX() > WIDTH/2 - 100/2 && HEIGHT - Gdx.input.getY() <
					170 + 100 && HEIGHT - Gdx.input.getY() > 170) {
				batch.draw(exit_hover, WIDTH/2 - 100/2, 170, 100, 100);
				if(Gdx.input.isTouched()) {
					Gdx.app.exit();
				}
			}

		}
		else{
			if (mapGenerated) {
				drawMap(level);
				stage.act();
				if (isShowInventory()) {
					Gdx.input.setInputProcessor(stage);
					inventoryGui.setPosition(hero.getPosX() * 36 + 72, hero.getPosY() * 36 - 108);
					hudGui.setPosition(hero.getPosX() * 36 + 72, hero.getPosY() * 36 + HEIGHT_PAD);
					enemyHud.setPosition(Gdx.graphics.getWidth(), 0);
					stage.draw();
					stage.addListener(new InputListener() {
						@Override
						public boolean keyUp(InputEvent event, int keycode) {
							if (keycode == Input.Keys.I) {
								setShowInventory(false);
							}
							return super.keyDown(event, keycode);
						}
					});
				} else {
					Gdx.input.setInputProcessor(control);
				}

				//if(!players.isEmpty()){
				drawHeroes();
				//}

				if (isAttacking()) {
					inventoryGui.setPosition(Gdx.graphics.getWidth(), 0);
					hudGui.setPosition(hero.getPosX() * 36 + 144, hero.getPosY() * 36);
					enemyHud.setPosition(hero.getPosX() * 36 - 144, hero.getPosY() * 36);
					stage.draw();
				}
			}

			// Only Main thread has access to OpenGL so it needs to be the one generating the map
			// MPClient or its network listeners can't just use generateLevel because they are on a different thread.
			if (seedReceived) {
				generateLevel(serverSeed, serverDepth);
				seedReceived = false;
			}

			camera.position.lerp(hero.pos3, 0.1f);
			camera.update();

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
			//System.out.println("ENEMY SPRITE" + tile.getEntities().peek().getSprite());
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
		else if(!tile.getEntities().isEmpty() && tile.getEntities().peek() instanceof HealthPotion) {
			sprite = sprites.get(tile.getEntities().peek().getSprite());
			//System.out.println("POTION SPRITE" + tile.getEntities().peek().getSprite());
			sprite.setPosition(x, y);
			sprite.draw(batch);
		}
		else if(!tile.getEntities().isEmpty() && tile.getEntities().peek() instanceof DamagePotion) {
			sprite = sprites.get(tile.getEntities().peek().getSprite());
			//System.out.println("POTION SPRITE" + tile.getEntities().peek().getSprite());
			sprite.setPosition(x, y);
			sprite.draw(batch);
		}
		else if(!tile.getEntities().isEmpty() && tile.getEntities().peek() instanceof Weapon) {
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

	private void drawHeroes(){
		//System.out.println("Drawing hero");
		for(Hero player : players) {
			System.out.println("Hero depth: " + hero.depth);
			System.out.println("player depth: " + player.depth);
			if(player.depth == hero.depth){
				System.out.println("same depth");
				Sprite sprite = sprites.get("players");
				sprite.setPosition(player.getPosX()*36, player.getPosY()*36);
				sprite.setColor(Color.CORAL);
				sprite.setAlpha(.5f);
				sprite.draw(batch);
			}
		}
	}

	//creates HUD GUI & the map of the stats bars.
	public void createHUDGui(){
		Map<String,Integer> bars = new HashMap<>();
		bars.put(HEALTHBAR, 100);
		bars.put(ARMOURBAR, 0);
		hudGui = new HUDGui(skin, bars);
		hudGui.setPosition(Gdx.graphics.getWidth(), inventoryGui.getHeight() + HEIGHT_PAD);
		hudGui.getColor().a = .8f;
		stage.addActor(hudGui);
		barList = hudGui.getHudBars();
	}

	public void createEnemyHud(){
		Map<String, Integer> bars = new HashMap<>();
		bars.put("EnemyHP", 0);
		bars.put("EnemyAR", 0);
		enemyHud = new HUDGui(skin, bars);
		enemyBarList = enemyHud.getHudBars();
		enemyHud.getColor().a = .8f;
		enemyHud.setSize(26*3+40,26*(enemyBarList.size() + 1) + 80);
		stage.addActor(enemyHud);
	}

	//creates Inventory GUI
	public void createInventoryGui(){
		inventoryGui = new InventoryGui(skin, hero, this);
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
		for(HUDProgressBar bar : enemyBarList){
			if(bar.getName() == barName){
				bar.setValue(newValue);
			}
		}
	}

	//a function that is called when a player clicks on an item from inventory to use
	public void usedItem(Item item){
		System.out.println("Player used x item");
		System.out.println(hero.getMaxHP());
		if(!item.use(hero)){
			inventoryGui.addItemToInventory(item);
		} else {
			item.use(hero);
			if(item.getId() == Item.HEALTHPOTION){
				changeBarValue(HEALTHBAR, hero.getCurrHP());
				hudGui.statsNumTexts.get(1).setText(String.valueOf(hero.getCurrHP()));
				System.out.println(hudGui.getHudBars().get(1).getValue());
				System.out.println(hero.getCurrHP());
			}
			else if(item.getId() == Item.DAMAGEPOTION){
				Packets.Packet004Potion potion = new Packets.Packet004Potion();
				potion.ID = Item.DAMAGEPOTION;
				potion.value = ( (DamagePotion) item).getDmgAmt();

				client.client.sendTCP(potion);
			}
			else if(item.getId() == Item.ARMORSCROLL){
				changeBarValue(ARMOURBAR, hero.getArmor());
				hudGui.statsNumTexts.get(0).setText(String.valueOf(hero.getArmor()));
			} else if(item.getId() == Item.HEALTHSCROLL) {
				// write code here for visual changes if wanted //
			} else if(item.getId() == Item.STRENGTHSCROLL){
				// write code here for visual changes if wanted //
			} else if(item.getId() == Item.WEAPON){
					item.setEquipped(true);
					System.out.println("Weapon used: " + item.use(hero));
					if(EquippedWeapon != null){
						EquippedWeapon.setEquipped(false);
						// revert changes from previous weapon to damage //
						hero.setStr(hero.getStr() - EquippedWeapon.getDmgModifier());
					}
					// do something with damage modifier in combat //
					hero.setStr(hero.getStr() + item.getDmgModifier());
					EquippedWeapon = item;
			}
		}
	}

	public void setShowInventory(boolean showInventory) {
		this.showInventory = showInventory;
	}

	public boolean isShowInventory() {
		return showInventory;
	}

	public void setAttacking(boolean attacking) {
		//System.out.println(attacking);
		this.attacking = attacking;
	}

	public boolean isAttacking() {
		return attacking;
	}


	// generate the level and level stage
	public void generateLevel(String seed, int depth){
		//initialize level
		System.out.println("generateLevel seed: " + seed);
		level = new Level(this, depth, hero);
		levels.add(level);
		level.setSeed(seed);
		level.generate();

		stage = new LevelStage(level);
		stage.getViewport().setCamera(camera);
		stage.setViewport(viewport);
		generateGuiElements();
		mapGenerated = true;
		Packets.Packet003Movement movement = new Packets.Packet003Movement();
		movement.xPos = hero.getPosX();
		movement.yPos = hero.getPosY();
		client.client.sendTCP(movement);
	}


	// Get the seed from server
	public void setSeed (String seed, int depth){
		this.serverSeed = seed;
		this.serverDepth = depth;
		seedReceived = true;
	}

	// Generate GUI Elements
	private void generateGuiElements(){
		//initialize Inventory & HUD gui
		createInventoryGui();
		createHUDGui();
		createEnemyHud();
  }

	public void newLevel(int depth){
		if(multiplayer) {
			Packets.Packet006RequestSeed request = new Packets.Packet006RequestSeed();
			request.depth = depth;
			System.out.println("Client: depth requested: " + request.depth);
			client.client.sendTCP(request);
		}
		else {			// single player option
			level.generateFloorPlan();
			generateLevel(level.getSeed(), depth++);
		}
	}

	public Hero getHero() {
		return hero;
	}

	public void addPlayer(Hero player){
		System.out.println("Player Added");
		players.add(player);
	}
}
