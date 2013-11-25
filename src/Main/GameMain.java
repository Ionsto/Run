package Main;

import java.io.File;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.ContextAttribs;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.PixelFormat;

import Src.*;
public class GameMain {
	
	//A is the master race(letter)
	
	
	public static boolean Running = true;
	public int Context = -1;
	World world;
	Shader Shader_Game;	
	Shader Shader_Lamb_Slider;
	int Width = 500;	
	int Height = 500;
	float MaxScale = 300;
	float MinScale = 1;
	double MoveSense = 0.05;
	float ScrollSense = 10;
	Entity Selected = null;
	int Follow = -1;
	public void Start(){
		if(Init())
		{
			MainLoop();
		}
		//T - 1 second till endor is in range....
		Destroy();
		return;
	}
	public boolean Init(){
		try
		{
			//Display.setDisplayMode(new DisplayMode(100,100));
			//Display.setFullscreen(true);
			PixelFormat pixelFormat = new PixelFormat();
			ContextAttribs contextAtrributes = new ContextAttribs(3,2)
				.withForwardCompatible(true)
				.withProfileCore(true);
				//Display.setFullscreen(true);
				Display.setTitle("MainLoop?");
				Display.setDisplayMode(new DisplayMode(Width,Height));
				Display.create(pixelFormat, contextAtrributes);
				System.out.println(GL11.glGetString(GL11.GL_VERSION));
				Keyboard.create();
				Mouse.create();
				Width = Display.getWidth();
				Height = Display.getHeight();
		}
		catch (Exception e)
		{
			return false;
		}
		
		GLInit();
		world =  new World();
		return true;
	}
	public void GLInit(){
		Shader_Game = new Shader(new File("shader").getAbsolutePath()+"/Game_vert.vert",new File("shader").getAbsolutePath()+"/Game_frag.frag");
		Shader_Lamb_Slider = new Shader(new File("shader").getAbsolutePath()+"/Slider_vert.vert",new File("shader").getAbsolutePath()+"/Slider_frag.frag");
		GL20.glEnableVertexAttribArray(0);
	}
	public void MainLoop()//Nicely named
	{
		while(Running)
		{
			Running = !Display.isCloseRequested();
			PollInput();
			Update();
			Render();
			Display.update();
		}
	}
	public void Update()
	{
		world.Update();
	}
	public void Render()
	{
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
		Shader_Game.Bind();
		world.Render(Shader_Game);
		if(Selected != null)
		{
			Shader_Lamb_Slider.Bind();
			world.RenderInfo(Selected,Shader_Lamb_Slider);
		}
		
	}
	boolean down = false;
	boolean downR = false;
	boolean downF = false;
	boolean downm = false;
	public void PollInput()
	{
		world.Scale = Math.min(Math.max(world.Scale - ((Mouse.getDWheel() / ScrollSense)),MinScale),MaxScale);
		if(Keyboard.isKeyDown(Keyboard.KEY_R))
		{
			if(!downR)
			{
				world.Reset();
				downR = true;
			}
		}
		else
		{
			downR = false;
		}
		
		if(Follow != -1)
		{
			if(world.objs[Follow] == null)
			{
				Follow = -1;
			}
			else
			{
				world.CamraX = world.objs[Follow].Pos.X;
				world.CamraY = world.objs[Follow].Pos.Y;
			}
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_ESCAPE))
		{
			Running = false;//DIE, DIE PROGRAM
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT))
		{
			if(Selected != null)
			{
				if(Selected instanceof EntityRocket)
				{
					Selected.Vel.X = 0;
					Selected.Vel.Y = 0;
				}
			}
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_LCONTROL))
		{
			if(!downF)
			{
				if(Selected != null)
				{
					Follow = Selected.Id;
					downF = true;
				}
				else
				{
					Follow = -1;
				}
			}
		}
		else
		{
			downF = false;
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_W))
		{
			world.CamraY += MoveSense * world.DeltaTime * world.Scale;
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_S))
		{
			world.CamraY -= MoveSense * world.DeltaTime * world.Scale;
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_A))
		{
			world.CamraX -= MoveSense * world.DeltaTime * world.Scale;
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_D))
		{
			world.CamraX += MoveSense * world.DeltaTime * world.Scale;
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_E))//Move
		{
			Context = 0;
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_F))//Fire
		{
			Context = 1;
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_Q))//Add block
		{
			Context = 2;
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_C))//Harvest block
		{
			Context = 3;
		}
		if(Mouse.isButtonDown(0))
		{
			if(!downm)
			{
				if(Selected != null)
				{
					//Do Something
					if(Context == 0)//Move
					{
						if(Selected instanceof EntityRocket)
						{
							if(Selected.res[1].Quantity > 5 && Selected.res[0].Quantity > 10 )
							{
								Vector mouse = GetMouse();
								float Theta = (float) Math.atan2(Selected.Pos.Y - mouse.Y,Selected.Pos.X - mouse.X);
								Selected.Vel.X = -(float) (((EntityRocket)Selected).Speed * Math.cos(Theta));
								Selected.Vel.Y = -(float) (((EntityRocket)Selected).Speed * Math.sin(Theta));
								Selected.res[1].Quantity -= 5;
								Selected.ResCount -= 5;
							}
						}
					}
					//Do Something
					if(Context == 1)//Fire Rocket
					{
						if(Selected instanceof EntityPlayer)
						{
							if(Selected.res[0].Quantity > 30 &&  Selected.res[1].Quantity > 50)
							{
								Vector mouse = GetMouse();
								float Theta = (float) Math.atan2(Selected.Pos.Y - mouse.Y,Selected.Pos.X - mouse.X);
								EntityRocket roc = new EntityRocket(0);
								float x = Selected.CollModel.SX + roc.CollModel.SX;
								float y = Selected.CollModel.SY + roc.CollModel.SY;
								float dis = (float) Math.sqrt((x * x) + (y * y));
								roc.Pos.X = Selected.Pos.X -( (float) (dis * Math.cos(Theta)));
								roc.Pos.Y = Selected.Pos.Y -( (float) (dis * Math.sin(Theta)));
								roc.Vel.X = (float) (-(float) roc.Speed * Math.cos(Theta));
								roc.Vel.Y = (float) (-(float) roc.Speed * Math.sin(Theta));
								int i = world.Add(roc);
								Selected.res[0].Quantity -= 10;
								Selected.res[1].Quantity -= 50;
								Selected.ResCount -= 60;
							}
						}
					}
					if(Context == 2)//Add BuildingBlock
					{
						if(Selected instanceof EntityPlayer)
						{
							if(Selected.res[0].Quantity > 40 && Selected.res[1].Quantity > 60)
							{
								Vector mouse = GetMouse();
								float Theta = (float) Math.atan2(Selected.Pos.Y - mouse.Y,Selected.Pos.X - mouse.X);
								EntityRocket roc = new EntityBlock(0);
								float dis = 50;
								roc.Pos.X = Selected.Pos.X + (Math.min(dis,Math.abs(Selected.Pos.X - mouse.X)) * Math.signum(Selected.Pos.X - mouse.X) * -1);
								roc.Pos.Y = Selected.Pos.Y + (Math.min(dis,Math.abs(Selected.Pos.Y - mouse.Y)) * Math.signum(Selected.Pos.Y - mouse.Y) * -1);
								int i = world.Add(roc);
								Selected.res[0].Quantity -= 20;
								Selected.res[1].Quantity -= 60;
								Selected.ResCount -= 80;
							}
						}
					}
					if(Context == 3)//Harvest block
					{
						if(Selected instanceof EntityPlayer)
						{
							Vector mouse = GetMouse();
							Entity SelectedToHarvest = world.Select(mouse.X,mouse.Y);
							if(SelectedToHarvest != null)
							{
								float X = Math.abs(Selected.Pos.X - SelectedToHarvest.Pos.X);
								float Y = Math.abs(Selected.Pos.Y - SelectedToHarvest.Pos.Y);
								float Toll = 20;
								if(X < Selected.CollModel.SX + SelectedToHarvest.CollModel.SX + Toll && Y < Selected.CollModel.SY + SelectedToHarvest.CollModel.SY + Toll)
								{
									world.MoveStuff(Selected,SelectedToHarvest,10);
								}
							}
						}
					}
					Selected.Selected = false;
					Context = -1;
					Selected = null;
				}
				else
				{
					//Try and select something
					Vector mouse = GetMouse();
					Selected = world.Select(mouse.X,mouse.Y);
					if(Selected != null)
					{
						Selected.Selected = true;
					}
				}
				downm = true;
			}
		}
		else
		{
			downm = false;
		}
	}
	public Vector GetMouse()
	{
		float mx = Mouse.getX();
		float my = Mouse.getY();
		float wx = (Width/2.0F);
		float wy = (Height/2.0F);
		float Mx = (float)(mx-wx)/wx;
		float My = (float)(my-wy)/wy;
		Mx *= world.Scale;
		My *= world.Scale;
		Mx += world.CamraX;
		My += world.CamraY;
		return new Vector(Mx,My);
	}
	public void Destroy()
	{
		//I say we take of into orbit and nuke the place....
		world.Destroy();
		Shader_Game.Destroy();
		Shader_Lamb_Slider.Destroy();
	}
}
