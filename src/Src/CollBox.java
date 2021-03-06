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
	public int CollidedWith = 0;
	public int Start = 0;
	public int Length = 0;
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
		if(Math.abs((modela.X - modelb.X) + (a.Pos.X - b.Pos.X)) < modela.SX + modelb.SX)
		{
			if(Math.abs((modela.Y - modelb.Y) + (a.Pos.Y - b.Pos.Y)) < modela.SY + modelb.SY)
			{
				float PenX = modela.SX + modelb.SX - Math.abs((modela.X - modelb.X) + (a.Pos.X - b.Pos.X));
				float PenY = modela.SY + modelb.SY - Math.abs((modela.Y - modelb.Y) + (a.Pos.Y - b.Pos.Y));
				return new Vector(PenX,PenY);
			}
		}
		return null;
	}
	public Vector AABBP(Entity a,Vector point)
	{
		if(Math.abs((this.X) + (a.Pos.X - point.X)) < this.SX)
		{
			if(Math.abs((this.Y) + (a.Pos.Y - point.Y)) < this.SY)
			{
				float PenX = this.SX - Math.abs((this.X) + (a.Pos.X - point.X));
				float PenY = this.SY - Math.abs((this.Y) + (a.Pos.Y - point.Y));
				return new Vector(PenX,PenY);
			}
		}
		return null;
	}
	public Vector Collide(Entity a,Entity b)//A is this's (long lost) parent
	{
		Vector Coll = null;
		//All boxes
		//ISUSSE
		if(AABB(this,b.CollModel,a,b) != null)//Prelim
		{
			for(int i = 0;i< boxes.size();++i)
			{
				for(int is = 0;is< b.CollModel.boxes.size();++is)
				{
					Coll = AABB(a.CollModel.boxes.get(i),b.CollModel.boxes.get(is),a,b);
					if(Coll != null)
					{
						this.CollidedWith = is;
						b.CollModel.CollidedWith = i;
						return Coll;
					}
				}
			}
		}
		return null;
	}
	public void AddBox(CollBox mod)
	{
		this.boxes.add(mod);
		float Maxx = this.X + this.SX;
		float Minx = this.X - this.SX;
		float Maxy = this.Y + this.SY;
		float Miny = this.Y - this.SY;
		if(mod.X + mod.SX > this.X + this.SX)
		{
			Maxx = mod.X+mod.SX;
		}
		if(mod.X - mod.SX < this.X - this.SX)
		{
			Minx = mod.X-mod.SX;
		}
		if(mod.Y+mod.SY > this.Y + this.SY)
		{
			Maxy = mod.Y+mod.SY;
		}
		if(mod.Y-mod.SY < this.Y - this.SY)
		{
			Miny = mod.Y-mod.SY;
		}
		this.X = (Minx + Maxx)/2;//Midpoints
		this.Y = (Miny + Maxy)/2;
		this.SX = Maxx - this.X;
		this.SY = Maxy - this.Y;
	}
	public Vector AddBox(CollBox mod,Entity a,Entity b)
	{
		Vector AbsDis = new Vector(0,0);
		if(mod.MainNode)
		{
			for(int i = 0;i< mod.boxes.size();++i)
			{
 				CollBox Question = mod.boxes.get(i);
				CollBox CollWith = boxes.get(mod.CollidedWith);
				AbsDis.X = ((b.Pos.X) - (a.Pos.X));// + (CollWith.SX * Math.signum((CollWith.X+b.Pos.X) - (Question.X+a.Pos.X)));
				AbsDis.Y = ((b.Pos.Y) - (a.Pos.Y));// + (CollWith.SY * Math.signum((CollWith.Y+b.Pos.Y) - (Question.Y+a.Pos.Y)));
				Question.X += AbsDis.X;
				Question.Y += AbsDis.Y;
				AddBox(Question);
			}
		}
		else
		{
			AbsDis.X = ((b.Pos.X) - (a.Pos.X));// + (CollWith.SX * Math.signum((CollWith.X+b.Pos.X) - (Question.X+a.Pos.X)));
			AbsDis.Y = ((b.Pos.Y) - (a.Pos.Y));// + (CollWith.SY * Math.signum((CollWith.Y+b.Pos.Y) - (Question.Y+a.Pos.Y)));
			mod.X += AbsDis.X;
			mod.Y += AbsDis.Y;
			AddBox(mod);
		}
		return AbsDis;
	}
}
