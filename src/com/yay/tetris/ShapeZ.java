package com.yay.tetris;

import java.util.Random;

public class ShapeZ implements Deformable {
	private Random r = new Random();
	private boolean type = r.nextBoolean();

	@Override
	public void deform(Element[] e, int x, int y) {
		if(type) {
			e[0].setXY(x-Element.LENGTH, y-Element.LENGTH);
			e[1].setXY(x, y-Element.LENGTH);
			e[3].setXY(x+Element.LENGTH, y);
			type = false;
 		} else {
 			e[0].setXY(x+Element.LENGTH, y-Element.LENGTH);
			e[1].setXY(x+Element.LENGTH, y);
			e[3].setXY(x, y+Element.LENGTH);
 			type = true;
		}
	}

	@Override
	public void rollBack(Element[] e, int x, int y) {
		deform(e, x, y);
	}
}
