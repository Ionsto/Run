package Src;

import java.util.ArrayList;
import java.util.List;

public class CollModel {
	public List<CollModel> children = new ArrayList<CollModel>();
	public int Depth = 1;//The quantity of children layers only relevent on top node
	public float X = 0;//Mid point
	public float Y = 0;
	public float SX = 0;//Full Size
	public float SY = 0;
	public CollModel(float x,float y,float sx,float sy)//half sx
	{
		X = x;
		Y = y;
		SX = sx * 2;
		SY = sy * 2;
	}
	public boolean AABB(CollModel model,int depth,Entity a,Entity b)
	{
		if(Math.abs(this.X - model.X + (a.PosX - b.PosX)) < this.SX + model.SX)
		{
			if(Math.abs(this.Y - model.Y + (a.PosY - b.PosY)) < this.SY + model.SY)
			{
				return true;
			}
		}
		return false;
	}
}
