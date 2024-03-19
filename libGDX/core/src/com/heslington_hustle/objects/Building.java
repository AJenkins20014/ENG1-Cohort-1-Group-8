/**
 * The Building class represents a building object in the game.
 * This class extends the Object class and players
 * can interact with buildings to start minigames.
 */
package com.heslington_hustle.objects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Align;
import com.heslington_hustle.game.HeslingtonHustle;
import com.heslington_hustle.game.PopUpText;
import com.heslington_hustle.screens.MinigameScreen;

public class Building extends Object {

	public MinigameScreen minigame;
	public float requiredEnergy;
	public int timeSpent;
	
	/**
     * Constructs a new Building object with the specified parameters.
     * @param game The game instance.
     * @param name The name of the building.
     * @param interactRegions The interaction regions of the building.
     * @param screen The screen where the building is located.
     * @param tooltip The tooltip text for the building.
     * @param tooltipX The x-coordinate of the tooltip.
     * @param tooltipY The y-coordinate of the tooltip.
     * @param nameX The x-coordinate of the building name.
     * @param nameY The y-coordinate of the building name.
     * @param minigameScreen The minigame screen associated with the building.
     * @param requiredEnergy The energy required to interact with the building.
     * @param timeSpent The time spent by the player when interacting with the building.
     */
	public Building(HeslingtonHustle game, String name, Rectangle[] interactRegions, int screen, String tooltip, int tooltipX, int tooltipY, int nameX, int nameY, MinigameScreen minigameScreen, Float requiredEnergy, int timeSpent) {
		super(game, name, interactRegions, screen, tooltip, tooltipX, tooltipY, nameX, nameY);
		this.minigame = minigameScreen;
		this.requiredEnergy = requiredEnergy;
		this.timeSpent = timeSpent;
	}
	
	/**
     * Allows the player to interact with the building.
     * Checks if the player has enough energy and time to start the minigame.
     * If conditions are met, subtracts energy, adds time, and starts the minigame.
     */
	public void interact() {
		// Check if player has enough time
		if (game.time + timeSpent > 24){
			game.addPopUpText(new PopUpText("It's too late to do this now!", 250, 300, 100, Align.center, false, 0.4f, new Color(1,1,1,1)), 2);
		}
		// Check if player has enough energy
		else if(game.energyBar.energy < requiredEnergy) {
			game.addPopUpText(new PopUpText("Insufficient Energy!", 250, 300, 100, Align.center, false, 0.4f, new Color(1,1,1,1)), 2);
		}
		// Subtract energy, add time and start minigame
		else {
			game.energyBar.addEnergy(-requiredEnergy);
			game.time += timeSpent;
			
			// Set minigame difficulty
			minigame.difficultyScalar = game.timesStudied[game.day-1]*0.25f + 1f;
			minigame.exam = false;
			minigame.startGame();
		}
	}
}