package uk.ac.cam.cl.gfxintro.km687.tick1;

public class Sphere extends SceneObject {

	// Sphere coefficients
	private final double SPHERE_KD = 0.8;
	private final double SPHERE_KS = 1.2;
	private final double SPHERE_ALPHA = 10;
	private final double SPHERE_REFLECTIVITY = 0.3;

	// The world-space position of the sphere
	private Vector3 position;

	public Vector3 getPosition() {
		return position;
	}

	// The radius of the sphere in world units
	private double radius;

	public Sphere(Vector3 position, double radius, ColorRGB colour) {
		this.position = position;
		this.radius = radius;
		this.colour = colour;

		this.phong_kD = SPHERE_KD;
		this.phong_kS = SPHERE_KS;
		this.phong_alpha = SPHERE_ALPHA;
		this.reflectivity = SPHERE_REFLECTIVITY;
	}

	/*
	 * Calculate intersection of the sphere with the ray. If the ray starts inside the sphere,
	 * intersection with the surface is also found.
	 */
	public RaycastHit intersectionWith(Ray ray) {

		// Get ray parameters
		Vector3 O = ray.getOrigin();
		Vector3 D = ray.getDirection().normalised();
		
		// Get sphere parameters
		Vector3 C = position;
		double r = radius;

		// Calculate quadratic coefficients
		double a = D.dot(D);
		double b = 2 * D.dot(O.subtract(C));
		double c = (O.subtract(C)).dot(O.subtract(C)) - Math.pow(r, 2);
		
		// TODO: Determine if ray and sphere intersect - if not return an empty RaycastHit
        // TODO: If so, work out any point of intersection
        // TODO: Then return a RaycastHit that includes the object, ray distance, point, and normal vector
		RaycastHit empty = new RaycastHit();

		double discriminant = Math.pow(b, 2) - 4*a*c;

		if(discriminant < 0) return empty; //no real solution
        else {
            double solution1 = (-b + Math.sqrt(discriminant)) / (2*a);
            double solution2 = (-b - Math.sqrt(discriminant)) / (2*a);

            Vector3 position1 = ray.evaluateAt(solution1);
            Vector3 position2 = ray.evaluateAt(solution2);

            if(solution1 < 0 && solution2 < 0) return empty; //solution behind camera

            if(solution1 < 0) return new RaycastHit(this, solution2, position2, getNormalAt(position2)); //solution 1 behind camera, want 2
            if(solution2 < 0) return new RaycastHit(this, solution1, position1, getNormalAt(position1)); //solution 2 behind camera, want 1

            if(solution1 < solution2) {
                return new RaycastHit(this, solution1, position1, getNormalAt(position1));
            } else {
                return new RaycastHit(this, solution2, position2, getNormalAt(position2));
            }

		}
    }


	// Get normal to surface at position
	public Vector3 getNormalAt(Vector3 position) {
		return position.subtract(this.position).normalised();
	}
}
