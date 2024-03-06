package com.heslington_hustle.objects;

import com.badlogic.gdx.graphics.Texture;
import com.heslington_hustle.game.HeslingtonHustle;

public class Bed extends Object {
	public Float energyChange;
	
	public Bed(HeslingtonHustle game, String name, Texture sprite, float x, float y, String tooltip, Float energyChange) {
		super(game, name, sprite, x, y, tooltip);
		this.energyChange = energyChange;
	}
	
	public void startNewDay() {
		
	}

}