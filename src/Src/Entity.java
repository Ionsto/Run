package Src;

public class Entity {
	public float PosX = 0;
	public float PosY = 0;
	public float PosR = 0;
	public float VecX = 10F;
	public float VecY = 0;
	public float VecR = 10;
	public int Id = 0;
	public RenderModel RenderModel;//The Model used for rendering
	public CollModel CollModel;
	public Resouce[] res= new Resouce[10];//personal values
	//Iron:used for hulls of ships
	//Hydrogen:used for fuel
	//Oxygen:used for burning stuff
	//Money:used for buying stuff
	//Rock:used to 'tank it up'!
	public Entity(int id)
	{
		RenderModel = new ModelPlayer(5F);//Cube
		CollModel = new CollModel(0,0,2.5F,2.5F);
		for(int i = 0;i< res.length;++i)
		{
			res[i] = null;//Set them all to null
		}
		this.Id = id;
	}
	public Entity(int id,float x,float y)
	{
		this(id);
		this.PosX = x;
		this.PosY = y;
	}
	public void Update(World world)
	{
		Intergrate(world);
		Friction(world);
	}
	public void Intergrate(World world)
	{
		this.PosX += (float) (this.VecX * world.DeltaTime);
		this.PosY += (float) (this.VecY * world.DeltaTime);
		this.PosR += (float) (this.VecR * world.DeltaTime);
	}
	public void Friction(World world)
	{
		this.VecX /= 1.001;
		this.VecY /= 1.001;
		this.VecR /= 1.001;
	}
	public void Destroy()
	{
	}
}
