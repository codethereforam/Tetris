package com.yay.tetris;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;

public class Element {
	private int x;
	private int y;
	private Direction dir = Direction.D;
	private Color color;
	private Controller c;
	
	public static final int LENGTH = Controller.WIDTH/20;
	
	public Element(int x, int y, Color color,Controller c) {
		this.x = x;
		this.y = y;
		this.color = color; 
		this.c = c;
	}
	
	public void draw(Graphics g) {
		Color c = g.getColor();
		g.setColor(color);
		g.fillRect(x, y, LENGTH, LENGTH);
		g.setColor(c);
	}
	
	public void moveX() {	
		switch (dir) {
		case L:
			x -= LENGTH;
			break;
		case R:
			x += LENGTH;
			break;
		}
	}
	
	public void moveY() {
		y += LENGTH;
	}
	
	public Rectangle getRect() {
		return new Rectangle(x, y, LENGTH+1, LENGTH+1);
	}
	
	public Point[] getPoints() {
		Point[] points = new Point[4];
		points[0] = new Point(x, y);
		points[1] = new Point(x + LENGTH, y);
		points[2] = new Point(x, y + LENGTH);
		points[3] = new Point(x + LENGTH, y + LENGTH);
		return points;
 	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public void setDir(Direction dir) {
		this.dir = dir;
	}

	public Direction getDir() {
		return dir;
	}
	
	public void down() {
		y += LENGTH;
	}

	public void setXY(int x, int y) {
		this.x = x;
		this.y = y;
	}
}
