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
							float RatX = objs[i].Vec.X + objs[is].Vec.X;
							float RatY = objs[i].Vec.Y + objs[is].Vec.Y;							
							objs[i].Pos.X -= coll.X * ZsDiv(objs[i].Vec.X,RatX);
							objs[i].Pos.Y -= coll.Y * ZsDiv(objs[i].Vec.Y,RatY);
							objs[is].Pos.X += coll.X * ZsDiv(objs[is].Vec.X,RatX);
							objs[is].Pos.Y -= coll.Y * ZsDiv(objs[is].Vec.Y,RatY);
							Vector normal = new Vector(0,0);
							objs[i].Vec.X = 0;
							objs[i].Vec.Y = 0;
							objs[is].Vec.X = 0;
							objs[is].Vec.Y = 0;
							Join(objs[i],objs[is]);
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
		a.CollModel.AddBox(bob.CollModel,a,bob);
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
				objs[i].Destroy();
				objs[i] = null;
			}
		}
		Add(new EntityPlayer(0,-100,0));
		Add(new EntityPlayer(0,100,0));
		objs[0].Vec.X = 5;
		objs[1].Vec.X = -5;
	}
	public void Destroy()
	{
		for(int i = 0;i < 100;++i)
		{
			if(objs[i] != null)
			{
				objs[i].Destroy();
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
	
}
