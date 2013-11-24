package Src;

public class Entity {
	public Vector Pos = new Vector(0,0);
	public Vector Vel = new Vector(0,0);
	public float Mass = 10;
	public int Id = 0;
	public RenderModel RenderModel;//The Model used for rendering
	public CollBox CollModel;
	public Resouce[] res= new Resouce[10];//personal values
	//Iron:used for hulls of ships
	//Hydrogen:used for fuel
	//Oxygen:used for burning stuff
	//Money:used for buying stuff
	//Rock:used to 'tank it up'!
	public Entity(int id)
	{
		RenderModel = new ModelPlayer(5F);//Cube
		CollModel = new CollBox(new CollBox(0,0,2.5F,2.5F));
		for(int i = 0;i< res.length;++i)
		{
			res[i] = null;//Set them all to null
		}
		this.Id = id;
	}
	public Entity(int id,float x,float y)
	{
		this(id);
		this.Pos.X = x;
		this.Pos.Y = y;
	}
	public void Update(World world)
	{
		Intergrate(world);
		Friction(world);
	}
	public void Intergrate(World world)
	{
		float DxDt;
		float DyDt;
		this.Pos.X += DxDt * world.DeltaTime;
		this.Pos.Y += DyDt * world.DeltaTime;
		
		this.Pos.X += (float) (this.Vel.X * world.DeltaTime);
		this.Pos.Y += (float) (this.Vel.Y * world.DeltaTime);
	}
	public Derivitve Evaluate()
	{
		
	}
	public void Friction(World world)
	{
		this.Vel.X /= (0.05 * world.DeltaTime)+1;//Woo friction
		this.Vel.Y /= (0.05 * world.DeltaTime)+1;
	}
	public Vector Accelerate(Vector pos,Vector vel)
	{
		float K = 10;
		float B = 1;
		return new Vector(-K * pos.X - b*vel.X,-K * pos.Y - b*vel.Y)
	}
	public void Destroy(World world)
	{
		this.RenderModel.Delete();
		world.objs[this.Id] = null;//Yay reset it
	}
}
