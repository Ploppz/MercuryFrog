package game.essential;

/**
 * Not yet a descendant of any mathematical matrix class.
 */
public class Matrix2D {
	
	/*
	 * [a, c],
	 * [b, d]
	 * 
	 * x = x * a + y * c;
	 * y = x * b + y * d;
	 */
	public Matrix2D() {
		
	}
	/**
	 * 
	 * @param a		x-scale
	 * @param b		y-scew
	 * @param c		x-scew
	 * @param d		y-scale
	 * @param tx	translate x
	 * @param ty	translate y
	 */
	public Matrix2D(double a, double b, double c, double d, double tx, double ty)
	{
		this.a = a; this.b = b; this.c = c; this.d = d; this.tx = tx; this.ty = ty;
	}
	public Matrix2D clone()
	{
		return new Matrix2D(a, b, c, d, tx, ty);
	}
	
	/** x-scale */
	public double a = 1;
	/** y-scew */
	public double b = 0;
	/** x-scew */
	public double c = 0;
	/** y-scale */
	public double d = 1;
	
	/**
	 * @return Returns self.
	 */
	public Matrix2D scale(double scale)
	{
		a *= scale;
		d *= scale;
		return this;
	}
	public Matrix2D scale(double xFactor, double yFactor)
	{
		a *= xFactor;
		d *= yFactor;
		return this;
	}
	
	/**
	 * @return Returns self.
	 */
	public Matrix2D skewX(double skew)
	{
		c *= skew;
		return this;
	}
	
	/**
	 * @return Returns self.
	 */
	public Matrix2D skewY(double skew)
	{
		b *= skew;
		return this;
	}
	
	/**
	 * 
	 * @param angle In radians.
	 * @return Returns self.
	 */
	public Matrix2D rotate(double angle)
	{
		double co = Math.cos(angle);
		double so = Math.sin(angle);
		a =   co;
		b =   so;
		c = - so;
		d =   co;
		return this;
	}
	
	public double tx = 0;
	public double ty = 0;
		
	
	/** Overwrites the matrix
	 *  @return Returns self.
	 */
	public Matrix2D translate(Vector2d transform)
	{
		tx = transform.x;
		ty = transform.y;
		return this;
	}
	
	
	
	/** Overwrites the matrix
	 *  @return Returns self. */
	public Matrix2D translate(double tx, double ty)
	{
		this.tx = tx;
		this.tx = ty;
		return this;
	}
	
	
	
	/** Offsets the matrix
	 *  @return Returns self. */
	public Matrix2D offset(Vector2d offset)
	{
		tx += offset.x;
		ty += offset.y;
		return this;
	}
	
	
	
	/** Offsets the matrix
	 *  @return Returns self. */
	public Matrix2D offset(double offsetX, double offsetY)
	{
		tx += offsetX;
		ty += offsetY;
		return this;
	}
	
	
	/**
	 * Scales the offset before applying it.
	 * @return Returns self.
	 */
	public Matrix2D scaledOffset(Vector2d offset)
	{
		tx += offset.x * a;
		ty += offset.y * d;
		return this;
	}
	
	
	/**
	 * Scales the offset before applying it.
	 * @return Returns self.
	 */
	public Matrix2D scaleAndOffset(double offsetX, double offsetY)
	{
		tx += offsetX * a;
		ty += offsetY * d;
		return this;
	}
	
	// Derive x/y values
	/**
	 * @param source The vector to transform using this matrix.
	 * @return Returns the transformed vector.
	 */
	public Vector2d transform(Vector2d source)
	{
		return new Vector2d(source.x * a + source.y * c + tx, source.x * b + source.y * d + ty);
	}
	
	public Vector2d transform(double x, double y)
	{
		return new Vector2d(x * a + y * c + tx, x * b + y * d + ty);
	}
	
	/**
	 * @return Returns value * a (scale-x);
	 */
	public double scaledX(double value)
	{
		return value * a;
	}
	/**
	 * @return Returns value * d (scale-y);
	 */
	public double scaledY(double value)
	{
		return value * d;
	}
	public Vector2d getScaled(Vector2d v)
	{
		return new Vector2d(v.x * a, v.y * d);
	}
	
	
	
	private void multiply(double a, double b, double c, double d, double tx, double ty)
	{
		double a_ = this.a * a + this.c * b;
		double b_ = this.b * a + this.d * b;
		double c_ = this.a * c + this.c * d;
		double d_ = this.b * c + this.d * d;
		
		double tx_ = this.a * tx + this.c * ty + tx;
		double ty_ = this.b * tx + this.d * ty + ty;
		
		this.a = a_; this.b = b_; this.c = c_; this.d = d_; this.tx = tx_; this.ty = ty_;
	}
	public void multiply(Matrix2D m) {
		multiply(m.a, m.b, m.c, m.d, m.tx, m.ty);
	}
}
