/**
 * The Foodhall class represents a food hall object in the game.
 * This class extends the Object class and players
 * can interact with food halls to eat and replenish energy.
 */
package com.heslington_hustle.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;
import com.heslington_hustle.game.HeslingtonHustle;
import com.heslington_hustle.game.PopUpText;

public class Foodhall extends Object {
	public Float energyChange;
	public int timeSpent;
	private Sound eat;
	private boolean eating;
	
	/**
     * Constructs a new Foodhall object with the specified parameters.
     * @param game The game instance.
     * @param name The name of the food hall.
     * @param interactRegions The interaction regions of the food hall.
     * @param screen The screen where the food hall is located.
     * @param tooltip The tooltip text for the food hall.
     * @param tooltipX The x-coordinate of the tooltip.
     * @param tooltipY The y-coordinate of the tooltip.
     * @param nameX The x-coordinate of the food hall name.
     * @param nameY The y-coordinate of the food hall name.
     * @param energyChange The change in energy after eating.
     * @param timeSpent The time spent by the player when eating.
     */
	public Foodhall(HeslingtonHustle game, String name, Rectangle[] interactRegions, int screen, String tooltip, int tooltipX, int tooltipY, int nameX, int nameY, Float energyChange, int timeSpent) {
		super(game, name, interactRegions, screen, tooltip, tooltipX, tooltipY, nameX, nameY);
		this.energyChange = energyChange;
		this.timeSpent = timeSpent;
		// Load eating sound
		this.eat = Gdx.audio.newSound(Gdx.files.internal("Map/Eat.mp3"));
	}
	
	/**
     * Allows the player to eat at the food hall.
     * Checks if the player has enough time to eat.
     * If conditions are met, subtracts energy, adds time, and updates the player's status.
     */
	public void eat() {
		if(eating) return; // Avoid accidental eating
		
		// Check if player has enough time
		if (game.time + timeSpent > 24){
			game.addPopUpText(new PopUpText("It's too late to do this now!", 250, 300, 100, Align.center, false, 0.4f, new Color(1,1,1,1)), 2);
		}
		else {
			
			// Fade to black and play eating sound
			game.fadeToBlack();
			eat.play(game.volume);
			eating = true;
			
			// Wait 2 seconds for the fade to go away
			Timer.schedule(new Task() {
			    @Override
			    public void run() {
			    	// Add energy and time
			    	if(game.timesEatenToday == 0) {
						game.energyBar.addEnergy(energyChange);
						game.time += timeSpent;
						game.addPopUpText(new PopUpText("You feel well fed", 250, 300, 100, Align.center, false, 0.4f, new Color(1,1,1,1)), 2);
					}
					// If already eaten once today, gain a bit less energy
					else if(game.timesEatenToday == 1) {
						game.energyBar.addEnergy(energyChange-15f);
						game.time += timeSpent;
						game.addPopUpText(new PopUpText("You feel full", 250, 300, 100, Align.center, false, 0.4f, new Color(1,1,1,1)), 2);
					}
					// If already eaten twice today, gain a lot less energy
					else if(game.timesEatenToday == 2) {
						game.energyBar.addEnergy(energyChange-30f);
						game.time += timeSpent;
						game.addPopUpText(new PopUpText("You feel bloated...", 250, 300, 100, Align.center, false, 0.4f, new Color(1,1,1,1)), 2);
					}
					// If already eaten 3 of more times today, lose energy instead
					else {
						game.energyBar.addEnergy(-20f);
						game.time += timeSpent;
						game.addPopUpText(new PopUpText("You start to feel sick...", 250, 300, 100, Align.center, false, 0.4f, new Color(1,1,1,1)), 2);
					}
					game.timesEatenToday++;
					eating = false;
			    }
			}, 1f);
			
		}
	}

}
