/**
 * The Object class represents a generic object in the game.
 * It serves as the parent class for specific objects such as Bed, Building, and Foodhall.
 */
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
	
	/**
     * Constructs a new Object with the specified parameters.
     * @param game The game instance.
     * @param name The name of the object.
     * @param interactRegions The interaction regions of the object.
     * @param screen The screen where the object is located.
     * @param tooltip The tooltip text for the object.
     * @param tooltipX The x-coordinate of the tooltip.
     * @param tooltipY The y-coordinate of the tooltip.
     * @param nameX The x-coordinate of the object's name.
     * @param nameY The y-coordinate of the object's name.
     */
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
