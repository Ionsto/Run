package Src;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.*;

public class Shader {
	public int Frag = 0;
	public int Vert = 0;
	public int Program = 0;
	public int Loc = 0;//Totalitarian degree of freedom  (my degree of freedom)
	public int Rot = 0;
	public int Scale = 0;
	public boolean Work = false;
	public Shader(String frag,String vert)
	{
		Frag = genShader(GL20.GL_FRAGMENT_SHADER,GetFile(vert));
		Vert = genShader(GL20.GL_VERTEX_SHADER,GetFile(frag));
		if(Work)
		{
			Program = GL20.glCreateProgram();
			GL20.glAttachShader(Program, Vert);
			GL20.glAttachShader(Program, Frag);
	        
	        GL20.glLinkProgram(Program);
	        //if (GL20.(Program, ARBShaderObjects.GL_OBJECT_LINK_STATUS_ARB) == GL11.GL_FALSE) {
	        //	System.err.println(getLogInfo(Program));
	        //	return;
	        //}
	        
	        ARBShaderObjects.glValidateProgramARB(Program);
	        if (ARBShaderObjects.glGetObjectParameteriARB(Program, ARBShaderObjects.GL_OBJECT_VALIDATE_STATUS_ARB) == GL11.GL_FALSE) {
	        	System.err.println(getLogInfo(Program));
	        	return;
	        }
	        Loc = ARBShaderObjects.glGetUniformLocationARB(Program, "loc");
	        //Rot = ARBShaderObjects.glGetUniformLocationARB(Program, "rot");
	        Scale = ARBShaderObjects.glGetUniformLocationARB(Program, "scale");
		}
	}
    private static String getLogInfo(int obj) {
        return ARBShaderObjects.glGetInfoLogARB(obj, ARBShaderObjects.glGetObjectParameteriARB(obj, ARBShaderObjects.GL_OBJECT_INFO_LOG_LENGTH_ARB));
    }
	public int genShader(int shaderType,String source)
	{
		int shader = 0;
    	try {
    		
	        shader = ARBShaderObjects.glCreateShaderObjectARB(shaderType);
	        
	        if(shader == 0)
	        	return 0;
	        
	        ARBShaderObjects.glShaderSourceARB(shader, source);
	        ARBShaderObjects.glCompileShaderARB(shader);
	        if (ARBShaderObjects.glGetObjectParameteriARB(shader, ARBShaderObjects.GL_OBJECT_COMPILE_STATUS_ARB) == GL11.GL_FALSE)
	            throw new RuntimeException("Error creating shader: ");
	        Work = true;
	        return shader;
    	}
    	catch(Exception exc) {
    		System.err.println(getLogInfo(shader));
    		System.err.println(source);
    		ARBShaderObjects.glDeleteObjectARB(shader);
    	}
        ARBShaderObjects.glUniform2fARB(Loc, 0, 0);
        ARBShaderObjects.glUniform1fARB(Rot, 0);
        ARBShaderObjects.glUniform2fARB(Scale, 0, 0);
		return shader;
	}
	public String GetFile(String file)
	{
		String all ="";
		try {
			all = new Scanner( new File(file) ).useDelimiter("\\A").next();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return all;
		
	}
	float x = 0;
	public void Bind()
	{
		ARBShaderObjects.glUseProgramObjectARB(Program);
	}
	public void Unbind()
	{
		ARBShaderObjects.glUseProgramObjectARB(0);
	}
	public void Destroy()
	{
		
	}
}
