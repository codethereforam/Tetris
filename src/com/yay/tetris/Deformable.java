package com.yay.tetris;

public interface Deformable {
	void deform(Element[] e, int x, int y);
	void rollBack(Element[] e, int x, int y);
}
