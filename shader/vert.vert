#version 150
in vec2 vert;
out vec3 colour;
uniform vec2 loc;
uniform vec2 colour;
uniform vec2 scale;
void main()
{
	gl_Position = vec4( ( ((vert) + loc))/scale, 0 , 1 );
}