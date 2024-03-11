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
		if(this.energy + energy > 100) {
			this.energy = 100;
		}
		else if (this.energy + energy < 0) {
			// Maybe applies an exhausted effect?
			this.energy = 0;
		}
		else{
			this.energy += energy;
		}
	}
}
