package com.heslington_hustle.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.heslington_hustle.screens.StartScreen;

public class HeslingtonHustle extends Game {
	
	// Default values
	public static int windowWidth = 1920;
	public static int windowHeight = 1080;
	public static float pixelArtScalar = 3;
	
	public SpriteBatch batch;
	
	public Player player;
	public EnergyBar energyBar;
	public int day;
	
	public Minigame[] minigames = new Minigame[7];
	
	@Override
	public void create () {
		batch = new SpriteBatch();
				
		// Set windowHeight and windowWidth to the monitor's resolution
		windowWidth = Gdx.graphics.getWidth();
		windowHeight = Gdx.graphics.getHeight();
		
		// Scale pixel art based on screen resolution
		pixelArtScalar = windowWidth/640f;
		
		// Add minigames to array
		minigames[0] = new Minigame(this, "test minigame", 100);
		
		this.setScreen(new StartScreen(this));
	}

	@Override
	public void render () {
		super.render();
	}
	
	@Override
	public void dispose () {

	}
}
