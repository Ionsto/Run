package Src;

public class EntityPlanet extends Entity {
	public EntityPlanet(int id) {
		super(id);
		RenderModel = new ModelPlanet(5F,16);//Cube
	}
	public EntityPlanet(int id,float x,float y) {
		super(id,x,y);
		RenderModel = new ModelPlanet(5F,16);//Cube
	}
	
}
