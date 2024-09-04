package ali.orbit;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferStrategy;
import java.util.LinkedList;
import java.util.Random;
import java.util.Scanner;

public class App extends Canvas implements Runnable, MouseListener, KeyListener, MouseMotionListener {
	
	private static final long serialVersionUID = 1L;
	
	private boolean running = false;
	private Thread thread;
	
	public float mdx = 0;
	public float mdy = 0;
	
	public int mx = 0;
	public int my = 0;
	
	public float cSpeed = 4;
	
	public boolean up = false, down = false, left = false, right = false;
	
	long secondsPassed;
	
	public float FPS = 0;
	public float timescale = 5;
	
	public float amountOfTicks = 60;
	public float cameraX = 0, cameraY = 0;
	
	public float timestep = 1.0f/amountOfTicks;
	
	public static final float G = 1;
	
	public float mass = 0.0001f;
	public float radius = 4;
	public Color color = Color.CYAN;
	public float velX = 0;
	public float velY = 40;
	
	public LinkedList<Body> bodies = new LinkedList<Body>();
	
	public App() {
		this.addMouseListener(this);
		this.addMouseMotionListener(this);
		this.addKeyListener(this);
		this.setFocusable(true);
	}
	
	public void run() {
		long lastTime = System.nanoTime();
		double ns = 1000000000 / amountOfTicks;
		double delta = 0;
		long timer = System.currentTimeMillis();
		int frames = 0;
		while(running){
			long now = System.nanoTime();
			delta += (now - lastTime) / ns;
			lastTime = now;
			while(delta >= 1){
				if(up) {
					cameraY += cSpeed;
				}
				if(down) {
					cameraY -= cSpeed;
				}
				if(left) {
					cameraX += cSpeed;
				}
				if(right) {
					cameraX -= cSpeed;
				}
				for(int i = 0; i < timescale; i++) {
					tick();
				}
				delta--;
			}
			render();
			frames++;
					
			if(System.currentTimeMillis() - timer > 1000){
				timer += 1000;
				FPS = frames;
				frames = 0;
			}
		}
	}

	Body sun1 = new Body(10000, 10, -70, 0, Color.yellow);
	Body sun2 = new Body(10000, 10, 70, 0, Color.yellow);
	Body earth = new Body(0.001f, 4, 150, 0, new Color(50, 50, 255));
	Body mars = new Body(1.5f, 3, 250, 0, new Color(50, 255, 0));
	Body saturn = new Body(4f, 5, 300, 0, new Color(109, 30, 240));
	
	public void start() {
		thread = new Thread(this);
		thread.start();
		running = true;
		earth.velY = 64;
		mars.velY = 30;
		saturn.velY = 30;
		
		sun2.velY = -48;
		
//		bodies.add(earth);
		bodies.add(sun1);
//		bodies.add(mars);
//		bodies.add(saturn);
		
		System.out.println("Welcome. Add new planet? (y/n):");
		Scanner scanner = new Scanner(System.in);
		String s = scanner.nextLine();
		while(!s.equals("y")) {
			System.out.println("Sorry. try again:");
			s = scanner.nextLine();
		}
		while(!s.equals("n")) {
			System.out.println("Define new body? Enter mass:");
			mass = scanner.nextFloat();
			System.out.println("Enter radius:");
			radius = scanner.nextFloat();
			System.out.println("Enter initial velocity X:");
			velX = scanner.nextFloat();
			System.out.println("Enter initial velocity Y:");
			velY = scanner.nextFloat();			
			System.out.println("Good! Now click anywhere to add it to the world!");
		}
		scanner.close();
	}
	
	int count = 0;
	
