/**
 * Represents the Book Stacker minigame screen.
 * This minigame requires players to stack falling books to build a tower as high as possible.
 * It extends the MinigameScreen class and implements the Screen interface.
 */
package com.heslington_hustle.screens.minigames.BookStacker;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Align;
import com.heslington_hustle.game.HeslingtonHustle;
import com.heslington_hustle.screens.InformationScreen;
import com.heslington_hustle.screens.MinigameScreen;
import com.heslington_hustle.screens.minigames.BookStacker.BookSegment.Position;
import com.heslington_hustle.screens.minigames.BookStacker.BookSegment.State;

public class BookStacker extends MinigameScreen implements Screen {
	public Texture background;
	private boolean minimised;
	private float studyPointsGained;
	private float maxStudyPointsGained;
	public static float fallSpeed;
	private int score;
	static float diff;
	public static Boolean changeHeightandSpeed = false;
	public static Boolean blockDrop;
	public static int currentLength; //Length of book
	public static int currentHeight = 0;
	public BookSegment.Position position;
	public BookSegment.State state;
	enum Direction{
		RIGHT,
		LEFT,
	}
	public static Direction direction = Direction.RIGHT;
	public static float clock;
	// Book grid dimensions
	public int rows = 9;
	public int columns = 16;
	// A grid for where books can spawns
	public static java.lang.Object[][] bookGrid;
	
	private Music backgroundMusic;
	public static Sound bookStack;

	/**
     * Constructs a new BookStacker instance.
     * @param game The HeslingtonHustle game instance.
     * @param difficultyScalar The scalar value to adjust the difficulty of the minigame.
     */
	public BookStacker(HeslingtonHustle game, float difficultyScalar) {
		super(game, difficultyScalar);
		 diff = difficultyScalar;
		this.studyPointsGained = 15f; // From worst possible performance
		this.maxStudyPointsGained = 100f; // From best possible performance
		
		// Load sounds
		backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("Music/BookStackerMusic.ogg"));
		backgroundMusic.setLooping(true);
				
