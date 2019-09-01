#version 140


in vec3 frag_normal;	    // fragment normal in world space
in vec2 frag_texcoord;		// fragment texture coordinates in texture space

out vec3 pixel_colour;

uniform sampler2D tex;  // 2D texture sampler

// Tone mapping and display encoding combined
vec3 tonemap( vec3 linearRGB )
{
    float L_white = 0.7; // Controls the brightness of the image

    float inverseGamma = 1./2.2;
    return pow( linearRGB/L_white, vec3(inverseGamma) ); // Display encoding - a gamma
}

void main()
{
	const vec3 I_a = vec3(0.01, 0.01, 0.01);       // Ambient light intensity (and colour)

	const float k_d = 0.6;                      // Diffuse light factor
   // vec3 C_diff = vec3(0.560, 0.525, 0.478);    // Diffuse material colour (TODO: replace with texture)
    vec4 texcolour = texture(tex, frag_texcoord);
    vec3 C_diff = texcolour.rgb;

	const vec3 I = vec3(0.941, 0.968, 1);   // Light intensity (and colour)
	vec3 L = normalize(vec3(2, 1.5, -0.5)); // The light direction as a unit vector
	vec3 N = frag_normal;                   // Normal in world coordinates


	// TODO: Calculate colour using the illumination model
    //vec3 linear_colour = vec3(0.0, 0.0, 0.0); // TODO: replace this line
    //vec3 linear_colour = abs(frag_normal);
    
    vec3 Ambient = C_diff * I_a;
    vec3 Diffuse = C_diff * k_d * I * max(0, dot(N, L));
    vec3 RGB_colour = Ambient + Diffuse;
    
    pixel_colour = tonemap( RGB_colour );
    //pixel_colour = N;
}
