package net.spookysquad.spookster.render;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import net.spookysquad.spookster.utils.Wrapper;

import org.lwjgl.opengl.GL11;

public class FontUtil extends Wrapper {
	
	public static String filterText(String text) {
		text = text.replaceAll("&", "§");
		text = text.replaceAll("\247", "§");
		
		return text;
	}

    public static float drawString(String text, float x, float y, int color) {
    	text = filterText(text);
    	GL11.glTranslatef(x, y, 0);
    	float returnFloat = getFont().drawString(text, 0, 0, color, false);
    	GL11.glTranslatef(-x, -y, 0);
    	return returnFloat;
    }
    
    public static float drawCenteredString(String text, float x, float y, int color) {
    	text = filterText(text);
    	return drawString(text, x - (getFont().getStringWidth(text) / 2), y, color);
    }
    
    public static float drawStringWithShadow(String text, float x, float y, int color) {
    	text = filterText(text);
    	GL11.glTranslatef(x, y, 0);
    	float returnFloat = getFont().drawString(text, 0, 0, color, true);
    	GL11.glTranslatef(-x, -y, 0);
    	return returnFloat;
    }
    
    public static float drawStringWithShadow(String text, float x, float y, int color, float width, int shadowColor) {
    	text = filterText(text);
    	GL11.glTranslatef(width, width, 0);
    	drawString(stripColorCodes(text), x, y, shadowColor);
    	GL11.glTranslatef(-width, -width, 0);
    	return drawString(text, x, y, color);
    }

    public static float drawStringWithShadow(String text, float x, float y, int color, float width) {
    	return drawStringWithShadow(text, x, y, color, width, (color & 16579836) >> 2 | color & -16777216);
    }
    
    public static float drawStringWithShadow(String text, float x, float y, int color, float width, float scale) {
    	GL11.glScalef(scale, scale, 0);
    	float thingy = drawStringWithShadow(text, x * (1.0F / scale), y * (1.0F / scale), color, width, (color & 16579836) >> 2 | color & -16777216);
    	GL11.glScalef(1.0F / scale, 1.0F / scale, 0);
    	return thingy;
    }
    
    public static float drawCenteredStringWithShadow(String text, float x, float y, int color) {
    	return drawStringWithShadow(text, x - (getFont().getStringWidth(text) / 2), y, color);
    }
    
    public static float drawCenteredStringWithShadow(String text, float x, float y, int color, float width) {
    	return drawStringWithShadow(text, x - (getFont().getStringWidth(text) / 2), y, color, width);
    }
    
    public static float drawCenteredStringWithShadow(String text, float x, float y, int color, float width, int shadowColor) {
    	return drawStringWithShadow(text, x - (getFont().getStringWidth(text) / 2), y, color, width, shadowColor);
    }
    
    public static float drawStringWithOutline(String text, float x, float y, int color, float width, int shadowColor) {
    	GL11.glTranslatef(width, 0, 0);
    	drawString(stripColorCodes(text), x, y, shadowColor);
    	GL11.glTranslatef(0, width, 0);
    	drawString(stripColorCodes(text), x, y, shadowColor);
    	GL11.glTranslatef(-width, -width, 0);
    	GL11.glTranslatef(-width , 0, 0);
    	drawString(stripColorCodes(text), x, y, shadowColor);
    	GL11.glTranslatef(0, -width, 0);
    	drawString(stripColorCodes(text), x, y, shadowColor);
    	GL11.glTranslatef(width, width, 0);
    	
    	return drawString(text, x, y, color);
    }

    public static float drawStringWithOutline(String text, float x, float y, int color, float width) {
    	return drawStringWithOutline(text, x, y, color, width, (color & 16579836) >> 2 | color & -16777216);
    }
    
    public static float drawCenteredStringWithOutline(String text, float x, float y, int color, float width) {
    	return drawStringWithOutline(text, x - (getFont().getStringWidth(text) / 2), y, color, width);
    }
    
    public static float drawCenteredStringWithOutline(String text, float x, float y, int color, float width, int shadowColor) {
    	return drawStringWithOutline(text, x - (getFont().getStringWidth(text) / 2), y, color, width, shadowColor);
    }

    private static final Pattern patternControlCode = Pattern.compile("(?i)\\u00A7[0-9A-FK-OR]");
    private static final Pattern patternColorCode = Pattern.compile("(?i)\\u00A7[0-9A-F]");

    private static String stripColorCodes(String text) {
        return patternColorCode.matcher(text).replaceAll("");
    }
    
    private static String stripControlCodes(String text) {
        return patternControlCode.matcher(text).replaceAll("");
    }

	public static int getFontHeight() {
		return 9;
	}
	
	public static List<String> wrapText(String text, double width) {
		List<String> finalWords = new ArrayList<String>();
		text = filterText(text);
		if (getFont().getStringWidth(text) > width) {
			String[] words = text.split(" ");
			String currentWord = "";
			int stringCount = 0;
			char lastColorCode = (char) -1;

			for (String word : words) {
				for(int i = 0; i < word.toCharArray().length; i++) {
					char c = word.toCharArray()[i];
					
					if(c == '\247' && i < word.toCharArray().length - 1) {
						lastColorCode = word.toCharArray()[i + 1];
					}
				}
				if (getFont().getStringWidth(currentWord + word + " ") < width) {
					currentWord += word + " ";
				} else {
					finalWords.add(currentWord);
					currentWord = (lastColorCode == -1 ? word + " " : "\247" + lastColorCode + word + " ");
					stringCount++;
				}
			}
			if (!currentWord.equals("")) {
				if (getFont().getStringWidth(currentWord) < width) {
					finalWords.add((lastColorCode == -1 ? currentWord + " " : "\247" + lastColorCode + currentWord + " "));
					currentWord = "";
					stringCount++;
				} else {
					for (String s : formatString(currentWord, width))
						finalWords.add(s);
				}
			}
		} else
			finalWords.add(text);
		return finalWords;
	}
	
	public static List<String> formatString(String s, double width) {
		List<String> finalWords = new ArrayList<String>();
		String currentWord = "";
		char lastColorCode = (char) -1;
		for (int i = 0; i < s.toCharArray().length; i++) {
			char c = s.toCharArray()[i];
			
			if(c == '\247' && i < s.toCharArray().length - 1) {
				lastColorCode = s.toCharArray()[i + 1];
			}
			
			if (getFont().getStringWidth(currentWord + c) < width) {
				currentWord += c;
			} else {
				finalWords.add(currentWord);
				currentWord = (lastColorCode == -1 ? String.valueOf(c):"\247" + lastColorCode + String.valueOf(c));
			}
		}

		if (!currentWord.equals("")) {
			finalWords.add(currentWord);
		}

		return finalWords;
	}
}
