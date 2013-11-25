package Src;

public class Entity {
	public Vector Pos = new Vector(0,0);
	public Vector Vel = new Vector(0,0);
	public float Red = 1;//Defult
	public float Green = 0;
	public float Blue = 0;
	public boolean Selected = false;
	public float Mass = 10;
	public int Id = 0;
	public RenderModel RenderModel;//The Model used for rendering
	public CollBox CollModel;
	public int ResCount = 0;
	public Resouce[] res= new Resouce[5];//personal values
	//Iron:used for hulls of ships
	//Hydrogen:used for fuel
	//Oxygen:used for burning stuff
	//Money:used for buying stuff
	//Rock:used to 'tank it up'!
	public Entity(int id)
	{
		RenderModel = new ModelSquare(5F);//Cube
		CollModel = new CollBox(new CollBox(0,0,2.5F,2.5F));
		for(int i = 0;i< res.length;++i)
		{
			res[i] = null;//Set them all to null
		}
		this.Id = id;
		SetUpRes();
		for(int i = 0;i< res.length;++i)
		{
			if(res[i] != null)
			{
				this.ResCount += res[i].Quantity;
			}
		}
	}
	public void SetUpRes()
	{
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

		for(int i = 0;i< res.length;++i)
		{
			if(res[i] != null)
			{
				if(res[i].Quantity>100){res[i].Quantity = 100;}
			}
		}
	}
	public void Intergrate(World world)
	{
		this.Pos.X += this.Vel.X * world.DeltaTime;
		this.Pos.Y += this.Vel.Y* world.DeltaTime;
	}
	public void Friction(World world)
	{
		this.Vel.X /= (0.05 * world.DeltaTime)+1;//Woo friction
		this.Vel.Y /= (0.05 * world.DeltaTime)+1;
	}
	public void Destroy(World world)
	{
		this.RenderModel.Delete();
		world.objs[this.Id] = null;//Yay reset it
	}
}
