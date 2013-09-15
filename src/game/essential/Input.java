package game.essential;

import java.awt.Component;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.PointerInfo;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.SwingUtilities;
import javax.swing.event.MouseInputAdapter;

public class Input {
	
	
	///////////////////////////////////////////////\
	//////////////////// STATIC ///////////////////\
	
	private static boolean[] keys;
	
	private static ArrayList<KeyListener> keyListeners = new ArrayList<KeyListener>();
	public static void addKeyListener(KeyListener listener)
	{
		keyListeners.add(listener);
	}
	public static void removeKeyListener(KeyListener listener)
	{
		keyListeners.remove(listener);
	}
	
	// MOUSE INFO
	
	private static Point mousePosition = new Point();
	
	public static Point getMousePos()
	{
		return mousePosition;
	}
	
	/** Get the point in which the left mouse button was last pressed */
	public static Point leftMousePressed = new Point();
	/** Get the point in which the left mouse button was last released */
	public static Point leftMouseReleased = new Point();
	
	/** Get the point in which the right mouse button was last pressed */
	public static Point rightMousePressed = new Point();
	/** Get the point in which the right mouse button was last released */
	public static Point rightMouseReleased = new Point();
	
	private static boolean leftMouseDown = false;
	public static boolean leftMouseIsDown()
	{
		return leftMouseDown;
	}
	private static boolean rightMouseDown = false;
	public static boolean rightMouseIsDown()
	{
		return rightMouseDown;
	}
	
	// KEY CONSTANTS

	public static final int SPACE = 32;
	public static final int ENTER = 10;
	public static final int ESCAPE = 27;
	
	public static final int LEFT = 37;
	public static final int UP = 38;
	public static final int RIGHT = 39;
	public static final int DOWN = 40;
	
	public static final int W = 87;
	public static final int A = 65;
	public static final int S = 83;
	public static final int D = 68;
	
	// METHODS
	
	public static boolean keyDown(int keyCode)
	{
		if (keyCode < keys.length) {
			return keys[keyCode];
		}
		return false;
	}
	
	
	public static void initialize(Component canvas) {
		keys = new boolean[402];
		
		// MAKE KEY EVENT LISTENER
		
		KeyAdapter keyAdapter = new KeyAdapter() {

			@Override
			public void keyPressed(KeyEvent e)
			{
				//System.out.println("Key code: " + e.getKeyCode());
				if (keys[e.getKeyCode()] || e.getKeyCode() >= 402) return;
				
				KeyListener kl;
				for (int i = keyListeners.size() - 1; i >= 0; i --) {
					kl = keyListeners.get(i);
					kl.keyDown(e);
				}
				
				
				keys[e.getKeyCode()] = true;
			}
			@Override
			public void keyReleased(KeyEvent e)
			{
				if (!keys[e.getKeyCode()] || e.getKeyCode() >= 402) return;
				
				KeyListener kl;
				for (int i = keyListeners.size() - 1; i >= 0; i --) {
					kl = keyListeners.get(i);
					kl.keyUp(e);
				}
				keys[e.getKeyCode()] = false;
			}
		};
		
		// MAKE MOUSE EVENT LISTENER.

		MouseInput mouseAdapter = new MouseInput();
		
		//
		
		canvas.addKeyListener(keyAdapter);
		canvas.addMouseListener(mouseAdapter);
		canvas.addMouseMotionListener(mouseAdapter);
	}
	
	private static class MouseInput extends MouseInputAdapter {

		// MOVE
		@Override
		public void mouseMoved(MouseEvent e) {
			setMousePosition(e);
		}
		@Override
		public void mouseDragged(MouseEvent e) {
			setMousePosition(e);
		}
		private void setMousePosition(MouseEvent e)
		{
			PointerInfo pointerInfo = MouseInfo.getPointerInfo();
			Point point = new Point(pointerInfo.getLocation());
			SwingUtilities.convertPointFromScreen(point, e.getComponent());
			mousePosition.x = (int) point.getX();
			mousePosition.y = (int) point.getY();
			
		}
		// PRESS/RELEASE
		@Override
		public void mousePressed(MouseEvent e) {
			if (SwingUtilities.isLeftMouseButton(e)) {
				leftMouseDown = true;
				leftMousePressed.x = (int)e.getPoint().getX();
				leftMousePressed.y = (int)e.getPoint().getY();
			} else if (SwingUtilities.isRightMouseButton(e)) {
				rightMouseDown = true;
				rightMousePressed.x = (int)e.getPoint().getX();
				rightMousePressed.y = (int)e.getPoint().getY();
			}
		}
		@Override
		public void mouseReleased(MouseEvent e) {
			if (SwingUtilities.isLeftMouseButton(e)) {
				leftMouseDown = false;
				leftMouseReleased.x = (int)e.getPoint().getX();
				leftMouseReleased.y = (int)e.getPoint().getY();
			} else if (SwingUtilities.isRightMouseButton(e)) {
				rightMouseDown = false;
				rightMouseReleased.x = (int)e.getPoint().getX();
				rightMouseReleased.y = (int)e.getPoint().getY();
			}
		}
		
		// ENTER/EXIT
		@Override
		public void mouseEntered(MouseEvent e) {
			
		}
		@Override
		public void mouseExited(MouseEvent e) {
			
		}
		
	}

}
