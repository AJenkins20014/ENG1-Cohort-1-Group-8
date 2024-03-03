package com.heslington_hustle.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.heslington_hustle.screens.StartScreen;
import com.heslington_hustle.screens.minigames.BugfixerMinigame;

public class HeslingtonHustle extends Game {
	
	// Default values
	public static int windowWidth = 1920;
	public static int windowHeight = 1080;
	public static float pixelArtScalar = 3;
	
	public SpriteBatch batch;
	public BitmapFont font;
	
	public Player player;
	public EnergyBar energyBar;
	public int day;
	
	public Screen[] minigames = new Screen[7];
	
	@Override
	public void create () {
		batch = new SpriteBatch();
				
		// Set windowHeight and windowWidth to the monitor's resolution
		windowWidth = Gdx.graphics.getWidth();
		windowHeight = Gdx.graphics.getHeight();
		
		// Scale pixel art based on screen resolution
		pixelArtScalar = windowWidth/640f; // Current pixel art canvas size is 640x360. To ensure consistency, pixel art should be scaled using this info.
		
		// Add minigames to array
		initialiseMinigames();
		
		// Set custom cursor
		Pixmap pixmap = new Pixmap(Gdx.files.internal("Cursor.png"));
		Cursor cursor = Gdx.graphics.newCursor(pixmap, 0, 0);
		Gdx.graphics.setCursor(cursor);
		
		// Load font
		font = new BitmapFont(Gdx.files.internal("Yoster.fnt"));
		
		// Display start menu
		this.setScreen(new StartScreen(this));
	}
	
	private void initialiseMinigames() {
		minigames[0] = new BugfixerMinigame(this, 100);
		// etc...
	}

	@Override
	public void render () {
		super.render();
	}
	
	@Override
	public void dispose () {

	}
}
