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
	
	@Override
	public void create () {
		batch = new SpriteBatch();
				
		// Set windowHeight and windowWidth to the monitor's resolution
		windowWidth = Gdx.graphics.getWidth();
		windowHeight = Gdx.graphics.getHeight();
		
		// Scale pixel art based on screen resolution
		pixelArtScalar = windowWidth/640f;
		
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
