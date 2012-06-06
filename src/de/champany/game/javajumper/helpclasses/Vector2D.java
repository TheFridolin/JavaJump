package de.champany.game.javajumper.helpclasses;

public class Vector2D {
	public double x = 0;
	public double y = 0;
		
	public Vector2D() {
		
	}
	
	public Vector2D (double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	public Vector2D (Vector2D v) {
		this.x = v.x;
		this.y = v.y;
	}
	
	public double getDistanceTo(Vector2D v) {
		return Math.sqrt(
					Math.abs(v.x-this.x)*
					Math.abs(v.x-this.x)+
					Math.abs(v.y-this.y)*
					Math.abs(v.y-this.y)
				);
	}
	
	public Vector2D getAdd(double x, double y) {
		return new Vector2D(this.x+x, this.y+y);
	}
	
	public Vector2D getAdd(int x, int y) {
		return new Vector2D((double) (this.x+x), (double) (this.y+y));
	}
	
	@Override
	public boolean equals(Object other) {
		return (
			other != null && other instanceof Vector2D &&
			x == ((Vector2D) other).x && y == ((Vector2D) other).y);
	}
	
	@Override
	public String toString() {
		return "(" + x + "|" + y + ")";
	}
}
