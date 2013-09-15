package LD27;


import game.essential.Drawable;
import game.essential.Matrix2D;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.util.ArrayList;

import Menu.MainMenu;

public class Game extends GameObject implements Drawable {

	private Player player = new Player();
	
	private ArrayList<Trap> traps = new ArrayList<Trap>();
	
	/** Milliseconds */
	private double timePassed = 0;
	
	private long timeInit;
	
	public Game()
	{
		mainCamera.followList.add(player);
		slide.setReleaseListener(player);
		timeInit = System.currentTimeMillis();
	}
	public boolean paused = false;
	
	private long updateTime = 0;
	@Override
	public void update(long delta)
	{
		double timeFactor = delta / Main.targetFrameDuration;
		System.out.println(timeFactor);
		
		updateTime = System.currentTimeMillis();
		if (paused) return;
		// Timing
		double timeSinceLast = delta / 1000000;
		if (player.vel.x > 1) {
			timeSinceLast /= player.vel.x / 2;
		}
		timePassed += timeSinceLast;
		
		//
		
		updateLevel();
		
		
		
		player.update(delta);
		
		checkCollision();
		
		//
		mainCamera.update();
		fps.update(delta);
		slide.update(delta);
		
		if (timePassed > 10000) {
			int totalTime = (int) (System.currentTimeMillis() - timeInit);
			Main.getInstance().endGame(this, totalTime);
		}
		updateTime = System.currentTimeMillis() - updateTime;
	}

	private int lastCamera = 0;
	private void updateLevel()
	{
		// Distance between the traps:
		int minDistance = 40;
		int maxDistance = 400;
		if (Math.abs(player.vel.x) > 1) {
			maxDistance *= Math.abs(player.vel.x / 2);
			minDistance *= Math.abs(player.vel.x);
		}
		
		int sinceLastUpdate = (int) Math.abs(mainCamera.position.x - lastCamera);
		
		if (sinceLastUpdate > minDistance) {
			if (sinceLastUpdate > maxDistance) {
				addTrap();
			} else if (Math.random() > 0.9) {
				addTrap();
			}
		}

		/*int i = 0;
		while (i < sinceLastUpdate) {
			// take a step.
			int step = (int) (Math.random() * (maxDistance - minDistance) + minDistance);
			i += step;
			
			Trap newTrap = new Trap(mainCamera.position.x + i + Main.WIDTH / 2);
			traps.add(newTrap);
		}*/
		
		
		// Remove old traps.
		for (int i = traps.size() - 1; i >= 0; i --) {
			if (traps.get(i).pos.x < mainCamera.position.x - Main.WIDTH / 2 - 50){
				traps.remove(i);
			}
		}
		
	}
	private void addTrap()
	{
		Trap newTrap = new Trap(mainCamera.position.x + Main.WIDTH / 2);
		traps.add(newTrap);
		lastCamera = (int) mainCamera.position.x;
	}
	
	private void checkCollision()
	{
		for (int i = 0; i < traps.size(); i ++) {
			Trap t = traps.get(i);
			if (t.functioning) {
				double distance = player.pos.x - t.pos.x;
				if (distance < 10 && distance > - 5) {
					if (player.state != Player.STATE.JUMPING || (player.state == Player.STATE.JUMPING && (player.getJumpPhase() < 2 || player.getJumpPhase() > 10))) {
						System.out.println("Collision");
						t.functioning = false;
						t.rotationSpeed = (Math.random() - 0.5) / 2;
						t.vel.x = player.vel.x * 1.4 * (Math.random() * 0.4 + 1);
						t.vel.y = - player.vel.x * Math.random();
						
						// Slow down player
						player.vel.x *= 0.8;
					}
				}
			}
		}
	}

	Camera mainCamera = new Camera(Main.WIDTH, Main.HEIGHT);
	
	Background bg = new Background(Main.WIDTH, Main.HEIGHT);
	
	FpsCounter fps = new FpsCounter(Main.WIDTH - 60, Main.HEIGHT - 10);
	
	PowerSlide slide = new PowerSlide(Main.WIDTH / 2, 40);
	
	private long renderTime = 0;
	
