package com.heslington_hustle.game;

import com.badlogic.gdx.graphics.Texture;

public class EnergyBar {
	public float energy;
	public Texture sprite;
	
	public EnergyBar(Texture sprite, float energy) {
		this.energy = energy;
		this.sprite = sprite;
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
