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
	
	public GameOverScreen(HeslingtonHustle game, float score, String grade, boolean newHighScore) {
		this.game = game;
		this.score = score;
		this.grade = grade;
		this.newHighScore = newHighScore;
	}
	
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
				backToMenu();
			}
		}
		game.font.draw(game.batch, game.layout, 250, 40);
		
		
		if(newHighScore) {
			game.font.getData().setScale(1.0f); // Set font size
			game.font.setColor(new Color(222/255f, 158/255f, 65/255f, 1));
			game.font.draw(game.batch, "New high score!", 250, 120, 100, Align.center, false);
		}
				
				
		game.batch.end();
	}
	
	private void backToMenu() {	
		// Return to main menu
		game.setScreen(new StartScreen(game));
	}
	
	@Override
	public void show() {
		
	}

	@Override
	public void resize(int width, int height) {
		
	}

	@Override
	public void pause() {
		
	}

	@Override
	public void resume() {
		
	}

	@Override
	public void hide() {
		
	}

	@Override
	public void dispose() {
			
	}

}
