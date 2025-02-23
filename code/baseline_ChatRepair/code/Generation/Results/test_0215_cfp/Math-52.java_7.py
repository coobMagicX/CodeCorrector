// Update starting after the preliminary computation where q1, q2, q3 are computed:

// Original calculation (for reference):
// q1 = inv * k.getX();
// q2 = inv * k.getY();
// q3 = inv * k.getZ();

double normK = k.getNorm();
if (normK == 0) {
    q0 = 1.0; // indicates no rotation if the axis vector k is zero
    q1 = 0.0;
    q2 = 0.0;
    q3 = 0.0;
} else {
    // Normalize k, and calculate q1, q2, q3 safely
    q1 = inv * k.getX();
    q2 = inv * k.getY();
    q3 = inv * k.getZ();

    // New computation for the scalar part respecting the singular case:
    double qx = uRef.getY() * q3 - uRef.getZ() * q2;
    double qy = uRef.getZ() * q1 - uRef.getX() * q3;
    double qz = uRef.getX() * q2 - uRef.getY() * q1;

    double dotKvRef = vRef.dotProduct(new Vector3D(qx, qy, qz));

    if (dotKvRef == 0) {
        q0 = 1.0; // indicates possible alignment or no effective rotation
    } else {
        // Calculate q0 using updated vector components
        double normK2 = qx * qx + qy * qy + qz * qz;
        if (normK2 == 0) {
            q0 = 1.0;
        } else {
            q0 = dotKvRef / (2 * normK2);
        }
    }
}

// Ensure q0 is positive for standard quaternion form
if (q0 < 0) {
    q0 = -q0;
    q1 = -q1;
    q2 = -q2;
    q3 = -q3;
}
