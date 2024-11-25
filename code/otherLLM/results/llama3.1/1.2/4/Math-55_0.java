public static Vector3D crossProduct(final Vector3D v1, final Vector3D v2) {
    // rescale both vectors without losing precision,
    // to ensure their norm are the same order of magnitude

    // we reduce cancellation errors by preconditioning,
    // we replace v1 by v3 = v1 - rho * v2 with rho chosen in order to compute
    // v3 without loss of precision. See Kahan lecture
    // "Computing Cross-Products and Rotations in 2- and 3-Dimensional Euclidean Spaces"
    // available at http://www.cs.berkeley.edu/~wkahan/MathH110/Cross.pdf

    double rho = (v1.dot(v2)) / ((v2.dot(v2)) + 0.000001); // add a small value to avoid division by zero
    
    Vector3D v3 = v1.subtract(rho * v2);
    
    return new Vector3D(
        v3.y * v2.z - v3.z * v2.y,
        v3.z * v2.x - v3.x * v2.z,
        v3.x * v2.y - v3.y * v2.x
    );
}

// added a small value to avoid division by zero in the computation of rho