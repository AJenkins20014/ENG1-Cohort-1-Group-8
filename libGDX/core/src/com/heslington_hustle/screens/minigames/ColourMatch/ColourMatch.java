package com.heslington_hustle.screens.minigames.ColourMatch;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.ai.btree.decorator.Random;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.heslington_hustle.game.HeslingtonHustle;
import com.heslington_hustle.game.PopUpText;
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
	public int sequenceLength = 0;
	public ColourBlock redBlock;
	public ColourBlock blueBlock;
	public ColourBlock yellowBlock;
	public ColourBlock greenBlock;
	public ColourBlock sequenceBlock;
	public ColourMatch(HeslingtonHustle game, float difficultyScalar) {
		super(game, difficultyScalar);
		this.studyPointsGained = 15f; // From worst possible performance
		this.maxStudyPointsGained = 100f; // From best possible performance
	}
	
	@Override
	public void startGame() {
		background = new Texture("ColourMatchMinigame/Background.png");
		sequencePressed = 0;
		sequence = new Array<Colour>();
		toDisplaySequence = true;
		sequenceBlock = null;
		blockShown = false;
		pointer = 0;
		clock = 0;
		// Code to restart the game
		studyPointsGained = 15f;
		
		// If in borderless, set to fullscreen to pause game if unfocused
		if(game.isBorderless) {
			Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
		}
		
		// Display tutorial - TODO: create a tutorial in InformationScreen and rename the string in the constructor below to fit
	    game.setScreen(new InformationScreen(game, "colourMatchTutorial", this));
	    redBlock = new ColourBlock(game, Colour.RED,120,50);
	    blueBlock = new ColourBlock(game, Colour.BLUE,220,50);
	    greenBlock = new ColourBlock(game, Colour.GREEN,120,150);
	    yellowBlock = new ColourBlock(game, Colour.YELLOW,220,150);
	    addToSequence();
	}
	
	private void endGame() {
		// Calculate final score
		//studyPointsGained += score;
		
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
		renderBackground();
		renderBlocks();
		// Get mouse position in world coordinates
		Vector3 mousePos = game.camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 1f));
	    this.mousePos = mousePos;
	    
	    //Display Sequence
	    displaySequence();
	    readPlayerInputSequence();
	    if(sequencePressed  == sequence.size) {
	    	toDisplaySequence = true;
	    	sequencePressed = 0;
	    	pointer = 0;
	    	score+= 30;
	    	addToSequence();
	    }
	    
		game.batch.begin();
		
		
		// CODE TO RENDER SPRITES GOES HERE
		
		
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
	
	private void renderBackground() {
		// TODO Auto-generated method stub
		game.batch.begin();
		game.batch.draw(background,0,0);
		game.batch.end();
	}

	public void addToSequence() {
		sequence.add(generateColour()) ;
		
	}
	
	//Gets a Colour for the sequence
	public Colour generateColour() {
		int random = MathUtils.random.nextInt(4);
		        return colourList[random];
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

	public void renderBlocks() {
		redBlock.drawSprite();
		blueBlock.drawSprite();
		yellowBlock.drawSprite();
		greenBlock.drawSprite();
		if(sequenceBlock != null) {
			sequenceBlock.drawSprite();
		}
	}
	@Override
	public void dispose() {
		
	}
	public void displaySequence() {
		if(clock > (0.5-(0.01*sequence.size)) && toDisplaySequence == true) {
	    	if(pointer > sequence.size-1) {
	    		System.out.println("Hi");
	    		toDisplaySequence = false;
	    		pointer = 0;
	    	}
	    	else if(blockShown == true) {
	    		sequenceBlock.kill();
	    		sequenceBlock = null;
	    		pointer +=1;
	    		blockShown = false;
	    	}
	    	else {
	    		sequenceBlock = new ColourBlock(game,sequence.get(pointer),420,100);
	    		blockShown = true;
	    		
	    	}
	    	clock = 0;
	    	
	    }
	}
	public void readPlayerInputSequence() {
		if(Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
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
}
