package Src;

public class EntityRocket extends Entity {

	public EntityRocket(int id) {
		super(id);
		// TODO Auto-generated constructor stub
	}

	public EntityRocket(int id,float x,float y) {
		super(id,x,y);
		RenderModel = new ModelPlanet(5F,16);//Cube
	}
}
