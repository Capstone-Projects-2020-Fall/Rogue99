package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.mygdx.game.gui.InventoryGui;

public class Rogue99 extends ApplicationAdapter {
	SpriteBatch batch;
	Texture img;
	Skin skin;
	InventoryGui inventoryGui;
	Stage stage;

	@Override
	public void create () {
		batch = new SpriteBatch();
		img = new Texture("badlogic.jpg");
		skin = new Skin(Gdx.files.internal("uiskin.json"));
		inventoryGui = new InventoryGui(skin);
		stage = new Stage(new ScreenViewport());
		stage.addActor(inventoryGui);
		Gdx.input.setInputProcessor(stage);
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(1,0,0,1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		stage.act();
		stage.draw();
//		batch.begin();
//		if (Gdx.input.isKeyPressed(Input.Keys.I)){
//			inventoryGui.draw(batch,1);
//		} else if (Gdx.input.isKeyPressed(Input.Keys.ANY_KEY)){
//			Gdx.gl.glClearColor(1, 0, 0, 1);
//			Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
//
//			batch.draw(img, Gdx.graphics.getWidth()/2 - img.getWidth()/2, Gdx.graphics.getHeight()/2 - img.getHeight()/2);
//		}
//		batch.end();
	}


	@Override
	public void dispose () {
		batch.dispose();
		img.dispose();
	}
}
