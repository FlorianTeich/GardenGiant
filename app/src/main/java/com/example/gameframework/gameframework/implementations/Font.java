package com.example.gameframework.gameframework.implementations;

import java.util.LinkedHashMap;

import android.graphics.Bitmap;

import com.example.gameframework.gameframework.interfaces.Image;


public class Font {
	//constants to define available characters
	public final static String SPACE = " ";
	public final static String SYMBOLS1        = "!\"#$%&'()*+,-./";
	public final static String NUMBERS         = "0123456789";
	public final static String SYMBOLS2        = ":;<=>?@";
	public final static String CAPITAL_LETTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	public final static String SYMBOLS3        = "[\\]^_`";
	public final static String LOWER_LETTERS   = "abcdefghijklmnopqrstuvwxyz";
	public final static String SYMBOLS4        = "{|}~";
	public final static String VISIBLE_ASCII   = SPACE + SYMBOLS1 + NUMBERS + SYMBOLS2 + CAPITAL_LETTERS + SYMBOLS3 + LOWER_LETTERS + SYMBOLS4;

	private LinkedHashMap<Character, Image> font;
	
	public Font() {
		font = new LinkedHashMap<Character, Image>();
	}
	
	public void createFont(Image fontImage, String allowedCharacters, int characterWidth, int characterHeight) {
		int x = 0;
		int y = 0;
		for (int i=0; i < allowedCharacters.length(); i++) {
			if (allowedCharacters.charAt(i) == ' ') {
				font.put(' ', new AndroidImage(Bitmap.createBitmap(characterWidth, characterHeight, Bitmap.Config.ALPHA_8)));
				continue;
			}
			
			font.put(allowedCharacters.charAt(i), fontImage.getSubImage(x, y, characterWidth, characterHeight));
			x += characterWidth;
			if (x  >= fontImage.getWidth()) {
				x = 0;
				y += characterHeight;
			}
		}
		fontImage.dispose();
	}
	
	public Image getChar(Character c) {
		if (!font.containsKey(c)) throw new IllegalStateException("'" + c + "' not in font!");
		return font.get(c);
	}
	
	public boolean isReady() {
		return !font.isEmpty();
	}
	
	public void dispose() {
		for (Image image : font.values()) image.dispose();
		font.clear();
	}
}
