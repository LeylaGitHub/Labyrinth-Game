#version 330 core

layout(location = 0) in vec3 position;
layout(location = 1) in vec2 text_coord;
layout(location = 2) in vec3 normal;

//uniforms
// translation object to world

uniform mat4 model_matrix;
uniform mat4 view_matrix;
uniform mat4 projection_matrix;

uniform vec3 lightPosition;
uniform vec3 direction;
uniform vec2 tcMultiplier;

out struct VertexData
{
    vec2 text_coord;
    vec3 normal;
    vec3 toCamera;
//    vec3 toPointLight;
    vec3 toSpotLight;
    vec3 spotLightDirection;
} vertexData;


void main(){
    vec4 pos = projection_matrix * view_matrix * model_matrix * vec4(position, 1.0f);
    gl_Position = pos;
    mat4 modelview = view_matrix * model_matrix;

    // texture //
    vertexData.text_coord = text_coord * tcMultiplier;

    //normal in camera space//
    mat4 normalMat = transpose(inverse(modelview));
    vertexData.normal = (normalMat * vec4(normal, 0.0f)).xyz;

    //light directions in camera space//
    vertexData.spotLightDirection = (view_matrix * vec4(direction, 0.0f)).xyz;
//    vertexData.toPointLight = (view_matrix * vec4(lightPosition, 1.0f) - modelview * pos).xyz;
    vertexData.toSpotLight = (view_matrix * vec4(lightPosition, 1.0f) - modelview * pos).xyz;

    // toCamera direction in camera space //
    vertexData.toCamera = -(modelview * pos).xyz; //modelview

}
