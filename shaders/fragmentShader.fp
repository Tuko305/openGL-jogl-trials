#version 330 core
in vec2 pass_textureCoords;
out vec4 out_color;
uniform sampler2D textureSampler;
uniform vec3 lightColor;

void main(){
    out_color = texture(textureSampler, pass_textureCoords);
}