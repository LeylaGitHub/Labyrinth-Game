#version 330 core

uniform sampler2D diff;
uniform sampler2D emit;
uniform sampler2D spec;
uniform vec3 lightColor;
uniform float shininess;
uniform float innerAngle;
uniform float outerAngle;
uniform vec3 color_ground;

//input from vertex shader
in struct VertexData
{
    vec2 text_coord;
    vec3 normal;
    vec3 toCamera;
//    vec3 toPointLight;
    vec3 toSpotLight;
    vec3 spotLightDirection;
} vertexData;


//fragment shader output
out vec4 color;

// normalized vectors //
vec3 normalizedNormal = normalize(vertexData.normal);
//vec3 normalizedToPointLight = normalize(vertexData.toPointLight);
vec3 normalizedToSpotLight = normalize(vertexData.toSpotLight);
vec3 normalizedToCamera = normalize(vertexData.toCamera);
vec3 reflectVec = normalize(reflect(-vertexData.toSpotLight, normalizedNormal));

// returns value in gamma / sRGB space
vec3 gamma ( vec3 C_linear ) {
    return ( pow( C_linear, vec3 (1 / 1.5 ) ) );
}
// returns value in linear space
vec3 invgamma ( vec3 C_gamma ) {
    return (pow( C_gamma, vec3 ( 1.5 ) ) );
}

void main(){
    // lightIntensity for SpotLight //
    float lightIntensity;
    float theta = dot(-normalize(vertexData.toSpotLight), vertexData.spotLightDirection);
    if (theta < cos(innerAngle)) {
        lightIntensity = 1.0f;
    }else if (theta > cos(outerAngle)){
        lightIntensity = clamp((theta - cos(outerAngle)) / (cos(innerAngle) - cos(outerAngle)), 0.0f, 1.0f); //clamp --> lightIntensity has to be between 0 and 1
    }else{
        lightIntensity = 0.0f;
    }

    vec3 diffColor = texture(diff, vertexData.text_coord).rgb;
    vec3 specColor = texture(spec, vertexData.text_coord).rgb;
    vec3 emitColor = texture(emit, vertexData.text_coord).rgb;

    //    //diffuse component//
    //    float cosa = max(0.0, dot(normalizedNormal,normalizedToLight));
    //    vec3 diffuseTerm = diffColor * lightColor * cosa;

    // specular component //
    float cosb = max(0.0, dot(normalizedToCamera,reflectVec));
    float cosbk = pow(cosb, shininess);
    vec3 specularTerm = specColor * lightColor * cosbk;

//    // lightAttenuation for PointLight
//    float pointLightAttenuation = clamp(  1.0f / pow(length(vertexData.toPointLight), 2.0f ), 0.0f, 1.0f );

//    vec3 pointResult = (diffColor * lightColor * max(0.0, dot(normalizedNormal,normalizedToPointLight)) + specularTerm) * lightColor * pointLightAttenuation;
    vec3 spotResult = (diffColor * lightColor * max(0.0, dot(normalizedNormal,normalizedToSpotLight)) + specularTerm) * lightColor * lightIntensity;

    // color //
    vec3 groundColor = color_ground * emitColor;
    vec3 result = emitColor + pointResult + spotResult;
    result = invgamma(result);
    color = vec4(result + groundColor, 1.0f);

    //    // color test //
    //    color = vec4(vec3(emitColor), 1.0f);
}