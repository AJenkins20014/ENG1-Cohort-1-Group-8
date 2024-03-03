package com.heslington_hustle.game;

import com.badlogic.gdx.graphics.Texture;

public class Object {
	private HeslingtonHustle game;
	
	public String name;
	public Texture sprite;
	public float x;
	public float y;
	
	public Object(HeslingtonHustle game, String name, Texture sprite, float x, float y) {
		this.name = name;
		this.sprite = sprite;
		this.x = x;
		this.y = y;
	}
	
	public void onEnter(){
		// Display tooltip
	}
}