	@Override
	public void renderTo(BufferedImage destination, Matrix2D matrix)
	{
		renderTime = System.currentTimeMillis();
		if (paused) return;
		BufferedImage image = new BufferedImage(Main.WIDTH, Main.HEIGHT, BufferedImage.TYPE_INT_RGB);
		matrix = mainCamera.getMatrix();
		
		Graphics2D g = image.createGraphics();
		
		// Background
		{
			bg.renderTo(image, matrix);
			
		}
		
		// TRAPS
		{
			for (int i = 0; i < traps.size(); i ++) {
				traps.get(i).renderTo(image, matrix);
			}
		}
		
		// Player
		{
			player.renderTo(image ,matrix);
		}
		
		
		// Mid line
		{
			int lineStartY = (int) matrix.transform(0, 0).y;
			int lineEndY = (int) matrix.transform(Main.WIDTH, 0).y;
			g.setColor(Color.WHITE);
			g.drawLine(0, lineStartY, Main.WIDTH, lineEndY);
		}
		
		// TIME LEFT
		{
			g.setFont(MainMenu.font1);
			drawString("Time left: " + (int) ((10000 - timePassed) / 100) / 10d, Main.WIDTH / 2, 20, g, MainMenu.font1, true);
		}
		
		
		// FPS Counter
		{
			fps.renderTo(image, matrix);
			
		}
		// Power slide
		{
			slide.renderTo(image, matrix);
		}
		
		/*
		 * Draw to screen.
		 */
		Graphics2D destG = destination.createGraphics();
		destG.drawImage(image, (int) pos.x, (int) pos.y, null);
		
		renderTime = System.currentTimeMillis() - renderTime;
		// Debug
		{
			//drawString("Updating: " + (int)(updateTime / 100) / 10 + "\t, Rendering: " + (int)(renderTime / 100) / 10, 10, 30, destG, MainMenu.font1, false);
		}
	}
	
	private int[] getImageData(BufferedImage image)
	{
		return ((DataBufferInt) image.getRaster().getDataBuffer()).getData();
	}
	
	private Rectangle2D drawString(String str, int x, int y, Graphics2D g,  Font f, boolean center)
	{
		Rectangle2D r = getBounds(str, f);
		double newWidth = r.getWidth();
		double newHeight= r.getHeight();

		g.setFont(f);
		int yOffset = 0;
		int beginning = 0;
		int end = 0;
		while (beginning < str.length()) {
			// New line.
			end = str.indexOf('\n', end + 1);
			if (end == -1) end = str.length();
			String stringToRender = str.substring(beginning, end);
			Rectangle2D lineRect = getBounds(stringToRender, f);
			newWidth = Math.max(newWidth, lineRect.getWidth());
			newHeight += lineRect.getHeight();
			g.drawString(stringToRender, (float) (x - r.getX() - (center? lineRect.getWidth()/2 : 0)), (float) (y - r.getY() + yOffset));
			yOffset += lineRect.getHeight();
			beginning = end;
		}
		r.setRect(x, y, newWidth, newHeight);
		return r;
	}
	private Rectangle2D getBounds(String str, Font f)
	{
		return f.getStringBounds(str, new FontRenderContext(null, false, false));
	}
	
	
	private void applyOutline(BufferedImage image, int ARGB)
	{
		int[] data = getImageData(image);
		int[] preData = new int[data.length];
		System.arraycopy(data, 0, preData, 0, data.length);
		int w = image.getWidth();
		int h = image.getHeight();
		int i = 0;
		
		// Algorithm: Loop through pixels. If the pixel has any horizontal/vertical neighbors, fill it with color
		for (int y = 0; y < h; y ++) {
			for (int x = 0; x < w; x ++) {
				if (preData[i] == 0) {
					// If top neighbor
					if (i > w && preData[i - w] != 0) {
						data[i] = ARGB;
					}
					// If bottom neighbor
					else if (i < data.length - w && preData[i + w] != 0) {
						data[i] = ARGB;
					}
					// If left neighbor
					else if (x != 0 && preData[i - 1] != 0) {
						data[i] = ARGB;
					}
					// If right neighbor
					else if (x != w - 1 && preData[i + 1] != 0) {
						data[i] = ARGB;
					}
				}

				i ++;
			}
		}
	}

}
