package com.heslington_hustle.objects;

import com.badlogic.gdx.math.Rectangle;
import com.heslington_hustle.game.HeslingtonHustle;

public class Object {
	public HeslingtonHustle game;
	
	public String name;
	public Rectangle[] interactRegions;
	public int screen;
	public String tooltip;
	public int tooltipX, tooltipY;
	public int nameX, nameY;
	
	// Parent class of Bed, Building and Foodhall
	public Object(HeslingtonHustle game, String name, Rectangle[] interactRegions, int screen, String tooltip, int tooltipX, int tooltipY, int nameX, int nameY) {
		this.game = game;
		this.name = name;
		this.screen = screen;
		this.tooltip = tooltip;
		this.tooltipX = tooltipX;
		this.tooltipY = tooltipY;
		this.nameX = nameX;
		this.nameY = nameY;
		this.interactRegions = interactRegions;
	}
}
