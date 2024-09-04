package ali.orbit;

import java.awt.Graphics;

public class Point {

	public float x = 0,y = 0;
	public float r;
	
	public static Point ZERO = new Point(900.0f / 2.0f, 600.0f / 2.0f, 1);
	
	public Point(float x, float y, float r) {
		this.x = x;
		this.y = y;
		this.r = r;
	}
	
	public Point toScreenSpace(float x, float y, float r) {
		float sX = ZERO.x + x;
		float sY = ZERO.y - y;
		return new Point(sX, sY, r);
	}
	
	public Point toWorldSpace(float x, float y, float r) {
		float sX = ZERO.x + x;
		float sY = ZERO.y - y;
		return new Point(sX, sY, r);
	}
	
	
	public void draw(Graphics g, float r) {
		Point p = this.toScreenSpace(this.x, this.y, r);
		g.fillOval(p.x() - (p.r()), p.y() - (p.r()), p.r()*2, p.r()*2);
	}
	
	public int x() {
		return (int) x;
	}
	
	public int y() {
		return (int) y;
	}
	
	public int r() {
		return (int) r;
	}
}
