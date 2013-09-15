package Menu;

import game.essential.Drawable;
import game.essential.Input;
import game.essential.Matrix2D;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.InputStream;

import LD27.GameObject;
import LD27.Main;


public class MainMenu extends GameObject implements Drawable {
	
	public boolean locked = false;
	
	private int score = - 1;
	
	public MainMenu(int score)
	{
		this.score = score;
	}
	public MainMenu()
	{
		
	}
	
	@Override
	public void update(long delta) {
		if (!locked) {
			if (Input.keyDown(10)) {
				Main.getInstance().startGame(this);
			}
		}
	}
	@Override
	public void renderTo(BufferedImage destination, Matrix2D matrix)
	{
		BufferedImage image = new BufferedImage(Main.WIDTH, Main.HEIGHT, BufferedImage.TYPE_INT_ARGB);
		
		Graphics2D g = image.createGraphics();
		g.setColor(new Color(0xDDDDFF));
		drawString("You are a Mercury Frog and they usually only have 10 seconds to live, but you want to live life to the fullest.\n"
				+ "According to relativity, the faster you run, the slower time will pass.\n"
				+ "Try to stay alive for the longest period of time possible by being in motion.\n"
				+ "Press D to run right, and hold SPACE to charge a jump. Hitting small weird red things will slow you down!\n"
				+ "Good luck!", Main.WIDTH / 2, 10, g, font1, true);
		g.setColor(new Color(0xAAFFAA));
		drawString("Press ENTER to start!", Main.WIDTH / 2, 240, g, font1, true);
		
		// PREV SCORE
		{
			if (score != - 1) {
				drawString("You actually survived for " + ((int) (score / 100) / 10) + " seconds!", 300, Main.HEIGHT / 2, g, font1, true);
			}
		}
		// BEST SCORE
		{
			if (Main.getInstance().highScore != -1) {
				drawString("Best score: " + ((int) (Main.getInstance().highScore / 100) / 10) + " seconds.", 700, Main.HEIGHT / 2, g, font1, true);
			}
		}
		
		// DRAW TO CANVAS
		Graphics2D destG = destination.createGraphics();
		destG.drawImage(image, (int) pos.x, (int) pos.y, null);
	}

	
	
	
	// Generic functions 
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
	private int[] getImageData(BufferedImage image)
	{
		return ((DataBufferInt) image.getRaster().getDataBuffer()).getData();
	}

	// STATIC!

	public static Font font = getFont("res/04b03.TTF").deriveFont(8f);
	public static Font font1 = getFont("res/04b03.TTF").deriveFont(16f);
	private static Font getFont(String name)
    {
        Font font = null;
	    try {
	      InputStream is = MainMenu.class.getResourceAsStream(name);
	      font = Font.createFont(Font.TRUETYPE_FONT, is);
	    } catch (Exception ex) {
	      ex.printStackTrace();
	      System.err.println(name + " not loaded.  Using serif font.");
	      font = new Font("serif", Font.PLAIN, 17);
	    }
	    return font;
    }
}
