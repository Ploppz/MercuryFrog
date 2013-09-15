package LD27;

import game.essential.Matrix2D;
import game.essential.Vector2d;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Trap extends GameObject {
	
	private double rotation = 0;
	public double rotationSpeed = 0;
	
	public boolean functioning = true;
	
	public Trap(double x)
	{
		pos.x = x;
		pos.y = -8;
	}
	
	public void renderTo(BufferedImage image, Matrix2D matrix)
	{
		Vector2d screenPos = matrix.transform(pos);
		
		Graphics2D g = image.createGraphics();
		g.translate(screenPos.x, screenPos.y);
		g.scale(matrix.a, matrix.d);
		g.rotate(rotation);
		g.drawImage(box, 0, 0, null);
		
		rotation += rotationSpeed;
		pos.x += vel.x;
		pos.y += vel.y;
		if (!functioning) {
			vel.y += 0.1;
		}
	}
	
	
	private static BufferedImage box;
	static {
		try {
			box = ImageIO.read(PowerSlide.class.getResourceAsStream("misc/box.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
