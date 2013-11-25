package Src;

public class EntityPlanet extends Entity {
	public EntityPlanet(int id) {
		super(id);
		RenderModel = new ModelPlanet(5F,16);//Cube
	}
	public EntityPlanet(int id,float x,float y) {
		this(id);
		this.Pos.X = x;
		this.Pos.Y = y;
	}
	public void SetUpRes()
	{
		res[0] = new Resouce();
		res[0].Name = "Iron";
		res[0].Quantity = (int) (Math.random() * 50) + 10;
		res[1] = new Resouce();
		res[1].Name = "Hydrogen";
		res[1].Quantity = (int) (Math.random() * 80) + 40;
		res[2] = new Resouce();
		res[2].Name = "Oxygen";
		res[2].Quantity = (int) (Math.random() * 50) + 10;
		
		//res[3] = new Resouce();
		//res[3].Name = "Rock";
		//res[3].Quantity = (int) (Math.random() * 30)+90;
	}
	
}
