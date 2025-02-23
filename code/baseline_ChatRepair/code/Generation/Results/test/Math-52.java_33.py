public Rotation(Vector3D u1, Vector3D u2, Vector3D v1, Vector3D v2) {

    // Your previous code here...

    // compute the scalar part
    k = new Vector3D(uRef.getY() * q3 - uRef.getZ() * q2,
                     uRef.getZ() * q1 - uRef.getX() * q3,
                     uRef.getX() * q2 - uRef.getY() * q1);
    double kNormSq = k.getNormSq();
    
    // Handle the edge case where vector square norm is near zero, which can make q0 go to NaN.
    if (Math.abs(kNormSq) < 1e-15) {
        // In an edge case where the vectors are nearly parallel, q0 might approach an indeterminate form.
        // One simple approach might be to set it as 0.0 or 1.0, which represents a "no rotation"
        q0 = 1.0; // Since q1, q2, q3 are basically vectors close to zero, this unity scalar part defines no rotation
    } else {
        q0 = vRef.dotProduct(k) / (2 * kNormSq);
    }
}
