/**
 * Represents a screen displaying information or tutorials in the game. 
 * Implements the Screen interface.
 */
package com.heslington_hustle.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Align;
import com.heslington_hustle.game.HeslingtonHustle;

public class InformationScreen implements Screen{
	private HeslingtonHustle game;
	public String type;
	private Screen nextScreen;
	private int score;
	private float resourcesGained; // Study points / energy
	
	/**
     * Constructs an InformationScreen for tutorials and game over screens for minigames.
     * @param game The game instance.
     * @param type The type of information screen.
     * @param nextScreen The next screen to transition to after this one.
     */
	public InformationScreen(HeslingtonHustle game, String type, Screen nextScreen) {
		this.game = game;
		this.type = type;
		this.nextScreen = nextScreen;
	}
	
	/**
     * Constructs an InformationScreen for minigame results.
     * @param game The game instance.
     * @param type The type of information screen.
     * @param nextScreen The next screen to transition to after this one.
     * @param score The score achieved in a minigame.
     * @param resourcesGained The resources gained in a minigame.
     */
	public InformationScreen(HeslingtonHustle game, String type, Screen nextScreen, int score, float resourcesGained) {
		this.game = game;
		this.type = type;
		this.nextScreen = nextScreen;
		this.score = score;
		this.resourcesGained = resourcesGained;
	}

	/**
     * Renders the information screen.
     * @param delta The time elapsed since the last frame.
     */
	@Override
	public void render(float delta) {
		// Clear the screen
		Gdx.gl.glClearColor(32/255f, 46/255f, 55/255f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
					
		// Set projection matrix of the batch to the camera
		game.batch.setProjectionMatrix(game.camera.combined);
		game.camera.update();
						
		// Get mouse position in world coordinates
		Vector3 mousePos = game.camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 1f));
				
		
		// Begin drawing on screen
		game.batch.begin();
		
		if(type == "mainTutorial") {
			loadMainTutorial();
		}
		else if(type == "bugFixerTutorial") {
			loadBugFixerTutorial();
		}
		else if(type == "studyGameScore") {
			loadStudyGameScore();
		}
		else if(type == "recreationGameScore") {
			loadRecreationGameScore();
		}
		else if(type == "bookStackerTutorial") {
			loadBookStackerTutorial();
		}
		else if(type == "colourMatchTutorial") {
			loadColourMatchTutorial();
		}
		else if(type == "drunkDancerTutorial") {
			loadDrunkDancerTutorial();
		}
		else if(type == "swiftSwimmerTutorial") {
			loadSwiftSwimmerTutorial();
		}
		else if(type == "squashTutorial") {
			loadSquashTutorial();
		}
		
		// Continue button
		game.font.getData().setScale(0.6f); // Set font size
		game.font.setColor(new Color(1, 1, 1, 1));	
		game.layout.setText(game.font, "Continue!", new Color(1, 1, 1, 1), 100, Align.center, false);
		if(mousePos.x < 250 + game.layout.width && mousePos.x > 250 - game.layout.width && mousePos.y < 40 + game.layout.height && mousePos.y > 40 - game.layout.height) {
			game.layout.setText(game.font, "Continue!", new Color(232/255f, 193/255f, 112/255f, 1), 100, Align.center, false);
			if(Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
				// Button clicked
				game.menuClick.play(game.volume);
				game.setScreen(nextScreen);
			}
		}
		game.font.draw(game.batch, game.layout, 250, 40);
		
