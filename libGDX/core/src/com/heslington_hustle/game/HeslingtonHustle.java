package com.heslington_hustle.game;

import java.util.ArrayList;
import java.util.HashMap;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Align;
import com.heslington_hustle.screens.Map;
import com.heslington_hustle.screens.MinigameScreen;
import com.heslington_hustle.screens.StartScreen;
import com.heslington_hustle.screens.minigames.ExamGame;
import com.heslington_hustle.screens.minigames.BookStacker.BookStacker;
import com.heslington_hustle.screens.minigames.BugFixer.BugFixer;
import com.heslington_hustle.screens.minigames.DrunkDancer.DrunkDancer;
import com.heslington_hustle.screens.minigames.ColourMatch.ColourMatch;
import com.heslington_hustle.screens.minigames.Squash.Squash;
import com.heslington_hustle.screens.minigames.SwiftSwimmer.SwiftSwimmer;

public class HeslingtonHustle extends Game {
	
	// Default values
	public static int windowWidth = 1920;
	public static int windowHeight = 1080;
	public float volume;
	public boolean isBorderless; // For use in minigames
	
	public OrthographicCamera camera;
	public SpriteBatch batch;
	public BitmapFont font;
	public GlyphLayout layout = new GlyphLayout();
	
	public Preferences prefs; // Contains save data
	
	public Map map;
	public Player player;
	public EnergyBar energyBar;
	public int day;
	public int time; // Hour
	public HashMap<String, Float> studyPoints = new HashMap<>(); // Is used to calculate final score and alter exam difficulty (Minigame Name | Study Points)
	public int[] timesStudied = new int[7]; // Number of times studied per day.
	public int timesEatenToday; // Number of times eaten on the current day
	
	public MinigameScreen[] minigames = new MinigameScreen[7];
	public ExamGame exam = new ExamGame(this, 1f);
	
	public boolean paused; // If game is paused
	public boolean confirmQuit; // If the player is about to quit
	
	private float clock; // Incremented every frame
	private ArrayList<PopUpText> popUps = new ArrayList<>();
	private ArrayList<Float> popUpTimers = new ArrayList<>(); // How long each pop up has been displayed for
	private float popUpAlpha; // Opacity of pop up text
	
	private float fadeClock; // Clock for the fadeToBlack() method
	private float fadeAlpha; // Opacity of the black screen
	private Sprite blackScreen; 
	
	public Music menuMusic;
	public Music mapMusic;
	public Sound menuClick;
	public Cursor cursor;
	
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
		Pixmap pixmap = new Pixmap(Gdx.files.internal("UI/Cursor.png"));
		cursor = Gdx.graphics.newCursor(pixmap, 0, 0);
		Gdx.graphics.setCursor(cursor);
		
		// Create spritebatch
		batch = new SpriteBatch();
		
		// Load font
		font = new BitmapFont(Gdx.files.internal("UI/Yoster.fnt"));

		// Load sounds
		menuMusic = Gdx.audio.newMusic(Gdx.files.internal("Music/MenuMusic.ogg"));
		menuMusic.setLooping(true);
		
		mapMusic = Gdx.audio.newMusic(Gdx.files.internal("Music/MapMusic.ogg"));
		mapMusic.setLooping(true);
		
		menuClick = Gdx.audio.newSound(Gdx.files.internal("UI/ButtonClick.mp3"));
		
		// Load textures
		blackScreen = new Sprite(new Texture("UI/BlackScreen.png"));
		fadeClock = 3;
		
