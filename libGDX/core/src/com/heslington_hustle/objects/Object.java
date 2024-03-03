package com.heslington_hustle.objects;

import com.badlogic.gdx.graphics.Texture;
import com.heslington_hustle.game.HeslingtonHustle;

public class Object {
	public HeslingtonHustle game;
	
	public String name;
	public Texture sprite;
	public float x;
	public float y;
	public String tooltip;
	
	public Object(HeslingtonHustle game, String name, Texture sprite, float x, float y, String tooltip) {
		this.game = game;
		this.name = name;
		this.sprite = sprite;
		this.x = x;
		this.y = y;
		this.tooltip = tooltip;
	}
}
