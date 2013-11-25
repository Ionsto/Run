package Src;

public class EntityPlayer extends EntityRocket {

	public EntityPlayer(int id) {
		super(id);
		Speed = 3;
		this.Green = 1;
	}
	public EntityPlayer(int id,float x,float y) {
		this(id);
		this.Pos.X = x;
		this.Pos.Y = y;
	}
	public void SetUpRes()
	{
		//20 To sutain 
		res[0] = new Resouce();
		res[0].Name = "Iron";
		res[0].Quantity = 100;
		res[1] = new Resouce();
		res[1].Name = "Hydrogen";
		res[1].Quantity = 80;
	}
}
