package de.champany.game.javajumper.entity;

import de.champany.game.javajumper.helpclasses.*;

public class CollisionBox {
	public int xLeft, xRight, yUp, yDown;
	public Vector2D pos;
	
	public final int UP = 1;
	public final int RIGHT = 2;
	public final int DOWN = 4;
	public final int LEFT = 8;
	
	public CollisionBox() {
		xLeft = 0;
		xRight = 0;
		yUp = 0;
		yDown = 0;
		pos = new Vector2D();
	}
	
	public CollisionBox(int xLeft, int xRight, int yUp,  int yDown, Vector2D pos) {
		this.setSize(xLeft, xRight, yUp, yDown);
		this.pos = pos;
	}
	
	public void setSize(int xLeft, int xRight, int yUp,  int yDown) {
		this.xLeft = xLeft;
		this.xRight = xRight;
		this.yUp = yUp;
		this.yDown = yDown;

	}
	
	public Vector2D getCorner(int cornerID) {
		switch (cornerID) {
		case (UP | LEFT):
			return pos.getAdd(-xLeft, -yUp);
		case (DOWN | LEFT):
			return pos.getAdd(-xLeft, yDown);
		case (DOWN | RIGHT):
			return pos.getAdd(xRight, yDown);
		case (UP | RIGHT):
			return pos.getAdd(xRight, -yUp);
		case 0:
			return pos.getAdd(0, 0);
		default:
			return pos.getAdd(0, 0);
		}
	}
}
