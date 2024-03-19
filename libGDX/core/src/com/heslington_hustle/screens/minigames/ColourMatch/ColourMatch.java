/**
 * Represents the Colour Match minigame screen.
 * This minigame requires players to memorize and replicate sequences of colored blocks.
 * It extends the MinigameScreen class and implements the Screen interface.
 */
package com.heslington_hustle.screens.minigames.ColourMatch;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.heslington_hustle.game.HeslingtonHustle;
import com.heslington_hustle.screens.InformationScreen;
import com.heslington_hustle.screens.MinigameScreen;

public class ColourMatch extends MinigameScreen implements Screen {
	public Texture background;
	public int sequencePressed;
	public boolean clicked;
	Vector3 mousePos;
	private boolean minimised;
	private float studyPointsGained;
	private float maxStudyPointsGained;
	private int score;
	public int pointer;
	public float clock;
	public Boolean toDisplaySequence;
	public Boolean blockShown;
	public Array<Colour> sequence;
	public enum Colour{
		RED,
		BLUE,
		YELLOW,
		GREEN
	}
	public Colour[] colourList = {Colour.RED,Colour.BLUE,Colour.GREEN,Colour.YELLOW};
	public ColourBlock redBlock;
	public ColourBlock blueBlock;
	public ColourBlock yellowBlock;
	public ColourBlock greenBlock;
	public ColourBlock sequenceBlock;
	
	private Music backgroundMusic;
	
