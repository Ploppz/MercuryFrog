package LD27;

import game.essential.Matrix2D;
import game.essential.Vector2d;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

public class Background {
	
	BufferedImage image;
	
	
	public Background(int width, int height)
	{
		image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		values = new double[width];
		// Init values
		{
			for (int i = 0; i < width; i ++) {
				if (i > 0) {
					values[i] = Math.random() * randomFactor + values[i - 1] * (1 - randomFactor);
				} else {
					values[i] = Math.random();
				}
			}
		}
	}
	
	// background stats
	double randomFactor = 0.1;
	double blueFactor = 1;
	double blueSpeed = - 0.01;
	
	double roundTo = 30;
	
	double[] values;
	double lastUpdated = 0;
	
	public void renderTo(BufferedImage image, Matrix2D matrix)
	{
		Vector2d screenPos = matrix.transform(0, 0);
		double delta = lastUpdated - screenPos.x;
		if (Math.abs(delta) > 1) {
			shift((int)delta);
			lastUpdated -= delta;
		}
		
		// RENDER
		Colour col = new Colour(0x6699FF);
		int midOnScreen = (int) matrix.transform(0, 0).y;
		int distanceFromMid;
		
		int[] data = getImageData(this.image);
		int i = 0;
		Colour newCol = new Colour();
		for (int y = 0; y < image.getHeight(); y ++) {
			for (int x = 0; x < image.getWidth(); x ++) {
				distanceFromMid = (int) (Math.abs(y - midOnScreen) / values[x]);
					/*int a = x % 30;
					if (a > 15) a = 30 - a;
					distanceFromMid += a;*/
				if (distanceFromMid >= 1) {
					double factor = 30d / ((int)(distanceFromMid / roundTo) * roundTo);
					if (factor > 1) factor = 1;
					//newCol = col.clone().multiply(factor);
					newCol.setRed((int) (col.getRed() * factor));
					newCol.setBlue((int) (col.getBlue() * factor));
					newCol.setGreen((int) (col.getGreen() * factor));
					newCol.setBlue((int) (newCol.getBlue() * blueFactor));
					data[i] = newCol.getRGB();
					
				} else {
					data[i] = col.getRGB();
				}
				i ++;
			}
		}
		int[] canvasData = getImageData(image);
		System.arraycopy(data, 0, canvasData, 0, data.length);
		
		updateRandomFactor();
	}
	
	private void updateRandomFactor()
	{

		randomFactor += (Math.random() - 0.5) / 10000;
		if (randomFactor < 0) randomFactor = 0;
		if (randomFactor > 1.5) randomFactor = 1.5;
		
		//
		
		blueSpeed += (0.5 - blueFactor) / 15000;
		blueSpeed += (Math.random() - 0.5) / 300;
		blueFactor += blueSpeed;
		if (blueSpeed > 1) {
			blueSpeed *= 0.95;
		}
		
		//
		
		if (Math.random() > 0.9) roundTo += Math.random() * 2 - 1;
	}
	
	private void shift(int delta)
	{
		double[] old = new double[values.length];
		System.arraycopy(values, 0, old, 0, values.length);
		
		int oldIndex = delta;
		for (int i = 0; i < values.length; i ++)
		{
			if (oldIndex >= 0 && oldIndex < values.length) {
				values[i] = old[oldIndex];
			}
			
			oldIndex ++;
		}
		if (delta < 0) {
			for (int i = Math.abs(delta) - 1; i >= 0; i --) {
				values[i] = Math.random() * randomFactor + values[i + 1] * (1 - randomFactor);
			}
		} else {
			for (int i = values.length - delta; i < values.length; i ++) {
				values[i] = Math.random() * randomFactor + values[i - 1] * (1 - randomFactor);
			}
		}
	}
	
	
	private int[] getImageData(BufferedImage image)
	{
		return ((DataBufferInt) image.getRaster().getDataBuffer()).getData();
	}
	
}
