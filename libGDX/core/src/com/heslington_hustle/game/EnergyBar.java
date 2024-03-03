package com.heslington_hustle.game;

import com.badlogic.gdx.graphics.Texture;

public class EnergyBar {
	public float energy;
	public Texture sprite;
	
	public EnergyBar(Texture sprite, Float energy) {
		this.energy = energy;
		this.sprite = sprite;
	}
	
	public void setEnergy(Float energy) {
		this.energy = energy;
	}
	
	public Float getEnergy() {
		return(energy);
	}
}
