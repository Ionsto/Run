package Src;

public class ModelPlayer extends Model {
	public ModelPlayer()
	{
		super();
		this.AddVert(-1, -1);//Main cube
		this.AddVert(-1, 1);
		this.AddVert(1, 1);
		this.AddVert(1, -1);
		this.AddFace(0, 1, 2);
		this.AddFace(1, 2, 3);
		
	}
}
