package com.heslington_hustle.screens.minigames.BookStacker;



import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Align;
import com.heslington_hustle.game.HeslingtonHustle;
import com.heslington_hustle.game.PopUpText;
import com.heslington_hustle.screens.InformationScreen;
import com.heslington_hustle.screens.MinigameScreen;
import com.heslington_hustle.screens.minigames.BookStacker.BookSegment.Position;
import com.heslington_hustle.screens.minigames.BookStacker.BookSegment.State;
import com.heslington_hustle.screens.minigames.BugFixer.ScatterBug;
import com.heslington_hustle.screens.minigames.BugFixer.SniperBug;

public class BookStacker extends MinigameScreen implements Screen {
	private boolean minimised;
	private float studyPointsGained;
	private float maxStudyPointsGained;
	public static float fallSpeed;
	private int score;
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

	public BookStacker(HeslingtonHustle game, float difficultyScalar) {
		super(game, difficultyScalar);
		this.studyPointsGained = 15f; // From worst possible performance
		this.maxStudyPointsGained = 100f; // From best possible performance
	
	}
	
	@Override
	public void startGame() {
		// Code to restart the game
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
		
		// Display tutorial - TODO: create a tutorial in InformationScreen and rename the string in the constructor below to fit
	    game.setScreen(new InformationScreen(game, "bookStackerTutorial", this));
	   
		spawnBook(0,5,State.STATIONARY); //  Bottom Platform
		spawnBook(8,0,State.MOVING);
		// Temporary - TODO: REMOVE
		//endGame();
	}
	
	private void endGame() {
		// Calculate final score
		studyPointsGained += score;
		
		/*
		// Check minigame high score
		if(game.prefs.getInteger("thisMinigameHighScore", 0) < score) {
			game.prefs.putInteger("thisMinigameHighScore", score);
			game.prefs.flush();
		}
		*/
		
		if(studyPointsGained > maxStudyPointsGained) {
			studyPointsGained = maxStudyPointsGained;
		}
		
		// Reset window back to borderless
		if(game.isBorderless) {
			Gdx.graphics.setUndecorated(true);
			Gdx.graphics.setWindowedMode(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		}
		
		// Add studyPoints score to total score for this minigame
		if(game.studyPoints.containsKey("thisMinigame")) {
			game.studyPoints.put("thisMinigame", (game.studyPoints.get("thisMinigame") + studyPointsGained));
		}
		else {
			game.studyPoints.put("thisMinigame", studyPointsGained);
		}
		
		// Update amount of times studied today
		game.timesStudied[game.day-1] += 1;
		
		
		// Display final score
		game.setScreen(new InformationScreen(game, "studyGameScore", game.map, score, studyPointsGained));
	}
	
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
			BookSegment segment= new BookSegment(game,i,j,position,state);
			bookGrid[i][j] = segment;
		}
	}
	@Override
	public void render(float delta) {
		//If all blocks miss tower end game
		if(currentLength == 0) {
			endGame();
		}
		//increment clock
		clock += Gdx.graphics.getDeltaTime();
		//Increases tower height and speed after row placed
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
		
		// Get mouse position in world coordinates
		Vector3 mousePos = game.camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 1f));
		//Counts all blocks have fallen and spawns in new segments
		
		if(BookSegment.counter == currentLength) {
			spawnBook(8,0,State.MOVING);
			BookSegment.counter = 0;
			wipe();
		}
		
		//Render Books
			for (int i = 0; i < rows; i++) {
				for (int j = 0; j < columns; j++) {
					if (bookGrid[i][j] instanceof BookSegment) {
						((BookSegment) bookGrid[i][j]).update();
				    }
				 }
			}
					
		game.batch.begin();
		
		

		
		
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
	}
	
	//Used to remove segments that are unusable
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
	public void removeCenter(){
		//Stops new blocks from spawning
		blockDrop = true;
		for (int i = 4; i >= 1; i--) {
			for (int j = 0; j < columns; j++) {
				if (bookGrid[i][j] instanceof BookSegment) {
					System.out.println(i);
					((BookSegment) bookGrid[i][j]).kill();
					
			    }
			 }
		
		}
		currentHeight = 1; //0 INDEXED
		for (int j = 0; j < columns; j++) {
			if (bookGrid[4][j] instanceof BookSegment) {
				((BookSegment) bookGrid[4][j]).fall();
				
		    }
		 }
		blockDrop = false;
	}

	@Override
	public void dispose() {
		
	}
	

}