		// Stop drawing on screen
		game.batch.end();
	}
	
	/**
     * Loads the main tutorial screen.
     */
	public void loadMainTutorial() {
		// Display the tutorial for the game in general
		game.font.getData().setScale(0.4f);
		game.font.setColor(new Color(1, 1, 1, 1));	
		game.font.draw(game.batch, "You wake up after a long night out. You glance at your phone and realise the date - only 1 week before exams start! "
				+ "You really shouldn't have left it so late again! Oh well, late is better than never, you rush out of the door with determination, time to get to work!", 50, 300, 500, Align.center, true);
		
		game.font.getData().setScale(0.5f);
		game.batch.draw(new Texture("UI/TutorialMove.png"), 60, 90);
		game.font.draw(game.batch, "Move", 150, 110, 100, Align.left, false);
		
		game.batch.draw(new Texture("UI/TutorialInteract.png"), 350, 105);
		game.font.draw(game.batch, "Interact", 390, 110, 100, Align.left, false);
	}
	
	/**
     * Loads the BugFixer minigame tutorial screen.
     */
	public void loadBugFixerTutorial() {
		game.font.getData().setScale(1.2f); // Set font size
		game.font.setColor(new Color(222/255f, 158/255f, 65/255f, 1));
		game.font.draw(game.batch, "BugFixer", 250, 330, 100, Align.center, false);
		game.font.getData().setScale(0.4f);
		game.font.setColor(new Color(1, 1, 1, 1));	
		game.font.draw(game.batch, "Blast the bugs and survive to get points!", 50, 260, 500, Align.center, true);
		
		game.font.getData().setScale(0.5f);
		game.batch.draw(new Texture("UI/TutorialMove.png"), 60, 90);
		game.font.draw(game.batch, "Move", 150, 110, 100, Align.left, false);
		
		game.batch.draw(new Texture("UI/TutorialLeftClick.png"), 380, 90);
		game.font.draw(game.batch, "Shoot", 420, 110, 100, Align.left, false);
	}
	
	/**
     * Loads the BookStacker minigame tutorial screen.
     */
	public void loadBookStackerTutorial() {
		game.font.getData().setScale(1.2f); // Set font size
		game.font.setColor(new Color(222/255f, 158/255f, 65/255f, 1));
		game.font.draw(game.batch, "BookStacker", 250, 330, 100, Align.center, false);
		game.font.getData().setScale(0.4f);
		game.font.setColor(new Color(1, 1, 1, 1));	
		game.font.draw(game.batch, "Stack books without dropping to get points!", 50, 260, 500, Align.center, true);
		
		game.font.getData().setScale(0.5f);
		game.batch.draw(new Texture("UI/TutorialLeftClick.png"), 250, 90);
		game.font.draw(game.batch, "Drop", 300, 110, 100, Align.left, false);
	}
	
	/**
     * Loads the ColourMatch minigame tutorial screen.
     */
	public void loadColourMatchTutorial() {
		game.font.getData().setScale(1.2f); // Set font size
		game.font.setColor(new Color(222/255f, 158/255f, 65/255f, 1));
		game.font.draw(game.batch, "ColourMatch", 250, 330, 100, Align.center, false);
		game.font.getData().setScale(0.4f);
		game.font.setColor(new Color(1, 1, 1, 1));	
		game.font.draw(game.batch, "Watch and then click colours in the right order", 50, 260, 500, Align.center, true);
		
		game.font.getData().setScale(0.5f);
		game.batch.draw(new Texture("UI/TutorialLeftClick.png"), 250, 90);
		game.font.draw(game.batch, "Click", 300, 110, 100, Align.left, false);
	}
	
	/**
     * Loads the SwiftSwimmer minigame tutorial screen.
     */
	public void loadSwiftSwimmerTutorial() {
		game.font.getData().setScale(1.2f); // Set font size
		game.font.setColor(new Color(222/255f, 158/255f, 65/255f, 1));
		game.font.draw(game.batch, "SwiftSwimmer", 250, 330, 100, Align.center, false);
		game.font.getData().setScale(0.4f);
		game.font.setColor(new Color(1, 1, 1, 1));	
		game.font.draw(game.batch, "Swim as fast as possible", 50, 260, 500, Align.center, true);
		
		game.font.getData().setScale(0.5f);
		game.batch.draw(new Texture("UI/TutorialLeftClick.png"), 60, 90);
		game.font.draw(game.batch, "Click as fast as possible to swim", 150, 110, 100, Align.left, false);
	}
	
	/**
     * Loads the DrunkDancer minigame tutorial screen.
     */
	public void loadDrunkDancerTutorial() {
		game.font.getData().setScale(1.2f); // Set font size
		game.font.setColor(new Color(222/255f, 158/255f, 65/255f, 1));
		game.font.draw(game.batch, "DrunkDancer", 250, 330, 100, Align.center, false);
		game.font.getData().setScale(0.4f);
		game.font.setColor(new Color(1, 1, 1, 1));	
		game.font.draw(game.batch, "Dance in time with the arrows!", 50, 260, 500, Align.center, true);
		
		game.font.getData().setScale(0.5f);
		game.batch.draw(new Texture("UI/TutorialMove.png"), 160, 90);
		game.font.draw(game.batch, "or", 200, 110, 100, Align.center, false);
		game.batch.draw(new Texture("UI/TutorialArrowKeys.png"), 290, 90);
		game.font.draw(game.batch, "Dance", 370, 110, 100, Align.left, false);
	}
	
	/**
     * Loads the Squash minigame tutorial screen.
     */
	public void loadSquashTutorial() {
		game.font.getData().setScale(1.2f); // Set font size
		game.font.setColor(new Color(222/255f, 158/255f, 65/255f, 1));
		game.font.draw(game.batch, "Squash", 250, 330, 100, Align.center, false);
		game.font.getData().setScale(0.4f);
		game.font.setColor(new Color(1, 1, 1, 1));	
		game.font.draw(game.batch, "Hit the ball with the paddle!", 50, 260, 500, Align.center, true);
		
		game.font.getData().setScale(0.5f);
		game.batch.draw(new Texture("UI/TutorialUpDown.png"), 230, 90);
		game.font.draw(game.batch, "Move", 300, 110, 100, Align.left, false);
	}
	
	/**
     * Loads the game over screen for study minigames.
     */
	public void loadStudyGameScore() {
		game.font.getData().setScale(1.2f); // Set font size
		game.font.setColor(new Color(222/255f, 158/255f, 65/255f, 1));
		game.font.draw(game.batch, "Game Over!", 250, 330, 100, Align.center, false);
		game.font.getData().setScale(0.4f);
		game.font.setColor(new Color(1, 1, 1, 1));	
		game.font.draw(game.batch, "Final score: " + Integer.toString(score), 250, 260, 100, Align.center, false);
		
		if(resourcesGained < 30f) {
			game.font.draw(game.batch, "You don't feel very productive...", 250, 200, 100, Align.center, false);
		}
		else if(resourcesGained < 50f) {
			game.font.draw(game.batch, "You feel slightly productive", 250, 200, 100, Align.center, false);
		}
		else if(resourcesGained < 70f) {
			game.font.draw(game.batch, "You feel productive", 250, 200, 100, Align.center, false);
		}
		else if(resourcesGained < 90f) {
			game.font.draw(game.batch, "You feel very productive", 250, 200, 100, Align.center, false);
		}
		else {
			game.font.draw(game.batch, "You feel extremely productive!", 250, 200, 100, Align.center, false);
		}
				
		if(game.timesStudied[game.day-1] > 2) {
			game.font.draw(game.batch, "You start to feel stressed...", 250, 150, 100, Align.center, false);
		}
		else if(game.timesStudied[game.day-1] > 3) {
			game.font.draw(game.batch, "You feel very stressed...", 250, 150, 100, Align.center, false);
		}
	}
	
	/**
     * Loads the game over screen for recreation minigames.
     */
	public void loadRecreationGameScore() {
		game.font.getData().setScale(1.2f); // Set font size
		game.font.setColor(new Color(222/255f, 158/255f, 65/255f, 1));
		game.font.draw(game.batch, "Game Over!", 250, 330, 100, Align.center, false);
		game.font.getData().setScale(0.4f);
		game.font.setColor(new Color(1, 1, 1, 1));	
		game.font.draw(game.batch, "Final score: " + Integer.toString(score), 250, 260, 100, Align.center, false);

		if(resourcesGained < 30f) {
			game.font.draw(game.batch, "You don't feel very well rested...", 250, 200, 100, Align.center, false);
		}
		else if(resourcesGained < 40f) {
			game.font.draw(game.batch, "You feel slightly well rested", 250, 200, 100, Align.center, false);
		}
		else if(resourcesGained < 50f) {
			game.font.draw(game.batch, "You feel well rested", 250, 200, 100, Align.center, false);
		}
		else if(resourcesGained < 60f) {
			game.font.draw(game.batch, "You feel very well rested", 250, 200, 100, Align.center, false);
		}
		else {
			game.font.draw(game.batch, "You feel extremely well rested!", 250, 200, 100, Align.center, false);
		}
	}

	/**
     * Called when the screen size changes.
     * @param width The new width.
     * @param height The new height.
     */
	@Override
	public void resize(int width, int height) {
		
	}
	
	/**
     * Called when this screen becomes displayed.
     */
	@Override
	public void show() {
		game.menuMusic.play();
	}

	/**
     * Called when the game is paused.
     */
	@Override
	public void pause() {
		
	}

	/**
     * Called when the game is resumed from a paused state.
     */
	@Override
	public void resume() {
		
	}

	/**
     * Called when this screen stops being displayed.
     */
	@Override
	public void hide() {
		game.menuMusic.pause();
	}

	/**
     * Called when the screen resources should be disposed.
     */
	@Override
	public void dispose() {
		
	}

}
