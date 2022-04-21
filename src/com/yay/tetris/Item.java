package com.yay.tetris;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.util.Random;

//Item位置为element[2]的位置
public class Item {
	private int level;
	private int preLevel;
	private Controller c;
	
	private Element[] elements = new Element[4];
	private Deformable shape;
	
	private static Deformable nextShape = randomShape();
	private static Element[] nextElements = new Element[4];
	
	private static boolean first = true;
	
	public Item(int level, Controller c) {
		this.level = level;
		preLevel = level;
		this.c = c;
		
		shape = nextShape;		
		if(first) {
			int x = Controller.WIDTH/4;
			int y = 0;
			initElements(elements, ColorMap.getColor(shape.getClass().getSimpleName()),x, y);
			shape.deform(elements, x, y);
			first = false;
		} else {
			elements = nextElements;
			moveElementsToMP(elements);
		}
		
		nextShape = randomShape();
	}
	
	public void moveElementsToMP(Element[] elements) {
		for(Element e : elements) {
			e.setXY(e.getX() + Controller.WIDTH/4 - Controller.X_IN_MP, e.getY() - Controller.Y_IN_MP);
		}
	}
	
	public static Deformable randomShape() {
		Random r = new Random();
		int n = r.nextInt(7) + 1;
		switch(n) {
		case 1 :
			return new ShapeI();
		case 2 :
			return new ShapeJ();
		case 3 :
			return new ShapeL();
		case 4 :
			return new ShapeO();
		case 5 :
			return new ShapeS();
		case 6 :
			return new ShapeZ();
		case 7 :
			return new ShapeT();
		}
		return null;
	}
	
	public void initElements(Element[] ele, Color color,int x, int y) {
		for(int i=0; i< ele.length; i++) {
			ele[i] = new Element(x, y, color, c);
		}
	}
	
	public void draw(Graphics g) {	
		if(!move()) {
			return;
		}
		drawElements(g, elements);
	}
	
	public void drawElements(Graphics g, Element[] elements) {
		for(Element e : elements) {
			e.draw(g);
		}
	}
	
	public void drawNextShape(Graphics g, int xx, int yy) {
		nextElements = new Element[4];
		initElements(nextElements, ColorMap.getColor(nextShape.getClass().getSimpleName()),xx, yy);
		nextShape.deform(nextElements, xx, yy);
		
		for(Element e : nextElements) {
			e.draw(g);
		}
	}
	
	public void keyPressed(KeyEvent e) {
		int keyCode = e.getKeyCode();
		switch(keyCode) {
		case KeyEvent.VK_UP:
			shape.deform(elements, elements[2].getX(), elements[2].getY());
			if(isOutOfBoundsOrIntersects()) {
				shape.rollBack(elements, elements[2].getX(), elements[2].getY());
			}
			break;
		case KeyEvent.VK_DOWN:			
				this.level = 10; 
			break;
		case KeyEvent.VK_LEFT :
			setDir(Direction.L);
			break;
		case KeyEvent.VK_RIGHT:
			setDir(Direction.R);
			break;
		case KeyEvent.VK_SPACE:
			c.stopOrContinue();
			break;
		}
	}
	
	public void setDir(Direction d) {
		for(Element ele : elements) {
			ele.setDir(d);
		}
	}
	
	public void keyReleased(KeyEvent e) {	
		if(e.getKeyCode() == KeyEvent.VK_DOWN) {			
				this.level = preLevel; 		
		}
	}	

	public boolean isOutOfBoundsOrIntersects() {
		for(Element e : elements) {
			if(e.getX() < 0 || e.getX() + Element.LENGTH > Controller.WIDTH/2
					|| e.getY() + Element.LENGTH*2 > Controller.HEIGHT) {
						return true;
			}
			
			for(Element ce : c.getElements()) {
				if( ce.getDir() != Direction.STOP ) {
					continue;
				}
				
				if( e.getRect().intersects( ce.getRect() ) ) {
					return true;
				}
			}
		}
		return false;
	}
	
	public boolean move() {
		if(isStop()) {
			return false;
		}
		
		boolean moveX = true;
		
		for(Element e : elements) {
			e.moveY();
		}
		
		for(Element e : elements) {
			if(e.getX() == 0 && e.getDir() == Direction.L) {
				moveX = false;	
				break;
			}
			
			if((e.getX() + Element.LENGTH) == Controller.WIDTH/2 && e.getDir() == Direction.R) {
				moveX = false;		
				break;
			}
			
			for(Element ce : c.getElements()) {
				if( ce.getDir() != Direction.STOP ) {
					continue;
				}
					
				Point[] points = ce.getPoints();
				
				if( e.getPoints()[1].equals(points[0]) && e.getPoints()[3].equals(points[2])
				|| e.getPoints()[0].equals(points[1]) && e.getPoints()[2].equals(points[3])) {
					moveX = false;	
					break;
				}
			}
			
			if(!moveX) {
				break;
			}
		}
		
		if(moveX) {
			for(Element e : elements) {
				e.moveX();
				e.setDir(Direction.D);
			}
		}
		return true;
	}
	
	public boolean isStop() {
		for(Element e : elements) { 
			boolean isIntersect = false;
			
			for(Element ce : c.getElements()) {
				if( ce.getDir() != Direction.STOP ) {
					continue;
				}
					
				Point[] points = ce.getPoints();
				
				if( e.getPoints()[2].equals(points[0]) && e.getPoints()[3].equals(points[1])) {
					isIntersect = true;
					break;
				}
			}
			
			if(e.getY() + Element.LENGTH*2   == Controller.HEIGHT || isIntersect) {
				if(hitTop()) {
					c.endAction();
				}
				for(Element ee : elements) {
					ee.setDir(Direction.STOP);
				}
				remove();
				c.produce();
				return true;
			}
		}
		return false;
	}
	
	public boolean remove() {
		int[] count = new int[4];
		for(Element e : c.getElements()) {	
			if(e.getY() == elements[0].getY()) {
				count[0]++;
			}
			
			if(elements[1].getY() != elements[0].getY() && e.getY() == elements[1].getY()) {
				count[1]++;
			}
			
			if(elements[2].getY() != elements[0].getY() && elements[2].getY() != elements[1].getY() && e.getY() == elements[2].getY()) {
				count[2]++;
			}
			
			if(elements[3].getY() != elements[0].getY() && elements[3].getY() != elements[1].getY() && elements[3].getY() != elements[2].getY() && e.getY() == elements[3].getY()) {
				count[3]++;
			}
		}
		
		for(int i=0; i<count.length; i++) {
			c.removeLine(i, count[i]);
		}
		
		return true;
	}
	
	public boolean hitTop() {
		for(Element e : elements) {
			if(e.getY() < 0) {
				return true;
			}
		}
		return false;
	}

	public int getLevel() {
		return level;
	}

	public Element[] getElements() {
		return elements;
	}
}
