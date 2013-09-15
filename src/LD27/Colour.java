package LD27;

public class Colour {
	
	private float alpha = 1;
	private int red = 0xFF;
	private int green = 0xFF;
	private int blue = 0xFF;
	
	public int getRGB()
	{
		return (red & 0xFF) << 16 | (green & 0xFF) << 8 | blue & 0xFF;
	}
	public Colour setRGB(int rgb)
	{
		blue = rgb & 0xFF;
		rgb >>= 8;
		green = rgb & 0xFF;
		rgb >>= 8;
		red = rgb & 0xFF;
		return this;
	}
	public int getARGB()
	{
		return (int)(alpha * 0xFF) << 24 | (red & 0xFF) << 16 | (green & 0xFF) << 8 | blue & 0xFF;
	}
	
	public Colour(float alpha, int RGB) {
		this.alpha = alpha;
		red = (RGB >> 16) & 0xFF;
		green = (RGB >> 8) & 0xFF;;
		blue = RGB & 0xFF;;
	}
	public Colour(int RGB) {
		red = (RGB >> 16) & 0xFF;
		green = (RGB >> 8) & 0xFF;;
		blue = RGB & 0xFF;;
	}
	public Colour(int red, int green, int blue) {
		this.red = red;
		this.green = green;
		this.blue = blue;
	}
	public Colour(float alpha, int red, int green, int blue) {
		this.alpha = alpha;
		this.red = red;
		this.green = green;
		this.blue = blue;
	}
	public Colour()
	{
		
	}
	
	public Colour clone()
	{
		return new Colour(alpha, red, green, blue);
	}
	
	
	
	public Colour blend(Colour rgb)
	{
		float a = alpha;
		float a2 = 1 - a;
		red = (int) (red * a + rgb.getRed() * a2) & 0xFF;
		green = (int) (green * a + rgb.getGreen() * a2) & 0xFF;
		blue = (int) (blue * a + rgb.getBlue() * a2) & 0xFF;
		return this;
	}
	public Colour blend2(Colour rgb)
	{
		double a = 1 - alpha;
		setRed((int) (red * alpha + rgb.getRed() * a));
		green = (int) (rgb.getRed() / 2 / alpha);
		//blue = (int) (Math.random() * 0xAA);
		return this;
	}
	public Colour blend(int rgb)
	{
		return blend(new Colour(rgb));
	}
	
	
	// Getters and setters yo
	public Colour setAlpha(float value) 
	{
		if (value < 0) value = 0;
		else if (value > 1) value = 1;
		alpha = value;
		return this;
	}
	public Colour setRed(int value)
	{
		if (value < 0) value = 0;
		else if (value > 0xFF) value = 0xFF;
		red = value;
		return this;
	}
	public Colour setGreen(int value)
	{
		if (value < 0) value = 0;
		else if (value > 0xFF) value = 0xFF;
		green = value; 
		return this;
	}
	public Colour setBlue(int value) 
	{
		if (value < 0) value = 0;
		else if (value > 0xFF) value = 0xFF;
		blue = value;
		return this;
	}
	/*
	 * 
	 */
	public float getAlpha()
	{
		return alpha;
	}
	public int getRed() {
		return red;
	}
	public int getGreen()
	{
		return green;
	}
	public int getBlue()
	{ 
		return blue; 
	}
	/** Get the average between the 3 colour components */
	public int getAverage()
	{
		return (red + green + blue) / 3;
	}
	/*
	 * ARITHMETICS
	 */

	public Colour multiply(double factor)
	{
		setRed((int) (red * factor));
		setGreen((int) (green * factor));;
		setBlue((int) (blue * factor));
		return this;
	}
	
	/** Add the offset to all the components */
	public Colour add(int offset)
	{
		addRed(offset);
		addGreen(offset);
		addBlue(offset);
		return this;
	}
	public Colour addRed(int offset)
	{
		setRed(red + offset);
		return this;
	}
	public Colour addGreen(int offset)
	{
		setGreen(green + offset);
		return this;
	}
	public Colour addBlue(int offset)
	{
		setBlue(blue + offset);
		return this;
	}
	
	
	
	// mhm
	public String toString()
	{
		//return "A: " + toHex(alpha) + "\tR: " + toHex(red) + "\tG: " + toHex(green) + "\tB: " + toHex(blue);
		return sub(Float.toString(alpha)) + " x " + Integer.toString(getRGB(), 16);
	}
	private String sub(String s)
	{
		if (s.length() > 5) {
			return s.substring(0, 5);
		}
		return s;
	}
}
