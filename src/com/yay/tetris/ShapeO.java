package com.yay.tetris;

public class ShapeO implements Deformable{

	@Override
	public void deform(Element[] e, int x, int y) {
		e[0].setXY(x-Element.LENGTH, y);
		e[1].setXY(x-Element.LENGTH, y+Element.LENGTH);
		e[3].setXY(x, y+Element.LENGTH);
	}

	@Override
	public void rollBack(Element[] e, int x, int y) {}
}
