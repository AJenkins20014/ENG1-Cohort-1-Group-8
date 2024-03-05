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
	
	public boolean equals(PopUpText text) {
		if(this == text) {
			return true;
		}
		else if (text == null || getClass() != text.getClass()) {
            return false;
        }
		
		return(this.text == text.text && this.x == text.x && this.y == text.y && this.targetWidth == text.targetWidth && this.align == text.align && this.wrap == text.wrap
				&& this.scale == text.scale && this.color.r == text.color.r  && this.color.g == text.color.g  && this.color.b == text.color.b  && this.color.a == text.color.a);
	}
}
