package net.spookysquad.spookster.utils;

import java.lang.reflect.Field;

import net.minecraft.client.Minecraft;
import net.minecraft.util.Timer;

public class GameUtil extends Wrapper {

	public static void setGameSpeed(float speed) {
        Field timerField = null;
		
        for(Field field: Minecraft.class.getDeclaredFields()) {
        	if(field.getType() == Timer.class) {
        		timerField = field;
        	}
        }
        
		timerField.setAccessible(true);
		   
		Timer timer = null;
		try {
			timer = (Timer) timerField.get(Minecraft.getMinecraft());
		}catch(Exception e5) { }
		
		if(timer != null) {
			timer.timerSpeed = speed;
		}
	}

	public static double getGameSpeed() {
        Field timerField = null;
		
        for(Field field: Minecraft.class.getDeclaredFields()) {
        	if(field.getType() == Timer.class) {
        		timerField = field;
        	}
        }
        
		timerField.setAccessible(true);
		   
		Timer timer = null;
		try {
			timer = (Timer) timerField.get(Minecraft.getMinecraft());
		}catch(Exception e5) { }
		
		return timer.timerSpeed;
	}
}