	public void tick() {
		count++;
		if(count % 60 == 0) {
			secondsPassed++;
		}
		
		for(int i = 0; i < bodies.size(); i++) {
			for(int j = i+1; j < bodies.size(); j++) {
				Body b1 = bodies.get(i);
				Body b2 = bodies.get(j);
				
				float r = (float) Math.sqrt(Math.pow(b2.position.x - b1.position.x, 2) + Math.pow(b2.position.y - b1.position.y, 2));
				float f = (G*b1.m*b2.m) / (r*r);
				
				float dx = b2.position.x - b1.position.x;
				float dy = b2.position.y - b1.position.y;
				
				if(dx > 0) {
					f = -f;
				} else if(dx > 0) {
					f = -f;
				}
				
				float sA = (float) Math.atan(dy/dx);
				
				b2.force(f, sA, timestep);
				b1.force(-f, sA, timestep);
			}
			bodies.get(i).tick(timestep);
		}
	}

	public void render() {
		BufferStrategy bs = this.getBufferStrategy();
		if(bs == null) {
			this.createBufferStrategy(3);
			return;
		}
		
		Graphics g = bs.getDrawGraphics();
		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		
		g.setColor(Color.black);
		g.fillRect(0, 0, getWidth(), getHeight());
		
		Point.ZERO.x = this.getWidth() / 2;
		Point.ZERO.y = this.getHeight() / 2;
		
		g2d.translate(cameraX, cameraY);
		
		for(int i = 0; i < bodies.size(); i++) {
			bodies.get(i).render(g);
		}
		
		g2d.translate(-cameraX, -cameraY);
		g.setColor(Color.white);
		g.setFont(new Font("Ariel", Font.PLAIN, 20));
		g.drawString("Seconds Passed: " + secondsPassed, 10, 20);
		g.drawString("Time scale: " + timescale, 10, 40);
		g.drawString("Camera speed: " + cSpeed, 10, 60);
		
		g.setFont(new Font("Ariel", Font.PLAIN, 16));
		g.setColor(new Color(0, 150, 255));
		g.drawString("Initial Velocity: "+velY, mx + 10, my + 10);
		g.setFont(new Font("Ariel", Font.PLAIN, 16));
//		g.setColor(new Color(255, 255, 0, 100));
//		g.drawString("Sun", (int)Point.ZERO.x - 14, (int)Point.ZERO.y + 28);
		
		g.dispose();
		bs.show();
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		if(e.getButton() == MouseEvent.BUTTON1) {
			color = new Color(new Random().nextInt(100) + 155, new Random().nextInt(255), new Random().nextInt(255));
//			color = new Color(0, 150, 255);
			Body body = new Body(mass, radius, (e.getX() - cameraX) - Point.ZERO.x, Point.ZERO.y - (e.getY()-cameraY), color);
			body.velX = 0;
			body.velY = velY;
			bodies.add(body);
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_UP) {
			timescale++;
		}
		
		if(e.getKeyCode() == KeyEvent.VK_DOWN && timescale > 0) {
			timescale--;
		}
		
		if(e.getKeyCode() == KeyEvent.VK_RIGHT) {
			cSpeed++;
		}
		
		if(e.getKeyCode() == KeyEvent.VK_LEFT && cSpeed > 1) {
			cSpeed--;
		}
		
		if(e.getKeyCode() == KeyEvent.VK_W) {
			up = true;
		}
		if(e.getKeyCode() == KeyEvent.VK_S) {
			down = true;
		}
		if(e.getKeyCode() == KeyEvent.VK_A) {
			left = true;
		}
		if(e.getKeyCode() == KeyEvent.VK_D) {
			right = true;
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_W) {
			up = false;
		}
		if(e.getKeyCode() == KeyEvent.VK_S) {
			down = false;
		}
		if(e.getKeyCode() == KeyEvent.VK_A) {
			left = false;
		}
		if(e.getKeyCode() == KeyEvent.VK_D) {
			right = false;
		}
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	int oldX = -1;
	int oldY = -1;
	public void mouseMoved(MouseEvent e) {
		int newX = e.getX();
		int newY = e.getY();
		
		mx = e.getX();
		my = e.getY();
		
		if(oldX == -1) {
			oldX = newX;
			oldY = newY;
			return;
		}
		
		mdx = newX - oldX;
		mdy = newY - oldY;
		
		oldX = newX;
		oldY = newY;
	}
}
