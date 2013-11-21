package Src;

import java.util.ArrayList;
import java.util.List;

public class CollBox {
	public List<CollBox> boxes = new ArrayList<CollBox>();
	public boolean MainNode = false;;//The quantity of children layers only relevent on top node
	public float X = 0;//Mid point
	public float Y = 0;
	public float SX = 0;//Full Size
	public float SY = 0;
	public CollBox(float x,float y,float sx,float sy)//half sx
	{
		X = x;
		Y = y;
		SX = sx * 2;
		SY = sy * 2;
	}
	public CollBox(CollBox box)//half sx
	{
		MainNode = true;
		AddBox(box);
	}
	public Vector AABB(CollBox modela,CollBox modelb,Entity a,Entity b)
	{
		if(Math.abs(modela.X - modelb.X + (a.Pos.X - b.Pos.X)) < modela.SX + modelb.SX)
		{
			if(Math.abs(modela.Y - modelb.Y + (a.Pos.Y - b.Pos.Y)) < modela.SY + modelb.SY)
			{
				float PenX = modela.SX + modelb.SX - Math.abs(modela.X - modelb.X + (a.Pos.X - b.Pos.X));
				float PenY = modela.SY + modelb.SY - Math.abs(modela.Y - modelb.Y + (a.Pos.Y - b.Pos.Y));
				return new Vector(PenX,PenY);
			}
		}
		return null;
	}
	public Vector Collide(Entity a,Entity b)//A is this's (long lost) parent
	{
		Vector Coll = null;
		//All boxes
		if(AABB(this,b.CollModel,a,b)!=null)//Prelim
		{
			for(int i = 0;i< boxes.size();++i)
			{
				for(int is = 0;is< b.CollModel.boxes.size();++is)
				{
					Coll = AABB(a.CollModel.boxes.get(i),b.CollModel.boxes.get(is),a,b);
					if(Coll != null)
					{
						return Coll;
					}
				}
			}
		}
		return null;
	}
	public void AddBox(CollBox mod)
	{
		if(mod.MainNode)
		{
			this.boxes.addAll(mod.boxes);
		}
		else
		{
			this.boxes.add(mod);
		}
		if(mod.X+mod.SX > mod.SX)
		{
			mod.SX = mod.X+mod.SX;
		}
		if(mod.Y+mod.SY > mod.SY)
		{
			mod.SY = mod.Y+mod.SY;
		}
	}
	public void AddBox(CollBox mod,Entity a,Entity b)
	{
		if(mod.MainNode)
		{
			for(int i = 0;i< mod.boxes.size();++i)
			{
				mod.boxes.get(i).X += (b.Pos.X - a.Pos.X);
				mod.boxes.get(i).Y += (b.Pos.Y - a.Pos.Y);
			}
		}
		else
		{
			mod.X += (b.Pos.X - a.Pos.X);
			mod.Y += (b.Pos.Y - a.Pos.Y);
		}
		this.boxes.add(mod);
	}
}
