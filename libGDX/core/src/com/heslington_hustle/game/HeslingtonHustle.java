package com.heslington_hustle.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.heslington_hustle.screens.StartScreen;
import com.heslington_hustle.screens.minigames.BugfixerMinigame;

public class HeslingtonHustle extends Game {
	
	// Default values
	public static int windowWidth = 1920;
	public static int windowHeight = 1080;
	public float volume;
	
	public OrthographicCamera camera;
	public SpriteBatch batch;
	public BitmapFont font;
	public GlyphLayout layout = new GlyphLayout();
	
	public Preferences prefs;
	
	public Player player;
	public EnergyBar energyBar;
	public int day;
	
	public Screen[] minigames = new Screen[7];
	
	@Override
	public void create () {	
		// Set windowHeight and windowWidth to the monitor's resolution
		windowWidth = Gdx.graphics.getWidth();
		windowHeight = Gdx.graphics.getHeight();
		
		// Load save data
		loadData();
				
		// Create camera of dimensions 640x360 (pixel art canvas size)
		camera = new OrthographicCamera(640, 360);
		camera.position.set(camera.viewportWidth/2, camera.viewportHeight/2, 0); // Set camera centre to bottom left corner
		camera.update();
		
		// Add minigames to array
		initialiseMinigames();
		
		// Set custom cursor
		Pixmap pixmap = new Pixmap(Gdx.files.internal("Cursor.png"));
		Cursor cursor = Gdx.graphics.newCursor(pixmap, 0, 0);
		Gdx.graphics.setCursor(cursor);
		
		// Create spritebatch
		batch = new SpriteBatch();
		
		// Load font
		font = new BitmapFont(Gdx.files.internal("Yoster.fnt"));
		
		// Display start menu
		this.setScreen(new StartScreen(this));
	}
	
	private void initialiseMinigames() {
		minigames[0] = new BugfixerMinigame(this, 100);
		// etc...
	}
	
	public void loadData() {
		// Load save data
		prefs = Gdx.app.getPreferences("User Data");
		
		// Load saved display settings
		if(prefs.getBoolean("fullscreen", true)) {
			Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
		}
		else {
			if(prefs.getBoolean("borderless", false)) {
				Gdx.graphics.setUndecorated(true);
			}
			else {
				Gdx.graphics.setUndecorated(false);
			}
			Gdx.graphics.setWindowedMode(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		}
		
		// Load saved FPS limit
		Gdx.graphics.setForegroundFPS(prefs.getInteger("framerate", 60));
		
		// Load saved volume
		volume = prefs.getFloat("volume", 1.0f);
	}

	@Override
	public void render () {
		super.render();
	}
	
	@Override
	public void dispose () {

	}
}
