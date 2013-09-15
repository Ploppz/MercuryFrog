package game.essential;



import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class ModelList<T extends Drawable> extends ArrayList<T> implements Drawable  {

	 //ArrayList methods
	
	@Override
	public boolean add(T e)
	{	
		if (locked) return false;
		if (e instanceof KeyListener) Input.addKeyListener((KeyListener) e);
		return super.add(e);
	}
	
	@Override
	public boolean remove(Object o)
	{
		if (locked) return false;
		if (o instanceof KeyListener) {
			Input.removeKeyListener((KeyListener) o);
		}
		return super.remove(o);
	}
	

	@Override
	public void clear()
	{
		if (locked) return;
		while(size() != 0) {
			remove(0);
		}
	}
	
	boolean locked = false;
	public boolean locked() { return locked; }
	public void lock()
	{
		locked = true;
	}
	public void unlock()
	{
		locked = false;
	}
	
	//// Drawable methods
	

	@Override
	public void update(long delta) {
		for (int i = 0; i < size(); i ++)
		{
			get(i).update(delta);
		}
	}
	
	@Override
	public void renderTo(BufferedImage image, Matrix2D matrix)
	{
		for (int i = 0; i < size(); i ++)
		{
			get(i).renderTo(image, matrix);
		}
	}



}
