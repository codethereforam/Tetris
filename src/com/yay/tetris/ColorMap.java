package com.yay.tetris;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

public class ColorMap {
	private static Map<String, Color> map = new HashMap<>(); 
	
	static {
		map.put("ShapeI", Color.orange);
		map.put("ShapeJ", Color.magenta);
		map.put("ShapeL", Color.blue);
		map.put("ShapeO", Color.red);
		map.put("ShapeS", Color.cyan);
		map.put("ShapeT", Color.yellow);
		map.put("ShapeZ", Color.green);
	}
	
	public static Color getColor(String className) {
		return map.get(className);
	}
}
