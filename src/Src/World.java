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
				if(Math.abs(objs[i].PosX) > this.WorldX)
				{
					objs[i].PosX = Math.signum(objs[i].PosX) * this.WorldX*-1;
				}
				if(Math.abs(objs[i].PosY) > this.WorldY)
				{
					objs[i].PosY = Math.signum(objs[i].PosY) * this.WorldY * -1;
				}
			}
		}
		Cycle();
	}
	public void Render(Shader shader)
	{

        ARBShaderObjects.glUniform2fARB(shader.Scale, Scale, Scale);
		for(int i = 0;i < 100;++i)
		{
			if(objs[i] != null)
			{
				//set shader info
		        ARBShaderObjects.glUniform2fARB(shader.Loc, objs[i].PosX - CamraX,  objs[i].PosY - CamraY);
		        ARBShaderObjects.glUniform1fARB(shader.Rot, objs[i].PosR);
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
	public void Cycle()
	{
		//Colliosion
		for(int i = 0;i< objs.length-1;++i)
		{
			if(objs[i] != null)
			{
				for(int is = i+1;is< objs.length;++is)
				{
					if(objs[is] != null)
					{
						if(objs[i].CollModel.AABB(objs[is].CollModel, 1, objs[i], objs[is]))
						{
							objs[i].PosX *= -1;
							objs[i].PosY *= -1;
							objs[is].PosX *= -1;
							objs[is].PosY *= -1;
						}
					}
				}
			}
		}
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
}