		// Display start menu
		this.setScreen(new StartScreen(this));
	}
	
	private void initialiseMinigames() {
		// Replace these with new minigames as they are added
		minigames[0] = new BugFixer(this, 1);
		minigames[1] = new BookStacker(this, 1);
		minigames[2] = new ColourMatch(this, 1);
		minigames[3] = new SwiftSwimmer(this, 1);
		minigames[4] = new Squash(this, 1);
		minigames[5] = new DrunkDancer(this, 1);
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
				isBorderless = true;
			}
			else {
				Gdx.graphics.setUndecorated(false);
				isBorderless = false;
			}
			Gdx.graphics.setWindowedMode(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		}
		
		// Load saved FPS limit
		Gdx.graphics.setForegroundFPS(prefs.getInteger("framerate", 60));
		
		// Load saved volume
		volume = prefs.getFloat("volume", 0.5f);
	}
	
	public void togglePause() {
		if(!paused) {
			paused = true;
		}
		else {
			paused = false;
			confirmQuit = false;
		}
	}
	
	public void pauseMenu() {
		// Draw menu box
		batch.draw(new Texture("UI/PauseMenu.png"), 0, 0);
		
		// Get mouse position in world coordinates
		Vector3 mousePos = camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 1f));
		
		font.getData().setScale(0.7f); // Set font size
		
		
		// Volume settings
		layout.setText(font, "Volume:   " + Integer.toString(Math.round(volume*10)), new Color(1, 1, 1, 1), 100, Align.left, false);
		font.draw(batch, layout, 200, 180);
		if(volume > 0) {
			if(mousePos.x < 375 && mousePos.x > 345 && mousePos.y < 180 && mousePos.y > 180 - layout.height) {
				layout.setText(font, "<", new Color(232/255f, 193/255f, 112/255f, 1), 100, Align.bottomLeft, false);
				font.draw(batch, layout, 345, 180);
				if(Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
					// Lower volume
					menuClick.play(volume);
					if(volume-0.1f < 0) {
						volume = 0f;
					}
					else {
						volume -= 0.1f;
					}
					// Save data
					prefs.putFloat("volume", volume);
					prefs.flush();
				}
			}
			else {
				layout.setText(font, "<", new Color(1, 1, 1, 1), 100, Align.bottomLeft, false);
				font.draw(batch, layout, 345, 180);
			}
		}
		if(volume < 1) {
			if(mousePos.x < 450 && mousePos.x > 420 && mousePos.y < 180 && mousePos.y > 180 - layout.height) {
				layout.setText(font, ">", new Color(232/255f, 193/255f, 112/255f, 1), 100, Align.bottomLeft, false);
				font.draw(batch, layout, 420, 180);
				if(Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
					// Raise volume
					menuClick.play(volume);
					volume += 0.1f;
					prefs.putFloat("volume", volume);
					prefs.flush();
				}
			}
			else {
				layout.setText(font, ">", new Color(1, 1, 1, 1), 100, Align.bottomLeft, false);
				font.draw(batch, layout, 420, 180);
			}
		}
		
		
		// Quit
		layout.setText(font, "End Run and Quit", new Color(207/255f, 87/255f, 60/255f, 1), 100, Align.left, false);
		font.draw(batch, layout, 160, 120);
		if(mousePos.x < 160 + layout.width && mousePos.x > 160 && mousePos.y < 120 && mousePos.y > 120 - layout.height) {
			layout.setText(font, "End Run and Quit", new Color(218/255f, 134/255f, 62/255f, 1), 100, Align.bottomLeft, false);
			font.draw(batch, layout, 160, 120);
			if(Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
				// Confirm quit
				menuClick.play(volume);
				if(confirmQuit) {
					this.dispose();
					setScreen(new StartScreen(this));
					confirmQuit = false;
					paused = false;
					menuMusic.play();
				}
				else {
					confirmQuit = true;
				}
			}
		}
		
		// Resume tooltip
		if(!confirmQuit) {
			font.draw(batch, "Press ESC to resume", 125, 50);
		}
		else {
			font.getData().setScale(0.6f); // Set font size
			font.draw(batch, "Click again to confirm quit", 100, 50);
		}
	}
	
	public void addPopUpText(PopUpText popUpText, float time) {
		if(popUps.size() > 10) {
			// Avoid popup spam
			return;
		}
		else if(popUps.size() == 0) {
			clock = 0; // Reset clock
			popUpAlpha = 1; // Reset fade
		}
		for(int i = 0; i < popUps.size(); i++) {
			if(popUpText.equals(popUps.get(i)) && time == popUpTimers.get(i)) {
				// Same popup and timer, avoid duplication
				return;
			}
		}
		popUps.add(popUpText);
		popUpTimers.add(time);
	}
	
	private void renderPopUps(boolean fade) {
		font.getData().setScale(popUps.get(0).scale); // Set font size
		if(!fade) {
			font.setColor(popUps.get(0).color); // Set font color
		}
		else {
			font.setColor(new Color(popUps.get(0).color.r, popUps.get(0).color.g, popUps.get(0).color.b, popUpAlpha)); // Fade out popup
		}
		
		font.draw(batch, popUps.get(0).text, popUps.get(0).x, popUps.get(0).y, popUps.get(0).targetWidth, popUps.get(0).align, popUps.get(0).wrap);
		
		font.setColor(new Color(1,1,1,1)); // Reset font color
	}
	
	private void updateMusicVolume() {
		float musicVolume = volume/2;
		menuMusic.setVolume(musicVolume);
		mapMusic.setVolume(musicVolume);
	}
	
	public void fadeToBlack() {
		// Reset fade clock and opacity
		fadeClock = 0f;
		fadeAlpha = 0f;
	}

	@Override
	public void render () {
		super.render();
		
		// Increment timers
		clock += Gdx.graphics.getDeltaTime();
		fadeClock += Gdx.graphics.getDeltaTime();
		
		// Begin drawing on screen
		batch.begin();
		
		// Clear pop ups if not on map screen
		if(!(this.getScreen() instanceof Map)) {
			popUps.clear();
		}
		
		// Code for fading and displaying pop ups over time
		// Pop ups are stored as an array so multiple can be stored and displayed in order
		if(popUps.size() > 0) {
			if(clock > popUpTimers.get(0)) {
				popUps.remove(0);
				clock = 0;
			}
			else if (popUpTimers.get(0) - clock < 1) {
				renderPopUps(true);
				popUpAlpha = popUpTimers.get(0) - clock;
			}
			else {
				renderPopUps(false);
			}
		}
		
		// Fade to black (if needed)
		if(fadeClock < 1) {
			fadeAlpha = fadeClock;
			blackScreen.setAlpha(fadeAlpha);
			blackScreen.draw(batch);
		}
		else if(fadeClock < 2) {
			fadeAlpha = 1-(fadeClock-1);
			blackScreen.setAlpha(fadeAlpha);
			blackScreen.draw(batch);
		}
		
		// Stop drawing on screen
		batch.end();
		
		updateMusicVolume();
	}
	
	@Override
	public void dispose () {

	}
}
