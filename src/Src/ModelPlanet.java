package Src;

public class ModelPlanet extends RenderModel {

	public ModelPlanet()
	{
		this.AddVert(-1, -1);//Main cube
		this.AddVert(-1, 1);
		this.AddVert(1, 1);
		this.AddVert(1, -1);
		this.AddFace(0, 1, 2);
		this.AddFace(2, 3, 0);
		this.Init();
	}
	public ModelPlanet(float size,int steps)
	{
		for(int i = 0;i < steps;++i)
		{
			float theta = 2.0f * 3.1415926f * i / steps;
			float x = (float) (size * Math.cos(theta));//calculate the x component 
			float y = (float) (size * Math.sin(theta));//calculate the y component 
			this.AddVert(x,y);
		}
		for(int i = 2;i < steps;i += 2 )
		{
			for(int is = 1;is < steps;is += 2 )
			{
				this.AddFace((byte)0, (byte)i,(byte)is);//Who does not like fanning?
			}
		}
		this.Init();
	}
	public int getround(int loc,int max)//Making the numbers like a mobuse strip
	{
		if(loc < max)
		{
			return loc;
		}
		if(loc == max)
		{
			return 0;
		}
		return getround(loc-max,max);
	}
}
