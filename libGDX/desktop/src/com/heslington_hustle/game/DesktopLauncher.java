package com.heslington_hustle.game;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;

// Please note that on macOS your application needs to be started with the -XstartOnFirstThread JVM argument
public class DesktopLauncher {
	
	public static void main (String[] arg) {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		
		// Config settings
		config.setForegroundFPS(60);
		config.setIdleFPS(30);
		config.setTitle("Heslington Hustle");
		config.setWindowIcon("UI/GameIcon.png");
		config.setResizable(false);
		config.setFullscreenMode(Lwjgl3ApplicationConfiguration.getDisplayMode());
		
		// Start game
		new Lwjgl3Application(new HeslingtonHustle(), config);
	}
}
