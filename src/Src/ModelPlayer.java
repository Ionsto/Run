package Src;

public class ModelPlayer extends RenderModel {
	public ModelPlayer()
	{
		this.AddVert(-1, -1);//Main cube
		this.AddVert(-1, 1);
		this.AddVert(1, 1);
		this.AddVert(1, -1);
		this.AddFace(0, 1, 2);
		this.AddFace(2, 3, 0);
		this.Init();
	}
	public ModelPlayer(float size)
	{
		this.AddVert(-size, -size);//Main cube
		this.AddVert(-size, size);
		this.AddVert(size, size);
		this.AddVert(size, -size);
		this.AddFace(0, 1, 2);
		this.AddFace(2, 3, 0);
		this.Init();
	}
}
