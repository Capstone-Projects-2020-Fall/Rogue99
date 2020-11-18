package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.mygdx.game.client.MPClient;
import com.mygdx.game.gui.*;
import com.mygdx.game.item.*;
import com.mygdx.game.interactable.Control;
import com.mygdx.game.interactable.Hero;
import com.mygdx.game.map.*;
import com.badlogic.gdx.scenes.scene2d.Stage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Rogue99 extends ApplicationAdapter {

	public final int HEIGHT_PAD = 132;
	public final String HEALTHBAR = "Health";
	public final String ARMOURBAR = "Armour";
	public final String SCOREBAR = "Score";


	public Hero hero;
	SpriteBatch batch;	public OrthographicCamera MapCamera;
	public MPClient client;
	FitViewport MapViewport;

	//map of enemy types per difficulty
	public HashMap<Integer, ArrayList<String>> enemyMap = new HashMap<>();

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
	int levelNum;

	//list of other players
	public ArrayList<Hero> players;
	long lastTime;

	LevelStage MapStage;
	Control control;

	boolean mapDrawn;
	boolean showInventory;
	boolean attacking;
	boolean mapGenerated;
	boolean seedReceived;
	boolean showMainMenu;
	public boolean multiplayer;
	public int timerCount = 0;
	boolean gameStarted;
	boolean rangeMode;
	boolean showPopUp;
	boolean showEscape;

	Item EquippedWeapon;
	String serverSeed;
	int serverDepth;

	MessageWindow gameLostWindow;

	public MainMenu mainMenu;
	public Stage mainMenuStage;
	NameInputWindow nameInputWindow;
	ExtendViewport mainMenuViewport;
	OrthographicCamera mainMenuCamera;

	public GameLobbyGui gameLobbyGui;

	Stage popUpStage;
	MessageWindow popUpWindow;
	long lastPopUp;
	ExitScreen exitScreen;

	Stage GuiElementStage;
	FitViewport GuiElementViewport;
	OrthographicCamera GuiCamera;


	@Override
	public void create () {
		batch = new SpriteBatch();
		levels = new ArrayList<>();
		players = new ArrayList<>();
		//establish enemy difficulty map
		setEnemyMap();


		//initialize camera and viewport
		MapCamera = new OrthographicCamera();
		MapViewport = new FitViewport(Gdx.graphics.getWidth()*0.8f, Gdx.graphics.getHeight(), MapCamera);
		MapCamera.setToOrtho(false, MapCamera.viewportWidth, MapCamera.viewportHeight);

		GuiElementStage = new Stage();
		GuiCamera = new OrthographicCamera();
		GuiElementViewport = new FitViewport(Gdx.graphics.getWidth()*0.20f, Gdx.graphics.getHeight(), GuiCamera);
		GuiElementStage.setViewport(GuiElementViewport);
		GuiElementStage.getViewport().setCamera(GuiCamera);
		GuiCamera.setToOrtho(false, GuiCamera.viewportWidth, GuiCamera.viewportHeight);

		mapDrawn = false;
		showInventory = false;
		attacking = false;
		mapGenerated = false;
		seedReceived = false;
		gameStarted = false;
		rangeMode = false;
		showPopUp = false;
		showEscape = false;

		//load sprites and add to hash map
		//textureAtlas = new TextureAtlas("spritesheets/sprites.txt");
//		textureAtlas = new TextureAtlas("spritesheets/fantasysprites.txt");
		textureAtlas = new TextureAtlas("spritesheets/fantasysprites3.txt");

		addSprites();

		//load skin for Inventory & HUD
		skin = new Skin(Gdx.files.internal("uiskin.json"));

		//create player's hero
		hero = new Hero(this, "hero");


		lastTime = System.currentTimeMillis();


		 gameLostWindow = new MessageWindow(this, "You Lost!", skin, "You have been defeated.");

		 mainMenuCamera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		 mainMenuViewport = new ExtendViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), mainMenuCamera);
		 mainMenuStage = new Stage();
		 mainMenuStage.setViewport(MapViewport);
		 mainMenuStage.getViewport().setCamera(MapCamera);
		 mainMenuCamera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

		 popUpStage = new Stage();
		 popUpStage.getViewport().setCamera(MapCamera);
		 popUpStage.setViewport(MapViewport);

		showMainMenu = true;
		mainMenu();
		//init_single_player();
		//init_multiplayer();
	}
	private void mainMenu() {
		mainMenu = new MainMenu(this,"", skin);
		mainMenuStage.addActor(mainMenu);
		gameLobbyGui = new GameLobbyGui(this,"",skin);
		Gdx.input.setInputProcessor(mainMenuStage);
	}
	private void init_single_player(){
		resetHero();

		Level tempLevel = new Level(null, 0, null);
		tempLevel.generateFloorPlan();
		generateLevel(tempLevel.getSeed(), 0);
		
		control = new Control(hero, this);

		Gdx.input.setInputProcessor(control);
	}

	private void init_multiplayer() {
		resetHero();

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

	public void resetHero(){
		hero.setCurrHP(100);
		hero.setSprite("hero");
		hero.getInventory().clear();
		hero.setArmor(0);
		hero.setStr(10);
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

			/* STAGE RENDERING BEGINS */
			if (showMainMenu) {
				Gdx.input.setInputProcessor(mainMenuStage);
				mainMenuStage.act();
				mainMenuStage.draw();
			} else if (mapGenerated) {
				MapCamera.zoom = 0.6f;
				MapStage.act();
				MapStage.getViewport().setScreenBounds(0,0, (int) (Gdx.graphics.getWidth()*0.8), Gdx.graphics.getHeight());
				MapStage.getViewport().apply();
				MapStage.draw();
				GuiElementStage.act();
				GuiElementStage.getViewport().setScreenBounds((int)(Gdx.graphics.getWidth()*0.8),0,(int)(Gdx.graphics.getWidth()*0.2),Gdx.graphics.getHeight() );
				GuiElementStage.getViewport().apply();
				GuiElementStage.draw();

				if (isShowEscape()) {
					exitScreen.setPosition(hero.getPosX() * 36 - exitScreen.getWidth() / 2, hero.getPosY() * 36 - exitScreen.getHeight() / 2);
					MapStage.addActor(exitScreen);
					Gdx.input.setInputProcessor(MapStage);
					MapStage.addListener(new InputListener() {
						@Override
						public boolean keyUp(InputEvent event, int keycode) {
							if (keycode == Input.Keys.ESCAPE) {
								setShowEscape(false);
								removeActor(exitScreen);
							}
							return super.keyUp(event, keycode);
						}
					});
				} else {
					Gdx.input.setInputProcessor(control);
					removeActor(inventoryGui);
					removeActor(hudGui);
				}


				if (System.currentTimeMillis() - lastTime > 1000) {
					hero.freezeTime(-1);
					lastTime = System.currentTimeMillis();
				}

				if (timerCount == 60) {
					level.moveEnemies();
				} else {
					timerCount++;
				}

				if(hero.getCurrHP() <= 0) {
					attacking = false;
					gameLostWindow.setPosition(hero.getPosX() * 36 - 127, hero.getPosY() * 36);
					Gdx.input.setInputProcessor(MapStage);
					removeActor(hudGui);
					removeActor(enemyHud);
					MapStage.addActor(gameLostWindow);
				}
				if(showInventory){
					Gdx.input.setInputProcessor(GuiElementStage);
					GuiElementStage.addListener(new InputListener() {
						@Override
						public boolean keyUp(InputEvent event, int keycode) {
							if (keycode == Input.Keys.I) {
								setShowInventory(false);
							}
							return super.keyUp(event, keycode);
						}
					});
				}
			}

				// Only Main thread has access to OpenGL so it needs to be the one generating the map
				// MPClient or its network listeners can't just use generateLevel because they are on a different thread.
				if (seedReceived) {
					generateLevel(serverSeed, serverDepth);
					seedReceived = false;
				}


				MapCamera.position.lerp(hero.pos3, 0.1f);
				MapCamera.update();
		/* STAGE RENDERING ENDS */
			}





	@Override
	public void dispose () {
		batch.dispose();
		textureAtlas.dispose();
		sprites.clear();
	}

	@Override
	public void resize(int width, int height) {
		MapViewport.update(width, height, true);
		batch.setProjectionMatrix(MapCamera.combined);
	}

	//adds sprites to hash map for more efficient use
	public void addSprites(){
		Array<TextureAtlas.AtlasRegion> regions = textureAtlas.getRegions();

		for(TextureAtlas.AtlasRegion region : regions){
			Sprite sprite = textureAtlas.createSprite(region.name);
			sprites.put(region.name, sprite);
		}
	}



	//creates HUD GUI & the map of the stats bars.
	public void createHUDGui(){
		Map<String,Integer> bars = new HashMap<>();
		bars.put(HEALTHBAR, 100);
		bars.put(ARMOURBAR, 0);
		hudGui = new HUDGui("OwnStats",skin, bars);
		hudGui.getTitleLabel().setText("PLAYER");
		System.out.println("Inventory Gui y: " + inventoryGui.getY() + " Inventory y + height " + inventoryGui.getY() + inventoryGui.getHeight() );
		hudGui.setPosition(-GuiElementStage.getWidth(), inventoryGui.getY() + inventoryGui.getHeight()*3.6f);
		hudGui.getColor().a = .8f;
		barList = hudGui.getHudBars();
		GuiElementStage.addActor(hudGui);
	}

	public void createEnemyHud(){
		Map<String, Integer> bars = new HashMap<>();
		bars.put("EnemyHP", 0);
		enemyHud = new HUDGui("EnemyStats",skin, bars);
		enemyBarList = enemyHud.getHudBars();
		enemyHud.getColor().a = .8f;
		enemyHud.setSize(26*3+40,26*(enemyBarList.size() + 1) + 80);
		enemyHud.setPosition(-GuiElementStage.getWidth() + hudGui.getWidth()*3.1f, inventoryGui.getY() + inventoryGui.getHeight()*3.6f);
		GuiElementStage.addActor(enemyHud);
	}

	//creates Inventory GUI
	public void createInventoryGui(){
		inventoryGui = new InventoryGui(skin, hero, this);
		inventoryGui.setPosition(-GuiElementStage.getWidth(), -GuiElementStage.getHeight());
		inventoryGui.getColor().a = .8f;
		GuiElementStage.addActor(inventoryGui);
	}

	//adjust stats bars
	public void changeBarValue(String barName, int newValue){
		for(HUDProgressBar bar : barList){
			if(bar.getName() == barName){
				bar.setValue(newValue);
			}
			if(multiplayer){
				Packets.Packet005Stats stats = new Packets.Packet005Stats();
				stats.name = hero.getName();
				stats.health = hero.getCurrHP();
				stats.armor = hero.getArmor();
				client.client.sendTCP(stats);
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
			} else if(item.getId() == Item.STRENGTHSCROLL) {
				// write code here for visual changes if wanted //
			} else if(item.getId() == Item.BANESCROLL) {

			} else if(item.getId() == Item.SUMMONSCROLL) {
				Packets.Packet009Scroll scroll = new Packets.Packet009Scroll();
				scroll.ID = Item.SUMMONSCROLL;
				scroll.type = ( (SummonScroll) item).getEnemyType();
				scroll.playerName = hero.getName();

				client.client.sendTCP(scroll);
			} else if(item.getId() == Item.FREEZEPOTION) {
				Packets.Packet004Potion potion = new Packets.Packet004Potion();
				potion.ID = Item.FREEZEPOTION;
				potion.value = ( (FreezePotion) item).getFreeze();
				potion.playerName = hero.getName();
				client.client.sendTCP(potion);
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
				hero.setSprite("hero_armed");
				EquippedWeapon = item;
			}
		} hero.score += (int)(Math.random()*50);
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
		level.setSeed(seed);
		level.generate();
		levels.add(level);
		if(MapStage==null){
			MapStage = new LevelStage(level, this);
			MapStage.getViewport().setCamera(MapCamera);
			MapStage.setViewport(MapViewport);
		} else {
			MapStage.setStageLevel(level);
		}
		System.out.println("Stage width and height:" + MapStage.getWidth() + " " + MapStage.getHeight());
		System.out.println("Player inventory size: " + hero.getInventory().size());
		generateGuiElements();
		mapGenerated = true;
		if(multiplayer) {
			Packets.Packet003Movement movement = new Packets.Packet003Movement();
			movement.xPos = hero.getPosX();
			movement.yPos = hero.getPosY();
			client.client.sendTCP(movement);
		}
	}
	public void nextLevel(int depth){
		level = levels.get(depth+1);
		MapStage.setStageLevel(level);
	}

	public void prevLevel(){
		level = levels.get(level.getDepth()-1);
		MapStage.setStageLevel(level);
	}

	// Get the seed from server
	public void setSeed (String seed, int depth){
		this.serverSeed = seed;
		this.serverDepth = depth;
		seedReceived = true;
	}

	public void setGameStarted(boolean gameStarted) {
		this.gameStarted = gameStarted;
		showMainMenu = false;
	}

	// Generate GUI Elements
	private void generateGuiElements(){
		//initialize Inventory & HUD gui
		createInventoryGui();
		createHUDGui();
		createEnemyHud();
		exitScreen = new ExitScreen(this, "Menu", skin);
		if(multiplayer){
			popUpWindow = new MessageWindow(this,"ALERT", skin, "");
			popUpWindow.setPosition(-GuiElementStage.getWidth(), GuiElementStage.getHeight());
			GuiElementStage.addActor(popUpWindow);
		}
  }

	public void newLevel(int depth){

		if(multiplayer) {
			Packets.Packet006RequestSeed request = new Packets.Packet006RequestSeed();
			depth++;
			request.depth = depth;
			System.out.println("Client: depth requested: " + request.depth);
			client.client.sendTCP(request);
		}
		else {
			Level temp = new Level(this, depth++, null);// single player option
			temp.generateFloorPlan();
			generateLevel(temp.getSeed(), depth);
		}
	}

	public Hero getHero() {
		return hero;
	}

	public void addPlayer(Hero player){
		System.out.println("Player Added");
		players.add(player);
		gameLobbyGui.addPlayer(player);
	}

	public void removePLayer(String playerName){
		for(Hero player : players){
			if(player.getName().equals(playerName)){
				gameLobbyGui.removePlayer(player);
				players.remove(player);
				return;
			}
		}
	}


	public void removeActor(Actor actor){
		if(showMainMenu){
			for(Actor a : mainMenuStage.getActors()){
				if(a.getName() == actor.getName()){
					a.remove();
				}
			}
		} else {
			for (Actor a : MapStage.getActors()) {
				if (a.getName() == actor.getName()) {
					if (a.getName() == "You Lost!") {
						if (multiplayer) {
							client.client.close();
							mainMenuStage.getActors().get(mainMenuStage.getActors().size -1).remove();
						}
						MapViewport.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);
						MapCamera.zoom = 1;
						showMainMenu = true;
						mapGenerated = false;
					}
					a.remove();
				}
			}
		}
	}

	public void menuButtonClicked(String buttonName) {
		if (buttonName.equals("Single Player")) {
			init_single_player();
			showMainMenu = false;
		} else if (buttonName.equals("Multiplayer")) {
			nameInputWindow = new NameInputWindow(this, "Set Username", skin);
			nameInputWindow.setPosition(MapCamera.viewportWidth / 2 - nameInputWindow.getWidth() / 2, MapCamera.viewportHeight / 2 - nameInputWindow.getHeight() / 2);
			mainMenuStage.addActor(nameInputWindow);
		} else if (buttonName.equals("Resume")) {
			setShowEscape(false);
			removeActor(exitScreen);
		} else if(buttonName.equals("Main Menu")) {
			MapViewport.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);
			MapCamera.zoom = 1;
			showMainMenu = true;
			mapGenerated = false;
			setShowEscape(false);
			removeActor(exitScreen);
			if(multiplayer){
				disconnectClient();
			}
		} else {
			Gdx.app.exit();
		}
	}

	public void setUserName(String userName){
		nameInputWindow.remove();
		hero.setName(userName);
		System.out.println(hero.getName());
		init_multiplayer();
		//showMainMenu = false;
	}

	public void setShowEscape(boolean showEscape) {
		this.showEscape = showEscape;
	}

	public boolean isShowEscape() {
		return showEscape;
	}

	public void popUpWindow(String sentBy, String receivedBy, String itemType){
		String message = "";
		if(itemType.equals("summon_scroll")){
			message = "Player " + sentBy + " summoned an enemy in " + receivedBy + "'s game!";
		} else if(itemType.equals("damage_potion")){
			message = "Player " + sentBy + " damaged " + receivedBy + "!";
		} else if(itemType.equals("freeze_potion")){
			message = "Player " + sentBy + " froze " + receivedBy + "!";
		}
		popUpWindow.getTextField().setText(message);
		lastPopUp = System.currentTimeMillis();
	}

	private void setEnemyMap(){
		enemyMap.put(0, new ArrayList<String>());
		enemyMap.get(0).add("rat");
		enemyMap.get(0).add("zombie");

		enemyMap.put(1, new ArrayList<String>());
		enemyMap.get(1).add("wasp");
		//enemyMap.get(1).add("skeleton");

		enemyMap.put(2, new ArrayList<String>());
		enemyMap.get(2).add("slime");
		enemyMap.get(2).add("ghost");
	}

	public void connectionRejected(String message){
		MessageWindow messageWindow = new MessageWindow(this,"Connection Rejected", skin,message);
		messageWindow.setPosition(MapCamera.viewportWidth/2 - messageWindow.getWidth()/2, MapCamera.viewportHeight/2 - messageWindow.getHeight());
		messageWindow.setMovable(true);
		mainMenuStage.addActor(messageWindow);
	}

	public void connectionAccepted(){
		mainMenuStage.addActor(gameLobbyGui);
		gameLobbyGui.removePlayer(hero);
		gameLobbyGui.addPlayer(hero);
	}

	public void setRangeMode(boolean rangeMode) {
		this.rangeMode = rangeMode;
	}

	public boolean isRangeMode() {
		return rangeMode;
	}

	public void setShowMainMenu(boolean showMainMenu) {
		this.showMainMenu = showMainMenu;
	}

	public boolean isShowMainMenu() {
		return showMainMenu;
	}

	public void disconnectClient(){
		client.client.close();
		multiplayer = false;
		mainMenuStage.getActors().get(mainMenuStage.getActors().size-1).remove();
		players.clear();
		gameLobbyGui = new GameLobbyGui(this, "", skin);
	}
}
