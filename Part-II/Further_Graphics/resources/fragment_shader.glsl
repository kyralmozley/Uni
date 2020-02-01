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

float cube(vec3 p, int r) {
    vec3 d = abs(p) - vec3(r); //1=radius
    return min(max(d.x, max(d.y, d.z)), 0.0) + length(max(d, 0.0));
}

float cylinder(vec3 p, vec2 h) {
    vec2 d = abs(vec2(length(p.xz), p.y)) - h;
    return min(max(d.x, d.y), 0.0) + length(max(d, 0.0));
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
    vec2 q = vec2(length(p.xy) - t.x, p.z);
    return length(q) - t.y;
}

float smin(float a, float b) {
    float k = 0.2;
    float h = clamp(0.5 + 0.5 * (b-a) / k, 0, 1);
    return mix(b, a, h) - k * h * (1-h);
}

float fScene(vec3 pt) {
    float cubes1, cubes2;
    float dome;

    vec3 pos;

    pos = vec3(mod(pt.x + 2, 4) - 2, pt.y, pt.z - 10);
    cubes1 = max(length(pt + vec3(0,0,-10)) - 9, cube(pos));

    pos = vec3(mod(pt.x + 2, 4) - 2, pt.y, pt.z + 10);
    cubes2 = max(length(pt + vec3(0,0,10)) - 9, cube(pos));

    float cubes = min(cubes1, cubes2);

    dome = cylinder(pt, vec2(5, 2));
    dome = min(dome, sdSphere(pt - vec3(0, 2, 0), 5));
    //make a door
    dome = max(dome, -cylinder(pt, vec2(4,2)));
    dome = max(dome, -cube(pt - vec3(4.5,0,0), 1));

    //add pillars
    dome = min(dome, cylinder(pt - vec3(-3.5, 0, -3.5), vec2(1, 4)));
    dome = min(dome, cylinder(pt - vec3(3.5, 0, -3.5), vec2(1, 4)));
    dome = min(dome, cylinder(pt - vec3(-3.5, 0, 3.5), vec2(1, 4)));
    dome = min(dome, cylinder(pt - vec3(3.5, 0, 3.5), vec2(1, 4)));

    dome = min(dome, sdSphere(pt -vec3(-3.5, 4, -3.5), 1));
    dome = min(dome, sdSphere(pt -vec3(3.5, 4, -3.5), 1));
    dome = min(dome, sdSphere(pt -vec3(-3.5, 4, 3.5), 1));
    dome = min(dome, sdSphere(pt -vec3(3.5, 4, 3.5), 1));


    return min(cubes, dome);
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

//taken from lecture notes
float shadow(vec3 pt) {
    float kd = 1;
    int step = 0;

    for(int i = 0; i < LIGHT_POS.length(); i++) {
        vec3 lightDir = normalize(LIGHT_POS[i] - pt);

        for(float t = 0.1; t < length(LIGHT_POS[i] - pt) && step < RENDER_DEPTH && kd > 0.001;) {
            float d = abs(fScene(pt + t * lightDir));
            if(d < 0.001) {
                kd = 0;
            } else {
                kd = min(kd, 16 * d / t);
            }

            t += d;
            step++;
        }

    }

    return kd;
}


float shade(vec3 eye, vec3 pt, vec3 n) {
    float val = 0;
    float ambient = 0.1;
    float diffuse = 1.0;
    float specular = 1.0;
    float specShine = 256;

    val += ambient;  // Ambient

    float kd = shadow(pt);

    for (int i = 0; i < LIGHT_POS.length(); i++) {
        vec3 l = normalize(LIGHT_POS[i] - pt);
        val += kd * max(dot(n, l), 0);

        //specular
        vec3 N = normalize(n);
        vec3 L = l;
        vec3 V = normalize(eye - pt);
        vec3 R = normalize(reflect(-L, N));

        float LdotN = dot(L, N);
        float RdotV = dot(R, V);

        if(LdotN >=0 && RdotV >= 0) {
            val += kd * LdotN * pow(RdotV, specShine);
        }
    }
    return val;
}

vec3 illuminate(vec3 camPos, vec3 rayDir, vec3 pt) {
    vec3 c, n;
    n = getNormal(pt);
    c = getColor(pt);
    return shade(camPos, pt, n) * dc;
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