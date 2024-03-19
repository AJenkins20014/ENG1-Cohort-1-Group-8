/**
 * The MinigameScreen class serves as the parent class for all minigame screens.
 * It implements the Screen interface.
 */
package com.heslington_hustle.screens;

import com.badlogic.gdx.Screen;
import com.heslington_hustle.game.HeslingtonHustle;

public class MinigameScreen implements Screen {

	public HeslingtonHustle game;
	public float energyChange;
	public float difficultyScalar;
	public boolean exam;
	
	/**
	 * Constructs a new MinigameScreen object with the specified game instance and difficulty scalar.
	 * @param game The HeslingtonHustle game instance.
	 * @param difficultyScalar The scalar value representing the difficulty level of the minigame.
	 */
	public MinigameScreen(HeslingtonHustle game, float difficultyScalar){
		this.game = game;
		this.difficultyScalar = difficultyScalar;
		this.energyChange = 100; // Default - should be modified in each individual minigame
	}
	
	/**
	 * Starts the minigame. This method should be overridden in specific minigame classes.
	 */
	public void startGame() {

	}
	
	/**
     * Renders the minigame screen.
     * @param delta The time elapsed since the last frame.
     */
	@Override
	public void render(float delta) {
		
	}
	
	/**
     * Called when this screen becomes displayed.
     */
	@Override
	public void show() {
		
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
		
	}

	/**
     * Called when the screen resources should be disposed.
     */
	@Override
	public void dispose() {
		
	}

}
