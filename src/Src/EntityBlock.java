package Src;

public class EntityBlock extends EntityRocket {

	public EntityBlock(int id) {
		super(id);
		this.Green = 1;
		this.Red = 0;
	}
	public EntityBlock(int id, float x, float y) {
		this(id);
		this.Pos.X = x;
		this.Pos.Y = y;
	}
	public void SetUpRes()
	{
		res[0] = new Resouce();
		res[0].Name = "Iron";
		res[0].Quantity = 20;
		res[1] = new Resouce();
		res[1].Name = "Hydrogen";
		res[1].Quantity = 60;
	}

}
