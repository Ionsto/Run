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
	public List<Model> models = new ArrayList<Model>();
	public Entity[] objs = new Entity[100];
	public double DeltaTime = 0;
	public double Then = 0;
	public float Scale = 100;
	public float CamraX = 0;
	public float CamraY = 0;
	public float WorldX = 100;
	public float WorldY = 100;
	public World()
	{
		for(int i = 0;i < 100;++i)
		{
			objs[i] = null;
		}
		Model m = new Model();
		m.AddFace((byte)0, (byte)1, (byte)2);
		m.AddFace((byte)1, (byte)2, (byte)3);
		m.AddVert(-1,-1);
		m.AddVert(-1,1);
		m.AddVert(1,-1);
		m.AddVert(1,1);
		m.Init();
		this.models.add(m);
		this.models.add(new ModelPlayer());
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
	}
	public void Render(Shader shader)
	{

        ARBShaderObjects.glUniform2fARB(shader.Scale, Scale, Scale);
		for(int i = 0;i < 100;++i)
		{
			if(objs[i] != null)
			{
		        ARBShaderObjects.glUniform2fARB(shader.Loc, objs[i].PosX - CamraX,  objs[i].PosY - CamraY);
		        ARBShaderObjects.glUniform1fARB(shader.Rot, objs[i].PosR);
		        
				/*GL30.glBindVertexArray(models.get(objs[i].RenderModel).VAO);

				GL20.glEnableVertexAttribArray(0);
				
				GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, models.get(objs[i].RenderModel).VEA);
				GL11.glDrawElements(GL11.GL_TRIANGLES, models.get(objs[i].RenderModel).IndicesCount, GL11.GL_UNSIGNED_BYTE, 0);
				GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
				GL30.glBindVertexArray(0);*/
		        
		        
		        int vaoId = models.get(objs[i].RenderModel).VAO;
		        int vboiId = models.get(objs[i].RenderModel).VEA;
		        int indicesCount = models.get(objs[i].RenderModel).IndicesCount;
				// Bind to the VAO that has all the information about the vertices
				GL30.glBindVertexArray(vaoId);
				GL20.glEnableVertexAttribArray(0);
				GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vboiId);
				GL11.glDrawElements(GL11.GL_TRIANGLES, indicesCount, GL11.GL_UNSIGNED_BYTE, 0);
				GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
				//GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, 3);
				GL20.glDisableVertexAttribArray(0);
				GL30.glBindVertexArray(0);
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
		for(int i = 0;i < models.size();++i)
		{
			models.get(i).Delete();
		}
		for(int i = 0;i < 100;++i)
		{
			if(objs[i] != null)
			{
				objs[i].Destroy();
			}
		}
	}
}
