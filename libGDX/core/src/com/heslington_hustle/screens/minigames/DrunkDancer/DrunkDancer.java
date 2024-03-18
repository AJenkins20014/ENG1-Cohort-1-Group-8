package com.heslington_hustle.screens.minigames.DrunkDancer;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Align;
import com.heslington_hustle.game.HeslingtonHustle;
import com.heslington_hustle.game.PopUpText;
import com.heslington_hustle.screens.InformationScreen;
import com.heslington_hustle.screens.MinigameScreen;

public class DrunkDancer extends MinigameScreen implements Screen {
	private boolean minimised;
	private float energyGained;
	private float maxEnergyGained;
	public int score;
	public float health;
	
	private float clock;
	private float QTECooldown;
	private float timeSinceLastQTE;
	private float QTESpeed;
	
	public float yMin = -50f;
	public float yMax = 50f;
	public float yMinGood = -25f;
	public float yMaxGood = 25f;
	public float yMinPerfect = -10f;
	public float yMaxPerfect = 10f;
	public ArrayList<QuickTimeEvent> QTEs = new ArrayList<>();
	
	private String performance;
	private Texture background;
	private Texture healthBar1, healthBar2, healthBar3;
	private Music backgroundMusic;
	private Sound hit;
	private Sound miss;

	public DrunkDancer(HeslingtonHustle game, float difficultyScalar) {
		super(game, difficultyScalar);
		this.energyGained = 20f; // From worst possible performance
		this.maxEnergyGained = 60f; // From best possible performance
		
		// Load textures
		background = new Texture("DrunkDancerMinigame/Background.png");
		healthBar1 = new Texture("BugFixerMinigame/HealthBar1.png");
		healthBar2 = new Texture("BugFixerMinigame/HealthBar2.png");
		healthBar3 = new Texture("BugFixerMinigame/HealthBar3.png");
		
		// Load sounds
		backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("Music/DrunkDancerMusic.ogg"));
		backgroundMusic.setLooping(true);
		
