/**
 * Represents the screen displayed when the game is over, showing the player's score, grade, and an option to return to the main menu.
 * Implements the Screen interface.
 */
package com.heslington_hustle.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Align;
import com.heslington_hustle.game.HeslingtonHustle;

public class GameOverScreen implements Screen {

	private HeslingtonHustle game;
	private float score;
	private String grade;
	private boolean newHighScore;
	
	/**
     * Constructs a GameOverScreen.
     * @param game The game instance.
     * @param score The player's score.
     * @param grade The grade achieved by the player.
     * @param newHighScore Indicates whether the player achieved a new high score.
     */
	public GameOverScreen(HeslingtonHustle game, float score, String grade, boolean newHighScore) {
		this.game = game;
		this.score = score;
		this.grade = grade;
		this.newHighScore = newHighScore;
	}
	
	/**
     * Renders the game over screen.
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

		// Display score and grade
		game.font.getData().setScale(1.2f); // Set font size
		game.font.setColor(new Color(222/255f, 158/255f, 65/255f, 1));
		game.font.draw(game.batch, "Results Day!", 250, 330, 100, Align.center, false);
		game.font.setColor(new Color(232/255f, 193/255f, 112/255f, 1));
		game.font.draw(game.batch, Integer.toString(Math.round(score)), 250, 260, 100, Align.center, false);
		game.font.draw(game.batch, grade, 250, 190, 100, Align.center, false);
				
		// Exit button
		game.font.getData().setScale(0.6f); // Set font size
		game.font.setColor(new Color(1, 1, 1, 1));	
		game.layout.setText(game.font, "Back to Main Menu", new Color(1, 1, 1, 1), 100, Align.center, false);
		if(mousePos.x < 250 + game.layout.width && mousePos.x > 250 - game.layout.width && mousePos.y < 40 + game.layout.height && mousePos.y > 40 - game.layout.height) {
			game.layout.setText(game.font, "Back to Main Menu", new Color(232/255f, 193/255f, 112/255f, 1), 100, Align.center, false);
			if(Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
				// Button clicked
				game.menuClick.play(game.volume);
				backToMenu();
			}
		}
		game.font.draw(game.batch, game.layout, 250, 40);
		
		
		if(newHighScore) {
			game.font.getData().setScale(1.0f); // Set font size
			game.font.setColor(new Color(222/255f, 158/255f, 65/255f, 1));
			game.font.draw(game.batch, "New high score!", 250, 120, 100, Align.center, false);
		}
				
		// Stop drawing on screen
		game.batch.end();
	}
	
	/**
     * Returns to the main menu.
     */
	private void backToMenu() {	
		game.setScreen(new StartScreen(game));
	}
	
	/**
     * Called when this screen becomes displayed.
     */
	@Override
	public void show() {
		game.menuMusic.play();
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
