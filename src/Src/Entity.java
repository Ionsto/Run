package Src;

public class Entity {
	public Vector Pos = new Vector(0,0);
	public Vector Vec = new Vector(0,0);
	public float Mass = 0;
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
		this.Pos.X += (float) (this.Vec.X * world.DeltaTime);
		this.Pos.Y += (float) (this.Vec.Y * world.DeltaTime);
	}
	public void Friction(World world)
	{
		this.Vec.X /= (0.05 * world.DeltaTime)+1;//Woo friction
		this.Vec.Y /= (0.05 * world.DeltaTime)+1;
	}
	public void Destroy()
	{
	}
}
