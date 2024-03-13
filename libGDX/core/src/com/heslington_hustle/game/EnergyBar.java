package com.heslington_hustle.game;


public class EnergyBar {
	public float energy;
	
	public EnergyBar(float energy) {
		this.energy = energy;
	}
	
	public void setEnergy(float energy) {
		this.energy = energy;
	}
	
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
