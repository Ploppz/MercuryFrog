package LD27;

import game.essential.Drawable;
import game.essential.Input;
import game.essential.Matrix2D;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class PowerSlide extends GameObject implements Drawable {

	public int width = 150;
	
	public double progress = 0;
	
	private double progressSpeed = 1;
	
	public PowerSlide(double x, double y)
	{
		pos.x = x;
		pos.y = y;
	}
	
	private Player releaseListener;
	public void setReleaseListener(Player p)
	{
		releaseListener = p;
	}
	public void clearReleaseListener()
	{
		releaseListener = null;
	}
	
	private boolean spaceDown = false;
	
	@Override
	public void update(long delta) {
		
		double timeFactor = (delta) / Main.targetFrameDuration;
		System.out.println(timeFactor);
		// SPACE DOWN
		if (Input.keyDown(32)) {
			spaceDown = true;
			progressSpeed += 0.05 * timeFactor;
		} else {
			if (spaceDown && releaseListener != null) {
				releaseListener.jump(progress);
			}
			spaceDown = false;
			progressSpeed = 1;
			progress *= Math.pow(0.8, timeFactor);
		}
		
		progress += progressSpeed * timeFactor;
		
		if (progress < 0) progress = 0;
		if (progress > width) progress = width;
	}

	@Override
	public void renderTo(BufferedImage image, Matrix2D matrix) {
		int w = getActualWidth();
		
		Graphics2D g = image.createGraphics();
		g.drawImage(handle, (int) pos.x - w/2, (int) pos.y, null);
		for (int i = 0; i < width; i ++) {
			if (i < progress) {
				// Draw the "progress" image - the power_on image
				g.drawImage(on, (int) pos.x - width / 2 + i, (int) pos.y, null);
			} else {
				// Draw the power_off image
				g.drawImage(off, (int) pos.x - width / 2 + i, (int) pos.y, null);
			}
		}
		g.translate((int) (pos.x + w / 2), 0);
		g.scale(-1, 1);
		g.drawImage(handle, 0, (int) pos.y, null);
	}
	
	private int getActualWidth()
	{
		return 14 + width;
	}
	
	
	private static BufferedImage handle;
	private static BufferedImage on;
	private static BufferedImage off;
	static {
		// LOAD GRAPHICS
		try {
			handle = ImageIO.read(PowerSlide.class.getResourceAsStream("misc/handle.png"));
			on = ImageIO.read(PowerSlide.class.getResourceAsStream("misc/power_on.png"));
			off = ImageIO.read(PowerSlide.class.getResourceAsStream("misc/power_off.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
