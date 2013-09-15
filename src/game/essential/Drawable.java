package game.essential;

import java.awt.image.BufferedImage;

public interface Drawable {
	
	public abstract void update(long delta);
	/** Send an unconfigured new Camera if you don't want any transformation */
	public abstract void renderTo(BufferedImage image, Matrix2D matrix);
	
}
