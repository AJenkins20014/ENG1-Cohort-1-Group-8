/**
 * The EnergyBar class represents the energy bar of the player.
 * It manages the current energy level and provides methods to modify it.
 */
package com.heslington_hustle.game;

public class EnergyBar {
	public float energy; // Current energy
	
	/**
     * Constructs a new EnergyBar with the specified initial energy level.
     * @param energy The initial energy level of the energy bar.
     */
	public EnergyBar(float energy) {
		this.energy = energy;
	}
	
	/**
     * Sets the energy level to the specified value.
     * @param energy The new energy level to set.
     */
	public void setEnergy(float energy) {
		this.energy = energy;
	}
	
	/**
     * Adds the specified amount of energy to the current energy level.
     * If the resulting energy level exceeds 100, it is set to 100.
     * If the resulting energy level falls below 0, it is set to 0.
     * @param energy The amount of energy to add.
     */
	public void addEnergy(float energy) {
		// If energy will overflow, set it to the max instead
		if(this.energy + energy > 100) {
			this.energy = 100;
		}
		// If energy will go below 0, set it to 0 instead
		else if (this.energy + energy < 0) {
			// Maybe in future this could apply an exhausted effect?
			this.energy = 0;
		}
		// Add value to current energy
		else{
			this.energy += energy;
		}
	}
}
