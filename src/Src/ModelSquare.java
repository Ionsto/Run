package Src;

public class ModelSquare extends RenderModel {
	public ModelSquare()
	{
		this(1);
	}
	public ModelSquare(float size)
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
