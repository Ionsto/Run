package Src;

public class EntityRocket extends Entity {
	public float Speed = 10;
	public EntityRocket(int id) {
		super(id);
		RenderModel = new ModelSquare(5F);//Cube
	}

	public EntityRocket(int id,float x,float y) {
		this(id);
		this.Pos.X = x;
		this.Pos.Y = y;
	}
	public void SetUpRes()
	{
		res[0] = new Resouce();
		res[0].Name = "Iron";
		res[0].Quantity = 10;
		res[1] = new Resouce();
		res[1].Name = "Hydrogen";
		res[1].Quantity = 50;
	}
}
