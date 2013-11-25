package Src;

public class EntityRocket extends Entity {
	public float Speed = 10;
	public EntityRocket(int id) {
		super(id);
		RenderModel = new ModelSquare(5F);//Cube
	}

	public EntityRocket(int id,float x,float y) {
		super(id,x,y);
		RenderModel = new ModelSquare(5F);//Cube
	}
}
