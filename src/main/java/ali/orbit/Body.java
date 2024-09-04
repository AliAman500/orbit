package ali.orbit;

import java.awt.Color;
import java.awt.Graphics;
import java.util.LinkedList;

public class Body {
	
	public float m = 1;
	public float r = 10;
	public Point position = new Point(0, 0, r);
	public float velX = 0;
	public float velY = 0;
	public Color color;
	
	public LinkedList<Point> path = new LinkedList<Point>();
	
	public Body(float m, float r, float x, float y, Color color) {
		this.m = m;
		this.r = r;
		position.x = x;
		position.y = y;
		position.r = r;
		this.color = color;
	}
	
	public void force(float f, float angle, float timestep) {
		float a = f/m;
		
		velX += a * Math.cos(angle);
		velY += a * Math.sin(angle);
	}
	
	int count = 0;
	
	public void tick(float timestep) {
		count++;
		position.x += velX * timestep;
		position.y += velY * timestep;
		
		if(count % 1 == 0) {
			path.add(new Point(position.x, position.y, 4));
			count = 0;
		}
		
		if(path.size() >= 200) {
			path.removeFirst();
		}
	}

	int alpha = 255;
	
	public void render(Graphics g) {
		for(int i = 0; i < path.size(); i++) {
			g.setColor(new Color(color.getRed() / 255f, color.getGreen() / 255f, color.getBlue() / 255f, i/1000f));
			path.get(i).draw(g, 3);
		}
		g.setColor(color);
		position.draw(g, r);
	}
}