	/**
     * Constructs a new ColourMatch object with the specified game instance and difficulty scalar.
     * @param game The HeslingtonHustle game instance.
     * @param difficultyScalar The scalar value representing the difficulty level of the minigame.
     */
	public ColourMatch(HeslingtonHustle game, float difficultyScalar) {
		super(game, difficultyScalar);
		this.studyPointsGained = 15f; // From worst possible performance
		this.maxStudyPointsGained = 100f; // From best possible performance
		
		// Load sounds
		backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("Music/ColourMatchMusic.ogg"));
		backgroundMusic.setLooping(true);
	}
	
	/**
     * Starts the Colour Match minigame.
     */
	@Override
	public void startGame() {
		// Code to restart the game
		score = 0;
		studyPointsGained = 15f;
		background = new Texture("ColourMatchMinigame/Background.png");
		sequencePressed = 0;
		sequence = new Array<Colour>();
		toDisplaySequence = true;
		sequenceBlock = null;
		blockShown = false;
		pointer = 0;
		clock = 0;
		
		// If in borderless, set to fullscreen to pause game if unfocused
		if(game.isBorderless) {
			Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
		}
		
		// Display tutorial
	    game.setScreen(new InformationScreen(game, "colourMatchTutorial", this));
	    //Creates four buttons and then begins the sequence for player to copy
	    redBlock = new ColourBlock(game, Colour.RED,120,50);
	    blueBlock = new ColourBlock(game, Colour.BLUE,220,50);
	    greenBlock = new ColourBlock(game, Colour.GREEN,120,150);
	    yellowBlock = new ColourBlock(game, Colour.YELLOW,220,150);
	    for(int i =0;i== Math.ceil(difficultyScalar*4);i++) {
	    	addToSequence();
	    }
	}
	
	/**
     * Ends the Colour Match minigame.
     */
	private void endGame() {
		// Calculate final score
		studyPointsGained += score/4;
		
		 //Check minigame high score
		if(!exam) {
			if(game.prefs.getInteger("colourMatchHighScore", 0) < score) {
				game.prefs.putInteger("colourMatchHighScore", score);
				game.prefs.flush();
			}
		}
	
		if(studyPointsGained > maxStudyPointsGained) {
			studyPointsGained = maxStudyPointsGained;
		}
		
		System.out.print("Colour Match study points gained: " + studyPointsGained + "\n");
		
		// Reset window back to borderless
		if(game.isBorderless) {
			Gdx.graphics.setUndecorated(true);
			Gdx.graphics.setWindowedMode(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		}
		
		if(exam) {
			System.out.print("Colour Match difficulty Scalar: " + difficultyScalar + "\n");
			game.exam.score += studyPointsGained/3;
			System.out.print("Exam score after Colour Match: " + game.exam.score + "\n");
			game.exam.loadNextMinigame();
			return;
		}
		
		// Add studyPoints score to total score for this minigame
		if(game.studyPoints.containsKey("ColourMatch")) {
			game.studyPoints.put("ColourMatch", (game.studyPoints.get("ColourMatch") + studyPointsGained));
		}
		else {
			game.studyPoints.put("ColourMatch", studyPointsGained);
		}
		
		// Update amount of times studied today
		game.timesStudied[game.day-1] += 1;
		
		
		// Display final score
		game.setScreen(new InformationScreen(game, "studyGameScore", game.map, score, studyPointsGained));
	}
	
	/**
     * Renders the Colour Match minigame screen.
     * @param delta The time elapsed since the last frame.
     */
	@Override
	public void render(float delta) {
		clock += Gdx.graphics.getDeltaTime();
		if(minimised) return;
		
		// Clear the screen
		Gdx.gl.glClearColor(16/255f, 20/255f, 31/255f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		// Set projection matrix of the batch to the camera
		game.batch.setProjectionMatrix(game.camera.combined);
		game.camera.update();
		//Render sprites
		renderBackground();
		renderBlocks();
		
		// Get mouse position in world coordinates
		Vector3 mousePos = game.camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 1f));
	    this.mousePos = mousePos;
	    //If sequence is completed, reset variables increase score and add to sequence
	    if(sequencePressed  == sequence.size) {
	    	toDisplaySequence = true;
	    	sequencePressed = 0;
	    	pointer = 0;
	    	score+= 30;
	    	addToSequence();
	    }
	    
		game.batch.begin();
		
		//Display current score
		game.font.getData().setScale(0.3f); // Set font size
		game.font.draw(game.batch, "Score: " + Integer.toString((score)), 400, 350, 100, Align.center, false);


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
		
		//Display Sequence
	    displaySequence();
	    //If sequence is not being displayed, read player inputs STOPS PLAYER COPYING SEQUENCE AS SHOWS
	    if(!toDisplaySequence) {
	    	readPlayerInputSequence();
	    }
	}
	
	/**
	 * Used to draw the background on screen
	 */
	private void renderBackground() {
		game.batch.begin();
		game.batch.draw(background,0,0);
		game.batch.end();
	}

	/**
	 *Used to add a colour to the sequence
	 */
	public void addToSequence() {
		sequence.add(generateColour()) ;
	}
	
	/**
	 * Generates a random colour to be added to the sequence to guess
	 */
	public Colour generateColour() {
		int random = MathUtils.random.nextInt(4);
		        return colourList[random];
	}

	/**
     * Called when the screen size changes.
     * @param width The new width.
     * @param height The new height.
     */
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

	/**
	 * Draws all blocks on screen including sequence block if exists
	 */
	public void renderBlocks() {
		redBlock.drawSprite();
		blueBlock.drawSprite();
		yellowBlock.drawSprite();
		greenBlock.drawSprite();
		if(sequenceBlock != null) {
			sequenceBlock.drawSprite();
		}
	}

	/**
	 * Used to display the sequence to  be guessed
	 */
	public void displaySequence() {
		if(clock > (0.5-(0.025*sequence.size)) && toDisplaySequence == true) { //If sequence is playing and enough time has passed between blocks shown
	    	if(pointer > sequence.size-1) { //Code to end showing sequence
	    		toDisplaySequence = false;
	    		pointer = 0;
	    	}
	    	else if(blockShown == true) { // Code to get rid of block being shown and signal to show next
	    		sequenceBlock.kill();
	    		sequenceBlock = null;
	    		pointer +=1;
	    		blockShown = false;
	    	}
	    	else { // Creates block to be shown;
	    		sequenceBlock = new ColourBlock(game,sequence.get(pointer),420,100);
	    		blockShown = true;
	    		
	    	}
	    	clock = 0;
	    	
	    }
	}
	
	/**
	 * Checks if the correct colour is pressed, and ends the game if not, or progresses game if it is
	 */
	public void readPlayerInputSequence() {
		if(Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
			game.menuClick.play(game.volume);
			 switch(sequence.get(sequencePressed)) {
				case RED:this.clicked = redBlock.inBounds(mousePos.x,mousePos.y);break;
				case GREEN:this.clicked = greenBlock.inBounds(mousePos.x,mousePos.y);break;
				case BLUE:this.clicked = blueBlock.inBounds(mousePos.x,mousePos.y);break;
				case YELLOW:this.clicked = yellowBlock.inBounds(mousePos.x,mousePos.y);break;
			}
			 if(clicked == false) {
				 endGame();
			 }
			 else {
				 sequencePressed+=1;
			 }
			
		}
	}
	
	/**
     * Called when this screen stops being displayed.
     */
	@Override
	public void hide() {
		// Stop music
		backgroundMusic.stop();
	}
	
	/**
     * Called when this screen becomes displayed.
     */
	@Override
	public void show() {
		// Play music
		backgroundMusic.play();
	}
	
	/**
     * Updates the volume of the background music based on the game's volume setting.
     */
	private void updateMusicVolume() {
		float musicVolume = game.volume/2;
		backgroundMusic.setVolume(musicVolume);
	}
}
