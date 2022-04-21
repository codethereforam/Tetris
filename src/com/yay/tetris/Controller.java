package com.yay.tetris;

import java.awt.Color;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Panel;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.*;

import javax.swing.JOptionPane;

public class Controller extends Frame {
	private static final long serialVersionUID = 2744372514261636296L;
	public static final int WIDTH = 800;
	public static final int HEIGHT = 800;
	public static final Color MAJOR_BG = new Color(200, 255, 255);
	public static final Color MINOR_BG = new Color(200, 224, 255);
	/**
	 * shape 在MinorPanel中的x坐标
	 */
	public static final int X_IN_MP = 185;
	public static final int Y_IN_MP = 270;
	//1~11
	public static final int LEVEL = 6;

	private Item item;	
	private List<Element> elements = new ArrayList<>();

	private MajorPanel majorPanel = new MajorPanel();
	private MinorPanel minorPanel = new MinorPanel();
	
	private boolean stop = false;
	private boolean close = false;
	
	private int score = 0;
	
	public Controller() {
		produce();
	}
	
	public void produce() {
		item = new Item(LEVEL, this);
		for(Element e : item.getElements()) {
			elements.add(e);
		}
//XXX		
		minorPanel.repaint();
	}
	
	public void removeLine(int n, int count) {	
		if(count == WIDTH/2/Element.LENGTH) {
			for(Iterator<Element> i = elements.iterator(); i.hasNext();) {
				Element e = i.next();
				if(e.getY() == item.getElements()[n].getY()) {
					i.remove();
				} else if(e.getY() < item.getElements()[n].getY()) {
					e.down();
				}
			}
			
			addScore();
		}
	}
	
	public void addScore() {
		score += 10;
	}
	
	//if you die
	public void endAction() {
		close = true;
		JOptionPane.showMessageDialog(this, "Game Over!!! Your score:" + score);
	}
	
	private class KeyMonitor extends KeyAdapter {		
		@Override
		public void keyPressed(KeyEvent e) {
			item.keyPressed(e);
		}

		@Override
		public void keyReleased(KeyEvent e) {
			item.keyReleased(e);
		}		
	}
	
	private class MajorPanel extends Panel {	
		private static final long serialVersionUID = 4612663490895979798L;

		@Override
		public void paint(Graphics g) {
			/**
			 * 画出当前移动的Item
			 */
			item.draw(g);
			/**
			 * 画出当前stop的Element
			 */
			for(Element e : elements) {
				if(e.getDir() == Direction.STOP) {
					e.draw(g);
				}
			}
			/**
			 * 显示分数
			 */
			Color c = g.getColor();
			Font f = g.getFont();
			
			g.setColor(Color.red);
			g.setFont(new Font("Verdana", Font.ITALIC, 40));
			g.drawString("score:" + score, 5, 40);
			
			g.setFont(f);
			g.setColor(c);
		}
	}
	
	private class MinorPanel extends Panel {	
		private static final long serialVersionUID = 360653545001786348L;

		@Override
		public void paint(Graphics g) {
			Color c = g.getColor();
			Font f = g.getFont();
			
			g.setColor(Color.orange);
			g.setFont(new Font("Verdana", Font.ITALIC, 60));
			g.drawString("next shape", 40, 100);
			
			g.setColor(Color.red);
			g.drawRect(100, 150, 250, 250);		
			item.drawNextShape(g, X_IN_MP, Y_IN_MP);
			
			g.setColor(Color.blue);
			g.setFont(new Font("Consolas", Font.CENTER_BASELINE, 60));
			g.drawString("TETRIS", 90, 650);
			g.setFont(new Font("Consolas", Font.CENTER_BASELINE, 30));
			g.drawString("by yay", 230, 700);
			
			g.setFont(f);
			g.setColor(c);
		}
	}
	
	public void stopOrContinue() {
		if(!stop) {
			stop = true;
		} else {
			stop = false;
		}
	}
	
	public void launchGame() {
		this.setTitle("Tetris by yay");
		this.setBounds(200, 100, WIDTH, HEIGHT);
		this.setLayout(new GridLayout(1,2));
		this.setResizable(false);
		
		majorPanel.setBackground(MAJOR_BG);
		minorPanel.setBackground(MINOR_BG);
		this.add(majorPanel);
		this.add(minorPanel);
		
		this.addKeyListener(new KeyMonitor());
		
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				close = true;
				System.exit(0);
			}	
		});
		this.setVisible(true);
		
		new Thread(() -> {	
			while(!close) {
				if(!stop) {
					majorPanel.repaint();
				}
				try {
					Thread.sleep(450 - item.getLevel()*40);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}).start();
	}
	
	public static void main(final String...args) {
		new Controller().launchGame();
	}

	public List<Element> getElements() {
		return elements;
	}
}
