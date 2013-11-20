#version 420
in vec2 vert;
uniform vec2 loc;
uniform float rot;
uniform vec2 scale;
void main()
{
	float rotr = rot * (0.01);
	mat2 rotm = mat2(1,0,0,1);
	rotm[0][0] = cos(rotr);
	rotm[1][0] = sin(rotr);
	rotm[0][1] = -sin(rotr);
	rotm[1][1] = cos(rotr);
	gl_Position = vec4( ( ((vert*rotm) + loc))/scale, 0 , 1 );
}