		hit = Gdx.audio.newSound(Gdx.files.internal("DrunkDancerMinigame/Hit.mp3"));
		miss = Gdx.audio.newSound(Gdx.files.internal("DrunkDancerMinigame/Miss.mp3"));
	}
	
	@Override
	public void startGame() {
		// Code to restart the game
		score = 0;
		health = 100f;
		clock = 0;
		timeSinceLastQTE = 0;
		QTEs.clear();
		performance = "";
		energyGained = 20f; // From worst possible performance
		
		// If in borderless, set to fullscreen to pause game if unfocused
		if(game.isBorderless) {
			Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
		}
		
		// Display tutorial
	    game.setScreen(new InformationScreen(game, "drunkDancerTutorial", this));
	}
	
	public void endGame() {
		energyGained = score/20;
		
		// Check minigame high score
		if(game.prefs.getInteger("drunkDancerHighScore", 0) < score) {
			game.prefs.putInteger("drunkDancerHighScore", score);
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
		
		
		game.batch.begin();
		
		game.batch.draw(background, 0, 0);

		if(Gdx.input.isKeyJustPressed(Keys.A) || Gdx.input.isKeyJustPressed(Keys.LEFT)) {
			checkQTE(Keys.A);
		}
		if(Gdx.input.isKeyJustPressed(Keys.W) || Gdx.input.isKeyJustPressed(Keys.UP)) {
			checkQTE(Keys.W);
		}
		if(Gdx.input.isKeyJustPressed(Keys.S) || Gdx.input.isKeyJustPressed(Keys.DOWN)) {
			checkQTE(Keys.S);
		}
		if(Gdx.input.isKeyJustPressed(Keys.D) || Gdx.input.isKeyJustPressed(Keys.RIGHT)) {
			checkQTE(Keys.D);
		}
		
		// Call update methods for all QTEs and check for destruction
		for(int i = 0; i < QTEs.size(); i++) {	
			if(QTEs.get(i).y < -50f) {
				QTEs.get(i).destroy();
				health -= 15f;
				miss.play(game.volume);
				performance = "Too late!";
			}
			else {
				QTEs.get(i).update();
			}
		}
		
		drawUI();
		
		// Check if player has paused the game
		if(Gdx.input.isKeyJustPressed(Keys.ESCAPE)) {
			game.togglePause();
		}
		if(game.paused) {
			game.pauseMenu();
		}

		game.batch.end();
			
		if(game.paused) return;
		// Anything that shouldn't happen while the game is paused should go here
		
		if(health <= 0f) {
			endGame();
		}
		
		rampDifficulty();
		
		if(timeSinceLastQTE > QTECooldown) {
			timeSinceLastQTE = 0;
			newQTE();
		}
		
		clock += Gdx.graphics.getDeltaTime();
		QTECooldown += Gdx.graphics.getDeltaTime();
		timeSinceLastQTE += Gdx.graphics.getDeltaTime();
		updateMusicVolume();
	}
	
	private void checkQTE(int input) {
		boolean QTEFound = false;
		for(int i = 0; i < QTEs.size(); i++) {	
			if(QTEs.get(i).y < yMaxPerfect && QTEs.get(i).y > yMinPerfect && QTEs.get(i).input == input) {
				QTEs.get(i).destroy();
				score += 30f;
				hit.play(game.volume);
				performance = "Perfect!";
				QTEFound = true;
			}
			else if(QTEs.get(i).y < yMaxGood && QTEs.get(i).y > yMinGood && QTEs.get(i).input == input) {
				QTEs.get(i).destroy();
				score += 20f;
				performance = "Good";
				QTEFound = true;
			}
			else if(QTEs.get(i).y < yMax && QTEs.get(i).y > yMin && QTEs.get(i).input == input) {
				QTEs.get(i).destroy();
				score += 10f;
				performance = "Ok";
				QTEFound = true;
			}
		}
		
		if(!QTEFound) {
			score -=5f;
			health -=5f;
			miss.play(game.volume);
			performance = "Too early!";
		}
	}
	
	private void newQTE() {
		List<Integer> keys = List.of(Keys.A, Keys.W, Keys.S, Keys.D);
        int randomIndex = new Random().nextInt(keys.size());
        int input = keys.get(randomIndex);
		
		QTEs.add(new QuickTimeEvent(game, this, input, QTESpeed));
	}
	
	private void rampDifficulty() {
		// Decrease time between enemy spawns as game progresses
		if(clock < 3f) {
			QTECooldown = 2f/difficultyScalar;
		}
		else if (clock < 7f) {
			QTECooldown = 1f/difficultyScalar;
		}
		else if (clock < 13f) {
			QTECooldown = 0.75f/difficultyScalar;
		}
		else if (clock < 20f) {
			QTECooldown = 0.5f/difficultyScalar;
		}
		else if (clock < 30f) {
			QTECooldown = 0.25f/difficultyScalar;
		}
		else {
			QTECooldown = (1f/((clock+10f)/10f))/difficultyScalar;
		}
		
		QTESpeed = (clock+10)*10f;
	}
	
	private void drawUI() {
		// Draw health bar
		game.batch.draw(healthBar1, game.camera.viewportWidth/2 - healthBar1.getWidth()/2, 350);
		TextureRegion region = new TextureRegion(healthBar2, (int)Math.round(284f*(health/100f)), healthBar2.getHeight());
		game.batch.draw(region, game.camera.viewportWidth/2 - healthBar1.getWidth()/2, 350);
		game.batch.draw(healthBar3, game.camera.viewportWidth/2 - healthBar1.getWidth()/2, 350);

		// Draw Score
		game.font.getData().setScale(0.3f); // Set font size
		game.font.draw(game.batch, "Score: " + Integer.toString((int) (score)), 540, 350, 100, Align.center, false);
		
		// Draw Performance Text
		game.font.getData().setScale(0.5f); // Set font size
		game.font.draw(game.batch, performance, game.camera.viewportWidth/2 - 50, game.camera.viewportHeight/2 + 50, 100, Align.center, false);
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
	public void dispose() {
		
	}

}
