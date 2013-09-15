package LD27;

import game.essential.Matrix2D;
import game.essential.Vector2d;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Animation {
	
	BufferedImage[] frames;
	
	public int frameDuration = 1;
	
	private double currentFrame = 0;
	public void setFrame(double value)
	{
		currentFrame = value;
		lastRendered = System.currentTimeMillis();
	}
	public double getCurrentFrame()
	{
		return currentFrame;
	}
	
	public void startFromBeginning()
	{
		currentFrame = 0;
	}
	/**
	 * @param frames Number of frames.
	 * @param frameDuration	In milliseconds.
	 */
	public Animation(String folder, int frames, int frameDuration)
	{
		this(folder, frames, frameDuration, 0);
	}
	public Animation(String folder, int frames, int frameDuration, int offset)
	{
		this.frameDuration = frameDuration;
		this.frames = new BufferedImage[frames - offset];
		// Load all the sources

		for (int i = 1; i <= this.frames.length; i ++) {
			System.out.println(folder + "/" + i + ".png");
			try {
				this.frames[i - 1] = ImageIO.read(Animation.class.getResourceAsStream(folder + "/" + (i + offset) + ".png"));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	private long lastRendered = System.currentTimeMillis();
	public void renderTo(BufferedImage image, Matrix2D matrix, Vector2d offset)
	{
		

		Vector2d screenPos = matrix.transform(offset.clone().substract(frames[0].getWidth()/2, frames[0].getHeight()));
		
		long time = System.currentTimeMillis();
		long sinceLast = time - lastRendered;
		currentFrame += (double) sinceLast / (double) frameDuration;
		if (doneListener != null && currentFrame >= frames.length) {
			doneListener.stopJumping();
		}
		currentFrame %= frames.length;
		lastRendered = time;
		
		Graphics2D g = image.createGraphics();
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		g.translate(screenPos.x, screenPos.y);
		g.scale(matrix.a, matrix.d);
		
		g.drawImage(frames[(int) currentFrame], 0, 0, null);
		
	}
	
	private Player doneListener;
	
	public void setDoneListener(Player p)
	{
		doneListener = p;
	}
	public void clearDoneListener()
	{
		doneListener = null;
	}
	
}
