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
	World world;
	Shader Shader_Game;	
	int Width = 500;	
	int Height = 500;
	float MaxScale = 100;
	float MinScale = 1;
	double MoveSense = 0.05;
	float ScrollSense = 10;
	Entity Selected = null;
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
				Display.setFullscreen(true);
				Display.setTitle("MainLoop?");
				Display.setDisplayMode(new DisplayMode(Width,Height));
				Display.create(pixelFormat, contextAtrributes);
				System.out.println(GL11.glGetString(GL11.GL_VERSION));
				Keyboard.create();
				Mouse.create();
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
		Shader_Game = new Shader(new File("shader").getAbsolutePath()+"/vert.vert",new File("shader").getAbsolutePath()+"/frag.frag");
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
	}
	boolean down = false;
	boolean downm = false;
	public void PollInput()
	{
		world.Scale = Math.min(Math.max(world.Scale - ((Mouse.getDWheel() / ScrollSense)),MinScale),MaxScale);
		if(Keyboard.isKeyDown(Keyboard.KEY_R))
		{
			world.Reset();
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_E))
		{
			if(!down)
			{
				world.objs[world.Add(new Entity(0,0,-50))].Vel.Y = 5;
				down = true;
			}
		}
		else
		{
			down = false;
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
		if(Mouse.isButtonDown(0))
		{
			if(!downm)
			{
				if(Selected != null)
				{
					//Do Something
					System.out.println(Selected.Id);
					Selected = null;
				}
				else
				{
					//Try and select something
					float mx = Mouse.getX();
					float my = Mouse.getY();
					float wx = (Width/2.0F);
					float wy = (Height/2.0F);
					float Mx = (float)(mx-wx)/wx;
					float My = (float)(my-wy)/wy;
					Mx *= world.Scale;
					My *= world.Scale;
					//Mx += world.CamraX;
					//My += world.CamraY;
					Selected = world.Select(Mx,My);
					//System.out.println(((world.CamraX*-1)+ Mouse.getX()) + ":" + ((world.CamraY*-1) + (Height - Mouse.getY())) );
				}
				downm = true;
			}
		}
		else
		{
			downm = false;
		}
	}
	public void Destroy()
	{
		//I say we take of into orbit and nuke the place....
		world.Destroy();
		Shader_Game.Destroy();
		Display.destroy();
	}
}
