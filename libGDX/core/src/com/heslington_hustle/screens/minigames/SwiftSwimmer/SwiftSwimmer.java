package com.heslington_hustle.screens.minigames.SwiftSwimmer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Align;
import com.heslington_hustle.game.HeslingtonHustle;
import com.heslington_hustle.game.PopUpText;
import com.heslington_hustle.screens.InformationScreen;
import com.heslington_hustle.screens.MinigameScreen;

public class SwiftSwimmer extends MinigameScreen implements Screen {
	public float maxtime;
	private float clock = 0;
	private boolean minimised;
	private float energyGained;
	private float maxEnergyGained;
	private int score;
	public Swimmer swimmer;
	private Texture background;
	private Music backgroundMusic;
	public static Sound splash;
	
	public static Animation<TextureRegion> swimAnimation;
	private Texture swimSheet;

	public SwiftSwimmer(HeslingtonHustle game, float difficultyScalar) {
		super(game, difficultyScalar);
		this.energyGained = 20f; // From worst possible performance
		this.maxEnergyGained = 60f; // From best possible performance
		
		// Load textures
		background = new Texture("SwiftSwimmerMinigame/Background.png");
		
		// Load sounds
		backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("Music/SwiftSwimmerMusic.ogg"));
		backgroundMusic.setLooping(true);
						
		splash = Gdx.audio.newSound(Gdx.files.internal("SwiftSwimmerMinigame/Splash.mp3"));
	}
	
	@Override
	public void startGame() {
		loadAnimations();
		clock = 0;
		maxtime = 16 - difficultyScalar*4;
		// Code to restart the game
		energyGained = 20f; // From worst possible performance
		
		// If in borderless, set to fullscreen to pause game if unfocused
		if(game.isBorderless) {
			Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
		}
		
		
		// Display tutorial
	    game.setScreen(new InformationScreen(game,"swiftSwimmerTutorial", this));
	    
	    //Declares a swimmer
		swimmer = new Swimmer(game);
		
	}
	
	private void endGame() {
		Gdx.input.setCursorPosition(320, 170);
		//Calculates score and energy gained from laps done
		score = swimmer.laps*75;
		energyGained += score/15;

		// Check minigame high score
		if(game.prefs.getInteger("swiftSwimmerHighScore", 0) < score) {
			game.prefs.putInteger("swiftSwimmerHighScore", score);
			game.prefs.flush();
		}
		
		if(energyGained > maxEnergyGained) {
			energyGained = maxEnergyGained;
		}
		
		game.energyBar.addEnergy(energyGained);
		
		// Reset window back to borderless
		if(game.isBorderless) {
			Gdx.graphics.setUndecorated(true);
			Gdx.graphics.setWindowedMode(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		}
		
		// Display final score
		game.setScreen(new InformationScreen(game, "recreationGameScore", game.map, score, energyGained));
		
	}
	
	@Override
	public void render(float delta) {
		
		if(minimised) return;
		
		// Clear the screen
		Gdx.gl.glClearColor(16/255f, 20/255f, 31/255f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		// Set projection matrix of the batch to the camera
		game.batch.setProjectionMatrix(game.camera.combined);
		game.camera.update();
		
		// Get mouse position in world coordinates
		Vector3 mousePos = game.camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 1f));

		// Draw background
		game.batch.begin();
		game.batch.draw(background, 0, 0);
		game.batch.end();
		
		
		game.batch.begin();
		game.font.getData().setScale(0.3f); // Set font size
		//Shows remaining time and laps completed
		game.font.draw(game.batch, "Time Remaining: " + Integer.toString((int)Math.round(15 - Math.ceil(clock) )), 100, 350, 100, Align.center, false);
		game.font.draw(game.batch, "Laps Completed: " + Integer.toString((swimmer.laps)), 400, 350, 100, Align.center, false);
		
		
		//Ends game after 15 seconds
		if(clock > maxtime) {
			endGame();
		}
		
		// Check if player has paused the game
		if(Gdx.input.isKeyJustPressed(Keys.ESCAPE)) {
			game.togglePause();
		}
		if(game.paused) {
			game.pauseMenu();
		}
		
		game.batch.end();
		
		updateMusicVolume();
		
				
		if(game.paused) return;
		// Anything that shouldn't happen while the game is paused should go here
		
		//Increment delta time
		clock += Gdx.graphics.getDeltaTime();
		
		//Move Swimmer
		swimmer.Swim();
	}
	
	private void loadAnimations(){
		// Load animations
		if(game.player.avatarNumber == 0) {
			swimSheet = new Texture("SwiftSwimmerMinigame/Avatar1.png");
		}
		else if(game.player.avatarNumber == 1) {
			swimSheet = new Texture("SwiftSwimmerMinigame/Avatar2.png");
		}
		else if(game.player.avatarNumber == 2) {
			swimSheet = new Texture("SwiftSwimmerMinigame/Avatar3.png");
		}
		else{
			swimSheet = new Texture("SwiftSwimmerMinigame/Avatar4.png");
		}
		
		TextureRegion[][] swimTexture = TextureRegion.split(swimSheet,
				swimSheet.getWidth() / 2,
				swimSheet.getHeight());

		TextureRegion[] swimFrames = new TextureRegion[2];
		int index = 0;
		for (int j = 0; j < 2; j++) {
			swimFrames[index++] = swimTexture[0][j];
		}
		swimAnimation = new Animation<TextureRegion>(1, swimFrames);
		
	}
	
	@Override
	public void resize(int width, int height) {
		if(width == 0 && height == 0) {
			minimised = true;
			game.togglePause();
		}
		else {
			minimised = false;
		}
	}
	
	@Override
	public void hide() {
		// Stop music
		backgroundMusic.stop();
	}
	
	@Override
	public void show() {
		// Play music
		backgroundMusic.play();
	}
	
	private void updateMusicVolume() {
		float musicVolume = game.volume/2;
		backgroundMusic.setVolume(musicVolume);
	}
	
	@Override
	public void dispose() {
		
	}

}
