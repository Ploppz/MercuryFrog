package game.essential;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.util.ArrayList;



public class Monitor extends Canvas
{
	private BufferedImage image = null;
	
	
	private int width = 0;
	private int height = 0;
	public int getWidth()
	{
		return width;
	}
	public int getHeight()
	{
		return height;
	}
	
	public Monitor(int width, int height)
	{
		this.width = width;
		this.height = height;
		image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
	}
	
	
	public void render()
	{
		BufferStrategy bs = getBufferStrategy();
		if (bs == null) {
			createBufferStrategy(3);
			return;
		}
		//
		
		for (Drawable a : models) {
			a.renderTo(image, new Matrix2D());
		}
		//
		Graphics canvas = bs.getDrawGraphics();
		canvas.drawImage(image, 0, 0, width, height, this);
		
		canvas.dispose();
		bs.show();
	}
	public void clear(Color background)
	{
		Graphics g = image.getGraphics();
		g.setColor(background);
		g.fillRect(0, 0, width, height);
	}
	
	public ArrayList<Drawable> models = new ArrayList<Drawable>();
}
