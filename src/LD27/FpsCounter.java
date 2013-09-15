package LD27;

import game.essential.Drawable;
import game.essential.Matrix2D;
import game.essential.Vector2d;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import Menu.MainMenu;

public class FpsCounter implements Drawable {

	public Vector2d position = new Vector2d();
	
	public FpsCounter(double x, double y)
	{
		position.x = x;
		position.y = y;
	}
	
	private double fps = 0;
	
	@Override
	public void update(long delta) {
		fps = 1000000000 / delta;
	}

	@Override
	public void renderTo(BufferedImage image, Matrix2D matrix) {
		Graphics2D g = image.createGraphics();
		g.setFont(MainMenu.font);
		g.setColor(Color.WHITE);
		g.drawString("FPS: " + fps, (int)position.x, (int)position.y);
	}
	
	
	
}
