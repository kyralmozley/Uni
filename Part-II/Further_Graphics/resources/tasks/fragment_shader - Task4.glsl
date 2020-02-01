#version 330

uniform vec2 resolution;
uniform float currentTime;
uniform vec3 camPos;
uniform vec3 camDir;
uniform vec3 camUp;
uniform sampler2D tex;
uniform bool showStepDepth;

in vec3 pos;

out vec3 color;

#define PI 3.1415926535897932384626433832795
#define RENDER_DEPTH 800 //50
#define CLOSE_ENOUGH 0.00001

#define BACKGROUND -1
#define BALL 0
#define BASE 1

#define GRADIENT(pt, func) vec3( \
    func(vec3(pt.x + 0.0001, pt.y, pt.z)) - func(vec3(pt.x - 0.0001, pt.y, pt.z)), \
    func(vec3(pt.x, pt.y + 0.0001, pt.z)) - func(vec3(pt.x, pt.y - 0.0001, pt.z)), \
    func(vec3(pt.x, pt.y, pt.z + 0.0001)) - func(vec3(pt.x, pt.y, pt.z - 0.0001)))

const vec3 LIGHT_POS[] = vec3[](vec3(5, 18, 10));

/////////
const vec3 green = vec3(0.4, 1, 0.4);
const vec3 blue = vec3(0.4, 0.4, 1);
const vec3 black = vec3(0,0,0);
const vec3 white = vec3(1);

///////////////////////////////////////////////////////////////////////////////

vec3 getBackground(vec3 dir) {
    float u = 0.5 + atan(dir.z, -dir.x) / (2 * PI);
    float v = 0.5 - asin(dir.y) / PI;
    vec4 texColor = texture(tex, vec2(u, v));
    return texColor.rgb;
}

vec3 getRayDir() {
    vec3 xAxis = normalize(cross(camDir, camUp));
    return normalize(pos.x * (resolution.x / resolution.y) * xAxis + pos.y * camUp + 5 * camDir);
}

///////////////////////////////////////////////////////////////////////////////
// Shapes:

float sphere(vec3 pt) {
    return length(pt) - 1;
}

float cube(vec3 p) {
    vec3 d = abs(p) - vec3(1); //1=radius
    return min(max(d.x, max(d.y, d.z)), 0.0) + length(max(d, 0.0));
}

float sdSphere(vec3 pt, float s) {
    return length(pt) - s;
}

float sdPlane(vec3 p, vec4 n) {
    return dot(p, n.xyz) + n.w;
}

float sdfPlane(vec3 pt) {
    return (pt.y +1); //horizontal y=-1, so distance +1 in y
}

float torus(vec3 p, vec2 t) {
    vec2 q = vec2(length(p.xz) - t.x, p.y);
    return length(q) - t.y;
}

float smin(float a, float b) {
    float k = 0.2;
    float h = clamp(0.5 + 0.5 * (b-a) / k, 0, 1);
    return mix(b, a, h) - k * h * (1-h);
}

float fScene(vec3 pt) {
    float torus = torus(pt - vec3(0, 3, 0), vec2(3, 1)); //torus center (0,3,0) major = 3, minor =1
    return torus;
}

float f(vec3 pt) {
    float a = fScene(pt);
    float b = sdPlane(pt - vec3(0,-1,0), vec4(0,1,0,0));

    return min(a, b);
}


///////////////////////////////////////////////////////////////////////////////

vec3 getNormal(vec3 pt) {
    return normalize(GRADIENT(pt, f));
}


vec3 getColor(vec3 pt) {
    if(sdfPlane(pt) <= 0.0001) {
        float distance = fScene(pt);

        if(mod(distance, 5.0) > 4.75) {
            return black;
        }

        return mix(green, blue, mod(distance, 1));
    }

    return white;
}




float shade(vec3 eye, vec3 pt, vec3 n) {
    float val = 0;
    float ambient = 0.1;
    float diffuse = 1.0;
    float specular = 1.0;
    float specShine = 256;

    val += 0.1;  // Ambient

    for (int i = 0; i < LIGHT_POS.length(); i++) {
        vec3 l = normalize(LIGHT_POS[i] - pt);
        val += max(dot(n, l), 0);

        //specular
        vec3 N = normalize(n);
        vec3 L = l;
        vec3 V = normalize(eye - pt);
        vec3 R = normalize(reflect(-L, N));

        float LdotN = dot(L, N);
        float RdotV = dot(R, V);

        if(LdotN >=0 && RdotV >= 0) {
            val += LdotN * pow(RdotV, specShine);
        }
    }
    return val;
}

vec3 illuminate(vec3 camPos, vec3 rayDir, vec3 pt) {
    vec3 c, n;
    n = getNormal(pt);
    c = getColor(pt);
    return shade(camPos, pt, n) * c;
}


vec3 raymarch(vec3 camPos, vec3 rayDir) {
    int step = 0;
    float t = 0;

    for (float d = 1000; step < RENDER_DEPTH && abs(d) > CLOSE_ENOUGH; t += abs(d)) {
        d = f(camPos + t * rayDir);
        step++;
    }

    if (step == RENDER_DEPTH) {
        return getBackground(rayDir);
    } else if (showStepDepth) {
        return vec3(float(step) / RENDER_DEPTH);
    } else {
        return illuminate(camPos, rayDir, camPos + t * rayDir);
    }
}


///////////////////////////////////////////////////////////////////////////////

void main() {
    color = raymarch(camPos, getRayDir());
}