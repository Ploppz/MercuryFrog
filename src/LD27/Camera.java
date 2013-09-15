package LD27;

import game.essential.Matrix2D;
import game.essential.Vector2d;

import java.awt.Rectangle;
import java.util.ArrayList;

public class Camera {
	
	public Vector2d position = new Vector2d();
	public Vector2d velocity = new Vector2d();
	public Vector2d size = new Vector2d();
	
	
	private double zoom = 1;
	
	public ArrayList<GameObject> followList = new ArrayList<GameObject>();
	
	public Camera(double width, double height)
	{
		size.x = width;
		size.y = height;
		
	}
	
	// Get the matrix
	public Matrix2D getMatrix()
	{
		return new Matrix2D(zoom, 0, 0, zoom, size.x / 2 - this.position.x * zoom, size.y / 2 - this.position.y * zoom);
	}
	
	
	private final int quotient = 10;
	private final int cameraMargin = 90;
	
	public void update()
	{
		if (followList.size() == 0) return;
		
// FINDING THE BOUNDING RECTANGLE {
		Rectangle bound = new Rectangle(Integer.MAX_VALUE, Integer.MAX_VALUE, 0, 0);

		// Finding the minimum position
		for (GameObject toFollow : followList) {
			if (toFollow.pos.x < bound.x) {
				bound.x = (int) (toFollow.pos.x - 5);
			}
			if (toFollow.pos.y < bound.y) {
				bound.y = (int) (toFollow.pos.y - 5);
			}
			
		}
		
		// Finding the maximum size
		for (GameObject toFollow : followList) {
			int relativeX = (int) ((toFollow.pos.x + 5) - bound.x); // relative to bound's x
			int relativeY = (int) ((toFollow.pos.y + 5) - bound.y); // ...

			if (relativeX > bound.width) {
				bound.width = relativeX;
			}
			if (relativeY > bound.height) {
				bound.height = relativeY;
			}
		}
//	}
		// Target position:
		Vector2d targetPosition = new Vector2d(bound.getCenterX(), bound.getCenterY());
		
		position.x += (targetPosition.x - position.x) / quotient;
		position.x += followList.get(0).vel.x;
		if (followList.get(0).vel.x > 1) {
			position.x += 30;
		} else if (followList.get(0).vel.x < - 1) {
			position.x -= 30;
		} else {
			
		}
		//
		position.y += (targetPosition.y - position.y) / quotient;

		//Target zoom!!! To find this: what zoom do we need to make the bound rectangle fit screen? ( + a little margin yay!!! :D)
		double targetZoom;
		
		/*// Apply margins
		bound.width += cameraMargin * 2;
		bound.height += cameraMargin * 2;
		
		if ((double) bound.width / (double) Game.WIDTH > (double) bound.height / (double) Game.HEIGHT) {
			targetZoom = 1/ ((double) bound.width / (double) Game.WIDTH);
		} else {
			targetZoom = 1/ ((double) bound.height / (double) Game.HEIGHT);
		}*/
		/*if (followList.size() > 0) {
			targetZoom = (200 / 5);
			zoom += (targetZoom - zoom) / (quotient * 2);
		}*/

	}
	
	
	// GETTERS AND SETTERS
	
	public void setZoom(double value)
	{
		if (value <= 0) return;
		zoom = value;
	}
	public double getZoom()
	{
		return zoom;
	}

	
	
	public Vector2d getScreenPos(Vector2d position)
	{
		return new Vector2d(size.x / 2 + (position.x - this.position.x) * zoom,
							size.y / 2 + (position.y - this.position.y) * zoom);
	}
	public Vector2d getScaledSize(Vector2d size)
	{
		return size.times(zoom);
	}
	public double getScaledSize(double sizeComponent)
	{
		return sizeComponent * zoom;
	}

}
