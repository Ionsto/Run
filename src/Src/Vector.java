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
	public float Cross(Vector b)
	{
		return (X * b.X) + (Y * b.Y);
	}
	public float Normalise()
	{
		float Mag = (float) Math.sqrt((X*X)+(Y*Y));
		X = X/Mag;
		Y = Y/Mag;
		return Mag;
	}
	public Vector Perpendicular()
	{
		return new Vector(-Y,X);
	}
	public Vector Negitive()
	{
		return new Vector(-X,-Y);
	}
}
