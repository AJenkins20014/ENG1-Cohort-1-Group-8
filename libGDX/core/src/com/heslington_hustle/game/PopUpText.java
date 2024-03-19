/**
 * The PopUpText class represents text that pops up on the screen.
 */
package com.heslington_hustle.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.Align;

public class PopUpText {
	public String text;
	public float x;
	public float y;
	public float targetWidth;
	public int align;
	public boolean wrap;
	public float scale;
	public Color color;
	
	/**
     * Constructs a new PopUpText object with the specified parameters.
     * @param text The text content of the pop-up.
     * @param x The x-coordinate of the pop-up position.
     * @param y The y-coordinate of the pop-up position.
     * @param targetWidth The target width of the pop-up text.
     * @param align The alignment of the pop-up text.
     * @param wrap Indicates whether the pop-up text should wrap if it exceeds the target width.
     * @param scale The scale of the pop-up text.
     * @param color The colour of the pop-up text.
     */
	public PopUpText(String text, float x, float y, float targetWidth, int align, boolean wrap, float scale, Color color) {
		this.text = text;
		this.x = x;
		this.y = y;
		this.targetWidth = targetWidth;
		this.align = align;
		this.wrap = wrap;
		this.scale = scale;
		this.color = color;
	}
	
	/**
     * Constructs a new PopUpText object with the specified parameters and default values.
     * @param text The text content of the pop-up.
     * @param x The x-coordinate of the pop-up position.
     * @param y The y-coordinate of the pop-up position.
     * @param scale The scale of the pop-up text.
     */
	public PopUpText(String text, float x, float y, float scale) {
		this.text = text;
		this.x = x;
		this.y = y;
		this.targetWidth = 100;
		this.align = Align.left;
		this.wrap =  false;
		this.scale = scale;
		this.color = new Color(1,1,1,1);
	}
	
	/**
     * Constructs a new PopUpText object with the specified parameters.
     * @param text The text content of the pop-up.
     * @param x The x-coordinate of the pop-up position.
     * @param y The y-coordinate of the pop-up position.
     * @param scale The scale of the pop-up text.
     * @param color The colour of the pop-up text.
     */
	public PopUpText(String text, float x, float y, float scale, Color color) {
		this.text = text;
		this.x = x;
		this.y = y;
		this.targetWidth = 100;
		this.align = Align.left;
		this.wrap =  false;
		this.scale = scale;
		this.color = color;
	}
	
	/**
     * Constructs a new PopUpText object with the specified parameters.
     * @param text The text content of the pop-up.
     * @param x The x-coordinate of the pop-up position.
     * @param y The y-coordinate of the pop-up position.
     */
	public PopUpText(String text, float x, float y) {
		this.text = text;
		this.x = x;
		this.y = y;
		this.targetWidth = 100;
		this.align = Align.left;
		this.wrap =  false;
		this.scale = 1.0f;
		this.color = new Color(1,1,1,1);
	}
	
	/**
     * Checks if this PopUpText object is equal to another object.
     * @param text The PopUpText object to compare.
     * @return true If the objects are equal, false otherwise.
     */
	public boolean equals(PopUpText text) {
		// Check if a pop up is exactly the same as another to avoid spam
		if(this == text) {
			return true;
		}
		else if (text == null || getClass() != text.getClass()) {
            return false;
        }
		
		// Checks if each individual variable is the same in both objects
		return(this.text == text.text && this.x == text.x && this.y == text.y && this.targetWidth == text.targetWidth && this.align == text.align && this.wrap == text.wrap
				&& this.scale == text.scale && this.color.r == text.color.r  && this.color.g == text.color.g  && this.color.b == text.color.b  && this.color.a == text.color.a);
	}
}
