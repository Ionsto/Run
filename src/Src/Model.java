package Src;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

public class Model {
	List<Byte> IndPusher = new ArrayList<Byte>();
	public Vector<Float> VertPusher = new Vector<Float>();
	public byte[] IndTemp;
	public float[] VertTemp;
	public int IndicesCount = 0;
	public ByteBuffer Indices;
	public FloatBuffer Vertice;
	public int VAO = 0;
	public int VBO = 0;
	public int VEA = 0;
	public void Init()
	{
		IndTemp = tobyteArray(IndPusher.toArray(new Byte[IndPusher.size()]));
		VertTemp = tofloatArray(VertPusher.toArray(new Float[VertPusher.size()]));
		
		Indices = BufferUtils.createByteBuffer(IndTemp.length);
		Vertice = BufferUtils.createFloatBuffer(VertTemp.length);
		
		Indices.put(IndTemp);
		Vertice.put(VertTemp);
		
		Indices.flip();
		Vertice.flip();
		//DO THE OPENGL....
		
		VAO = GL30.glGenVertexArrays();
		GL30.glBindVertexArray(VAO);

		VBO = GL15.glGenBuffers();
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, VBO);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, Vertice, GL15.GL_STATIC_DRAW);
		GL20.glVertexAttribPointer(0, 2, GL11.GL_FLOAT, false, 0, 0);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
		GL30.glBindVertexArray(0);
		
		VEA = GL15.glGenBuffers();
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, VEA);
		GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, Indices, GL15.GL_STATIC_DRAW);
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
	}
	public byte[] tobyteArray(Byte [] b)
	{
		byte [] br = new byte[b.length];
		for(int i = 0;i< b.length;++i)
		{
			br[i] = Byte.valueOf(b[i]);
		}
		return br;
	}
	public float[] tofloatArray(Float [] b)
	{
		float [] br = new float[b.length];
		for(int i = 0;i< b.length;++i)
		{
			br[i] = Float.valueOf(b[i]);
		}
		return br;
	}
	public void AddFace(int i,int j,int k)
	{
		IndPusher.add((byte) i);
		IndPusher.add((byte) j);
		IndPusher.add((byte) k);
		IndicesCount+=3;
	}
	public void AddVert(float x,float y)
	{
		VertPusher.add(x);
		VertPusher.add(y);
	}
	public void Delete()
	{
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
		GL15.glDeleteBuffers(this.VBO);
		
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
		GL15.glDeleteBuffers(this.VEA);
		
		GL30.glBindVertexArray(0);
		GL30.glDeleteVertexArrays(this.VAO);
	}
}
