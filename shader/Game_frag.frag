#version 150
uniform vec3 colour;

void main()
{
	gl_FragColor = vec4(colour,1);//colourin;
}