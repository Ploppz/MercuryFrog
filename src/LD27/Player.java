package LD27;

import game.essential.Drawable;
import game.essential.Input;
import game.essential.Matrix2D;
import game.essential.Vector2d;

import java.awt.image.BufferedImage;

public class Player extends GameObject implements Drawable {

	private Animation idle = new Animation("dude/idle", 2, 300);
	private Animation walking = new Animation("dude/walk", 7, 40);
	private Animation jumping = new Animation("dude/jump2", 17, 80, 3);
	
	public int getJumpPhase()
	{
		return (int) jumping.getCurrentFrame();
	}
	
	public STATE state = STATE.IDLE;
	
	public Player()
	{
	}

	private boolean facingRight = true;
	
	private double acceleration = 0.015;
	
	@Override
	public void update(long delta) {

		double timeFactor = delta / Main.targetFrameDuration;
		
		double startVelX = vel.x;
		
		// UP
		if (Input.keyDown(38) || Input.keyDown(87)) {
			
		}
		// LEFT
		if (Input.keyDown(37) || Input.keyDown(65)) {
			facingRight = false;
			//vel.x -= 0.028;
		}
		// DOWN
		if (Input.keyDown(40) || Input.keyDown(83)) {
			
		}
		// RIGHT
		if (Input.keyDown(39) || Input.keyDown(68)) {
			facingRight = true;
			vel.x += acceleration * timeFactor;
		}
		pos.x += (startVelX + vel.x) / 2 * timeFactor;
		//pos.y += vel.y * timeFactor;
		
		if (state != STATE.JUMPING) {
			if (Input.keyDown(37) || Input.keyDown(65) || Input.keyDown(39) || Input.keyDown(68)) {
				state = STATE.WALKING;
			} else {
				state = STATE.IDLE;
				vel.x *= Math.pow(0.95, timeFactor);
			}
		}
		walking.frameDuration = (int) Math.abs(200 / vel.x);
		
	}
	public void jump(double progress)
	{
		jumping.setFrame(0);
		jumping.frameDuration = (int) progress;
		state = STATE.JUMPING;
		
		// event
		jumping.setDoneListener(this);
	}
	public void stopJumping()
	{
		state = STATE.IDLE;
		
		
		jumping.clearDoneListener();
	}

	@Override
	public void renderTo(BufferedImage image, Matrix2D matrix) {
		// Flip eventually
		Vector2d a = pos.clone();
		if (!facingRight) {
			matrix.offset( pos.x, 0);
			matrix.scale(-1, 1);
			a.x = 0;
		}
		
		if (state == STATE.IDLE) {
			idle.renderTo(image, matrix, a);
		} else if (state == STATE.WALKING) {
			walking.renderTo(image, matrix, a);
		} else if (state == STATE.JUMPING) {
			jumping.renderTo(image, matrix, a);
		}
	}
	
	
	
	public enum STATE {
		IDLE, WALKING, JUMPING;
	}
	
}
