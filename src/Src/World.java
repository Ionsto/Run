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
	public float WorldX = 1000;
	public float WorldY = 1000;
	public ModelSquare square;
	public World()
	{
		for(int i = 0;i < 100;++i)
		{
			objs[i] = null;
		}
		square = new ModelSquare();
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
					if(objs[i] == null)//Sort of nullifies the preveiose statement
					{
						++i;
					}
					if(objs[is] != null)
					{
						Vector coll = objs[i].CollModel.Collide(objs[i], objs[is]);
						if(coll != null)
						{
							//float RatX = Math.abs(objs[i].Vel.X + objs[is].Vel.X);
							//float RatY = Math.abs(objs[i].Vel.Y + objs[is].Vel.Y);
							
							//objs[i].Pos.X -= coll.Y * ZsDiv(objs[i].Vel.X, RatX);
							//objs[i].Pos.Y -= coll.X * ZsDiv(objs[i].Vel.Y, RatY);
							//objs[is].Pos.X += coll.Y * ZsDiv(objs[is].Vel.X, RatX);
							//objs[is].Pos.Y += coll.X * ZsDiv(objs[is].Vel.Y, RatY);
							
							float X = ((objs[i].Mass * objs[i].Vel.X)+(objs[is].Mass * objs[is].Vel.X)) / (objs[i].Mass + objs[is].Mass);
							float Y = ((objs[i].Mass * objs[i].Vel.Y)+(objs[is].Mass * objs[is].Vel.Y)) / (objs[i].Mass + objs[is].Mass);
							float force = ForceCalculate(objs[i].Vel,objs[is].Vel,objs[i].Mass,objs[is].Mass);
							objs[i].Vel.X = X;
							objs[i].Vel.Y = Y;
							objs[is].Vel.X = X;
							objs[is].Vel.Y = Y;
							if(force > 50)
							{
								int affect = i;
								int affects = is;
								if(1 - (objs[is].Mass / objs[i].Mass) > 0.5)//If one mass is rediculesly massive comparitivle
								{
									if(objs[is] instanceof EntityPlayer)
									{
										affects = i;
										affect = is;
									}
								}
								Join(objs[affect],objs[affects]);
								if(objs[affects]!= null){objs[affects].Destroy(this);}
							}
							else
							{
								//Move Resources (Planet -> Rocket -> SpaceShip) order of importance
								int from = i;
								int to = is;
								int MoveRate = 10;
								if(objs[from] instanceof EntityPlanet && objs[to] instanceof EntityRocket)
								{
									MoveStuff(objs[to],objs[from],MoveRate);
								}
								if(objs[from] instanceof EntityRocket && !(objs[from] instanceof EntityPlayer || objs[from] instanceof EntityBlock) && objs[to] instanceof EntityPlayer)
								{
									MoveStuff(objs[to],objs[from],MoveRate);
								}
								from = is;
								to = i;
								if(objs[from] instanceof EntityPlanet && objs[to] instanceof EntityRocket)
								{
									MoveStuff(objs[to],objs[from],MoveRate);
								}
								if(objs[from] instanceof EntityRocket && !(objs[from] instanceof EntityPlayer || objs[from] instanceof EntityBlock) && objs[to] instanceof EntityPlayer)
								{
									MoveStuff(objs[to],objs[from],MoveRate);
								}
							}
						}
					}
				}
			}
		}
	}
	public void MoveStuff(Entity a,Entity b,int count)
	{
		for(int i = 0;i< b.res.length;++i)
		{
			if(b.res[i] != null)
			{
				if(a.res[i] == null)
				{
					//populate
					a.res[i] = new Resouce();
					a.res[i].Exchange = b.res[i].Exchange;
					a.res[i].Name = b.res[i].Name;
				}
				if(b.res[i].Quantity >= count)
				{
					a.res[i].Quantity += count;
					b.res[i].Quantity -= count;
					a.ResCount += count;
					b.ResCount -= count;
				}
				else
				{
					int countt = b.res[i].Quantity;
					a.res[i].Quantity += countt;
					b.res[i].Quantity -= countt;
					a.ResCount += countt;
					b.ResCount -= countt;
				}
			}
			if(b.ResCount <= 0)
			{
				b.Destroy(this);
			}
		}
	}
	public float ForceCalculate(Vector a,Vector b,float Massa,float Massb)
	{
		float am = (float) Math.sqrt((a.X*a.X)+(a.Y*a.Y));
		float forcea = 0.5F *(Massa * (am* am));
		return forcea;
	}
	public float ZsDiv(float a,float b)//Special function that returns 0(zero safe division)
	{
		if(a != 0 && b != 0)
		{
			return a/b;
		}
		return 0;
	}
	public void Join(Entity a,Entity bob)
	{
		//I dislike bob destroy him
		MoveStuff(a,bob,bob.ResCount);
		a.Mass += bob.Mass;
		a.Selected |= bob.Selected;
		if(a instanceof EntityRocket && bob instanceof EntityRocket){
			((EntityRocket)a).Speed += ((EntityRocket)bob).Speed;
		}
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
		CamraX = 0;
		CamraY = 0;
		for(int i = 0;i < 100;++i)
		{
			if(objs[i] != null)
			{
				objs[i].Destroy(this);
				objs[i] = null;
			}
		}
		for(int i = 0;i < 50;++i)
		{
			float X = (float) ((Math.random() * this.WorldX*2) - this.WorldX);
			float Y = (float) ((Math.random() * this.WorldY*2) - this.WorldY);
			Add(new EntityPlanet(0, X,Y));
		}
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
	public void RenderInfo(Entity e,Shader shader)
	{
		for(int i = 0;i < e.res.length;++i)
		{
			if(e.res[i] != null)
			{
				if(e.res[i].Quantity > 0)
				{
					float bars = 8;
					float barshift = 1/(bars*2);
					float Height = e.res[i].Quantity;
					//GL20.glUniform2f(shader.Scale, barshift, 1);
					//GL20.glUniform2f(shader.Loc,-(i * barshift)-((bars)* barshift),-1 /2);
					GL20.glUniform2f(shader.Scale, barshift, Math.min(Height,100)/200);
					GL20.glUniform2f(shader.Loc,((i+1)/bars)-1,(Math.min(Height,100)/200)-1);
			        if(i == 0)
			        {
			        	GL20.glUniform3f(shader.Colour, 1,0,0);
			        }
			        if(i == 1)
			        {
			        	GL20.glUniform3f(shader.Colour, 0,1,0);
			        }
			        if(i == 2)
			        {
			        	GL20.glUniform3f(shader.Colour, 0,0,1);
			        }
			        if(i == 3)
			        {
			        	GL20.glUniform3f(shader.Colour, 1,0,1);
			        }
			        if(i == 4)
			        {
			        	GL20.glUniform3f(shader.Colour, 1,1,1);
			        }
			        //draw
					GL30.glBindVertexArray(square.VAO);
					GL20.glEnableVertexAttribArray(0);
					GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, square.VEA);
					GL11.glDrawElements(GL11.GL_TRIANGLES, square.IndicesCount, GL11.GL_UNSIGNED_BYTE, 0);
					GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
					GL20.glDisableVertexAttribArray(0);
					GL30.glBindVertexArray(0);
				}
			}
		}
	}
	public void Render(Shader shader)
	{

        GL20.glUniform2f(shader.Scale, Scale, Scale);
		for(int i = 0;i < 100;++i)
		{
			if(objs[i] != null)
			{
				//set shader info
		        GL20.glUniform2f(shader.Loc, objs[i].Pos.X - CamraX,  objs[i].Pos.Y - CamraY);
		        if(objs[i].Selected)
		        {
			    	GL20.glUniform3f(shader.Colour, 0,0,1);
		        }
		        else
		        {
			    	GL20.glUniform3f(shader.Colour, objs[i].Red,objs[i].Green,objs[i].Blue);
		        }
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
		for(int i = 0 ;i < objs.length;++i)
		{
			if(objs[i] != null)
			{
				if(objs[i].CollModel.AABBP(objs[i], new Vector(x,y))!= null)
				{
					best = i;
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