		bookStack = Gdx.audio.newSound(Gdx.files.internal("BookStackerMinigame/BookStack.mp3"));
	}
	
	/**
     * Starts the Book Stacker minigame.
     */
	@Override
	public void startGame() {
		background = new Texture("BookStackerMinigame/Background.png");
		// Code to restart the game
		score = 0;
		fallSpeed = 1;
		BookSegment.counter = 0;
		clock = 0;
		currentHeight = 0;
		currentLength = 5;
		bookGrid =  new java.lang.Object[rows][columns];
		studyPointsGained = 15f;
		blockDrop = false;
		
		// If in borderless, set to fullscreen to pause game if unfocused
		if(game.isBorderless) {
			Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
		}
		
		// Display tutorial
	    game.setScreen(new InformationScreen(game, "bookStackerTutorial", this));
	   
		spawnBook(0,5,State.STATIONARY); // spawns Bottom Platform
		spawnBook(8,0,State.MOVING); // spawns first row to drop
	}
	
	/**
     * Ends the Book Stacker minigame.
     */
	private void endGame() {
		// Calculate final score
		studyPointsGained += score/10;
		
		// Check minigame high score
		if(!exam) {
			if(game.prefs.getInteger("bookStackerHighScore", 0) < score) {
				game.prefs.putInteger("bookStackerHighScore", score);
				game.prefs.flush();
			}
		}
		
		if(studyPointsGained > maxStudyPointsGained) {
			studyPointsGained = maxStudyPointsGained;
		}
		
		System.out.print("BookStacker study points gained: " + studyPointsGained + "\n");
		
		// Reset window back to borderless
		if(game.isBorderless) {
			Gdx.graphics.setUndecorated(true);
			Gdx.graphics.setWindowedMode(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		}
		
		if(exam) {
			System.out.print("BookStacker difficulty Scalar: " + difficultyScalar + "\n");
			game.exam.score += studyPointsGained/5;
			System.out.print("Exam score after BookStacker: " + game.exam.score + "\n");
			game.exam.loadNextMinigame();
			return;
		}
		
		// Add studyPoints score to total score for this minigame
		if(game.studyPoints.containsKey("BookStacker")) {
			game.studyPoints.put("BookStacker", (game.studyPoints.get("BookStacker") + studyPointsGained));
		}
		else {
			game.studyPoints.put("BookStacker", studyPointsGained);
		}
		
		// Update amount of times studied today
		game.timesStudied[game.day-1] += 1;
		
		
		// Display final score
		game.setScreen(new InformationScreen(game, "studyGameScore", game.map, score, studyPointsGained));
	}
	
	/**
	 * Spawns in a row of books and assigns edges
	 * @param int i - y level of books
	 * @param int jPassed - starting x for books to spawn at
	 * @param State state - the state the book is in
	 */
	public void spawnBook(int i,int jPassed,BookSegment.State state) {
		for(int j = jPassed; j<currentLength+jPassed;j++) {
			if(j == jPassed) {
				position = Position.LEFT_END;
			}
			else if(j== currentLength+jPassed-1) {
				position = Position.RIGHT_END;
			}
			else {
				position = Position.MIDDLE;
			}
			//Creates segment and adds to grid
			BookSegment segment= new BookSegment(game,i,j,position,state);
			bookGrid[i][j] = segment;
		}
	}
	
	/**
     * Renders the Book Stacker minigame screen.
     * @param delta The time elapsed since the last frame.
     */
	@Override
	public void render(float delta) {
		//If all blocks miss tower end game
		if(currentLength == 0) {
			endGame();
		}
		//increment clock
		clock += Gdx.graphics.getDeltaTime();
		//Increases tower height and speed after row placed + Increments score
		if(changeHeightandSpeed) {
			currentHeight+=1;
			score += 10*currentLength;
			fallSpeed = fallSpeed - (fallSpeed/5);
			changeHeightandSpeed = false;
		}
		//Shortens tower to make game replayable
		if(currentHeight == 5){
			removeCenter();
		}
		if(minimised) return;
		
		// Clear the screen
		Gdx.gl.glClearColor(16/255f, 20/255f, 31/255f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		// Set projection matrix of the batch to the camera
		game.batch.setProjectionMatrix(game.camera.combined);
		game.camera.update();
		renderBackground();
		
		//Counts all blocks have fallen and spawns in new segments
		if(BookSegment.counter == currentLength) {
			spawnBook(8,0,State.MOVING);
			BookSegment.counter = 0;
			wipe();
		}

		game.batch.begin();
		
		//Display score
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
		
		//Render Books
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < columns; j++) {
				if (bookGrid[i][j] instanceof BookSegment) {
					((BookSegment) bookGrid[i][j]).update();
			    }
			 }
		}
	}
	
	/**
	 * Used to remove segments that are unusable
	 */
	public void wipe() {
		for (int i = currentHeight-1; i>=0;i--) {
			for (int j = 0; j <16 ; j++) {
				if (bookGrid[i][j] instanceof BookSegment) {
					if (!(bookGrid[i+1][j] instanceof BookSegment)) {
						((BookSegment) bookGrid[i][j]).kill();
					}
			    }
			 }
		}
		
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
	 * Removes center of tower for replayability
	 */
	public void removeCenter(){
		//Stops new blocks from spawning
		blockDrop = true;
		//Removes centre blocks
		for (int i = 4; i >= 1; i--) {
			for (int j = 0; j < columns; j++) {
				if (bookGrid[i][j] instanceof BookSegment) {
					((BookSegment) bookGrid[i][j]).kill();
					
			    }
			 }
		
		}
		currentHeight = 1; //0 INDEXED
		//Drops top row
		for (int j = 0; j < columns; j++) {
			if (bookGrid[4][j] instanceof BookSegment) {
				((BookSegment) bookGrid[4][j]).fall();
				
		    }
		 }
		blockDrop = false;
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
	
	/**
     * Renders the background of the Book Stacker minigame.
     */
	private void renderBackground() {
		game.batch.begin();
		game.batch.draw(background,0,0);
		game.batch.end();
	}

}

