package game.essential;

public class Vector2d {
	public double x = 0;
	public double y = 0;
	
	public Vector2d() {
		
	}
	public Vector2d(double x, double y)
	{
		this.x = x;
		this.y = y;
	}
	public Vector2d clone()
	{
		return new Vector2d(x, y);
	}
	
	public double length()
	{
		return Math.sqrt(x * x + y * y);
	}
	public double angle()
	{
		return Math.atan2(y, x);
	}
	
	public Vector2d normalize()
	{
		double l = length();
		x = x / l;
		y = y / l;
		return this;
	}
	
	public double dot(Vector2d factor)
	{
		return x * factor.x + y * factor.y;
	}
	public double dot()
	{
		return x * x + y * y;
	}
	/** will return self */
	public Vector2d add(Vector2d offset)
	{
		x += offset.x;
		y += offset.y;
		return this;
	}
	public Vector2d add(double x, double y)
	{
		this.x += x;
		this.y += y;
		return this;
	}
	public Vector2d substract(Vector2d offset)
	{
		x -= offset.x;
		y -= offset.y;
		return this;
	}
	public Vector2d substract(double x, double y)
	{
		this.x -= x;
		this.y -= y;
		return this;
	}
	
	public Vector2d times(Vector2d factor)
	{
		x *= factor.x;
		y *= factor.y;
		return this;
	}
	public Vector2d times(double factor)
	{
		x *= factor;
		y *= factor;
		return this;
	}
	
	
	public Vector2d rotate90CC()
	{
		double _x = x;
		x = -y;
		y = _x;
		return this;
	}
	public Vector2d rotate90CCW()
	{
		double _x = x;
		x = y;
		y = - _x;
		return this;
	}
	
	// toString
	public String toString()
	{
		return "[" + rounded(x, 4) + ", " + rounded(y, 4) + "]";
	}
	
	public String toStringRounded()
	{
		return "[" + Math.round(x) + ", " + Math.round(y) + "]";
	}
	public String rounded(double n, int numDecimals)
	{
		String s = Double.toString(n);
		if (s.indexOf('.') != -1) {
			while (s.substring(s.indexOf('.'), s.length()).length() < 5) {
				s += "0";
			}
			s = s.substring(0, Math.min(s.indexOf('.') + numDecimals + 1, s.length()));
		}
		if (n >= 0) {
			s = " " + s;
		}
		return s;
	}
}
