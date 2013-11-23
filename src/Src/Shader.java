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
	        GL20.glValidateProgram(Program);
	        if (GL20.glGetProgrami(Program, GL20.GL_VALIDATE_STATUS) == GL11.GL_FALSE) {
	        	System.err.println(GL20.glGetProgramInfoLog(Program,GL20.glGetProgrami(Program,GL20.GL_INFO_LOG_LENGTH)));
	        	return;
	        }
	        Loc = GL20.glGetUniformLocation(Program, "loc");
	        //Rot = ARBShaderObjects.glGetUniformLocationARB(Program, "rot");
	        Scale = GL20.glGetUniformLocation(Program, "scale");
		}
	}
	public int genShader(int shaderType,String source)
	{
		int shader = 0;
    	try {
    		
	        shader = GL20.glCreateShader(shaderType);
	        
	        if(shader == 0)
	        	return 0;
	        
	        GL20.glShaderSource(shader, source);
	        GL20.glCompileShader(shader);
	        if (GL20.glGetShaderi(shader, GL20.GL_COMPILE_STATUS) == GL11.GL_FALSE)
	            throw new RuntimeException("Error creating shader: ");
	        Work = true;
	        return shader;
    	}
    	catch(Exception exc) {
    		System.err.println(GL20.glGetShaderInfoLog(shader,GL20.glGetShaderi(shader,GL20.GL_INFO_LOG_LENGTH)));
    		System.err.println(source);
    		GL20.glDeleteShader(shader);
    	}
    	GL20.glUniform2f(Loc, 0, 0);
    	GL20.glUniform1f(Rot, 0);
        GL20.glUniform2f(Scale, 0, 0);
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
		GL20.glUseProgram(Program);
	}
	public void Unbind()
	{
		GL20.glUseProgram(0);
	}
	public void Destroy()
	{
		
	}
}
