package com.mygdx.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.mygdx.game.Rogue99;
import com.mygdx.game.gui.MainMenu;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.height = 2500;
		config.width = 2160;
		config.resizable = false;
		new LwjglApplication(new Rogue99(), config);
		//new LwjglApplication(new MainMenu(), config);
	}
}
