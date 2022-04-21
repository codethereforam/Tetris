package com.yay.tetris;

public class ShapeT implements Deformable {
	private int type = (int)(Math.random()*4 + 1);

	@Override
	public void deform(Element[] e, int x, int y) {
		if(type == 1) {
			e[0].setXY(x-Element.LENGTH, y);
			e[1].setXY(x, y+Element.LENGTH);
			e[3].setXY(x+Element.LENGTH, y);
			type++;
		} else if(type == 2) {
			e[0].setXY(x-Element.LENGTH, y);
			e[1].setXY(x, y+Element.LENGTH);
			e[3].setXY(x, y-Element.LENGTH);
			type++;
		} else if(type == 3) {
			e[0].setXY(x-Element.LENGTH, y);
			e[1].setXY(x, y-Element.LENGTH);
			e[3].setXY(x+Element.LENGTH, y);
			type++;
		} else if(type == 4) {
			e[0].setXY(x, y-Element.LENGTH);
			e[1].setXY(x, y+Element.LENGTH);
			e[2].setXY(x, y);
			e[3].setXY(x+Element.LENGTH, y);
			type = 1;
		}
	}

	@Override
	public void rollBack(Element[] e, int x, int y) {
		type += 2;
		if(type == 5) {
			type = 1;
		} else if(type == 6) {
			type = 2;
		}
		deform(e, x, y);
	}
}
