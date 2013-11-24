package Src;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.Sys;
import org.lwjgl.opengl.ARBShaderObjects;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

public class World {
	public Entity[] objs = new Entity[100];
	public double DeltaTime = 0;
	public double Then = 0;
	public float Scale = 100;
	public float CamraX = 0;
	public float CamraY = 0;
	public float WorldX = 200;
	public float WorldY = 200;
	public World()
	{
		for(int i = 0;i < 100;++i)
		{
			objs[i] = null;
		}
	}
	public long getTime() {
	    return (Sys.getTime() * 1000) / Sys.getTimerResolution();
	}
	public void Update()
	{
		if(Then == 0)
		{
			Then = System.currentTimeMillis();
		}
		long Now = System.currentTimeMillis();
		DeltaTime = Math.max((double) (Now - Then),1)/100;
		Then = Now;
		for(int i = 0;i < 100;++i)
		{
			if(objs[i] != null)
			{
				objs[i].Update(this);
				if(Math.abs(objs[i].Pos.X) > this.WorldX)
				{
					objs[i].Pos.X = Math.signum(objs[i].Pos.X) * this.WorldX*-1;
				}
				if(Math.abs(objs[i].Pos.Y) > this.WorldY)
				{
					objs[i].Pos.Y = Math.signum(objs[i].Pos.Y) * this.WorldY * -1;
				}
			}
		}
		Cycle();
	}
	public void Cycle()
	{
		//Collision
		for(int i = 0;i< objs.length-1;++i)
		{
			if(objs[i] != null)
			{
				for(int is = i+1;is< objs.length;++is)
				{
					if(objs[is] != null)
					{
						Vector coll = objs[i].CollModel.Collide(objs[i], objs[is]);
						if(coll != null)
						{
							float RatX = objs[i].Vel.X + objs[is].Vel.X;
							float RatY = objs[i].Vel.Y + objs[is].Vel.Y;							
							//objs[i].Pos.X -= coll.X * ZsDiv(objs[i].Vel.X,RatX);
							//objs[i].Pos.Y -= coll.Y * ZsDiv(objs[i].Vel.Y,RatY);
							//objs[is].Pos.X += coll.X * ZsDiv(objs[is].Vel.X,RatX);
							//objs[is].Pos.Y -= coll.Y * ZsDiv(objs[is].Vel.Y,RatY);
							Vector normal = new Vector(0,0);
							float X = ((objs[i].Mass * objs[i].Vel.X)+(objs[is].Mass * objs[is].Vel.X)) / (objs[i].Mass + objs[is].Mass);
							float Y = ((objs[i].Mass * objs[i].Vel.Y)+(objs[is].Mass * objs[is].Vel.Y)) / (objs[i].Mass + objs[is].Mass);
							objs[i].Mass += objs[is].Mass;
							objs[i].Vel.X = X;
							objs[i].Vel.Y = Y;
							Join(objs[i],objs[is]);
							objs[is].Destroy(this);
						}
					}
				}
			}
		}
	}
	public float ZsDiv(float a,float b)//Special function that returns 0(zero safe division)
	{
		float ans = 0;
		if(a != 0 && b != 0)
		{
			ans = a/b;
		}
		return ans;
	}
	public void Join(Entity a,Entity bob)
	{
		//I dislike bob destroy him
		a.RenderModel.Join(bob.RenderModel,a,bob, a.CollModel.AddBox(bob.CollModel,a,bob) );
	}
	public int Add(Entity ent)
	{
		for(int i = 0;i < 100;++i)
		{
			if(objs[i] == null)
			{
				objs[i] = ent;
				objs[i].Id = i;
				return i;
			}
		}
		return 0;
	}
	public void Reset()
	{
		for(int i = 0;i < 100;++i)
		{
			if(objs[i] != null)
			{
				objs[i].Destroy(this);
				objs[i] = null;
			}
		}
		Add(new EntityPlanet(0,-100,0));
		Add(new EntityPlanet(0,100,0));
		Add(new EntityPlayer(0,0,100));
		objs[0].Vel.X = 5;
		objs[1].Vel.X = -5;
	}
	public void Destroy()
	{
		for(int i = 0;i < 100;++i)
		{
			if(objs[i] != null)
			{
				objs[i].Destroy(this);
			}
		}
	}
	public void Render(Shader shader)
	{

        ARBShaderObjects.glUniform2fARB(shader.Scale, Scale, Scale);
		for(int i = 0;i < 100;++i)
		{
			if(objs[i] != null)
			{
				//set shader info
		        ARBShaderObjects.glUniform2fARB(shader.Loc, objs[i].Pos.X - CamraX,  objs[i].Pos.Y - CamraY);
		        //draw
				GL30.glBindVertexArray(objs[i].RenderModel.VAO);
				GL20.glEnableVertexAttribArray(0);
				GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, objs[i].RenderModel.VEA);
				GL11.glDrawElements(GL11.GL_TRIANGLES, objs[i].RenderModel.IndicesCount, GL11.GL_UNSIGNED_BYTE, 0);
				GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
				GL20.glDisableVertexAttribArray(0);
				GL30.glBindVertexArray(0);
			}
		}
	}
	public Entity Select(float x,float y)
	{
		int best = -1;
		float dis = 50;
		for(int i = 0 ;i < objs.length;++i)
		{
			if(objs[i] != null)
			{
				float xd = objs[i].Pos.X - x;
				float yd = objs[i].Pos.Y - y;
				float cdis = (xd * xd) + (yd * yd);
				if(Math.sqrt(cdis) < dis)
				{
					best = i;
					dis = cdis;
				}
			}
		}
		if(best != -1)
		{
			return objs[best];
		}
		return null;
	}
}
