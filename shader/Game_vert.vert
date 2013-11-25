#version 150
attribute vec2 vert;
uniform vec2 loc;
uniform vec2 scale;
void main()
{
	gl_Position = vec4( ( ((vert) + loc))/scale, 0 , 1 );
}