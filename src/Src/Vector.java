package Src;

public class Vector {
	public float X = 0;
	public float Y = 0;
	public Vector(float x,float y)
	{
		this.X = x;
		this.Y = y;
	}
	//Helping me since 1983
	public Vector Cross(float x,float y)
	{
		return new Vector(-y,x);
	}
	public Vector Perp(float x,float y)
	{
		return new Vector(-y,x);
	}
}
