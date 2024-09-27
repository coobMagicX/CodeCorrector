public static Vector3D crossProduct(final Vector3D v1, final Vector3D v2) {
    // Normalize vectors to reduce the risk of cancellation errors
    Vector3D normV1 = v1.normalize();
    Vector3D normV2 = v2.normalize();

    // Compute cross product from normalized vectors
    return new Vector3D(
        normV1.getY() * normV2.getZ() - normV1.getZ() * normV2.getY(),
        normV1.getZ() * normV2.getX() - normV1.getX() * normV2.getZ(),
        normV1.getX() * normV2.getY() - normV1.getY() * normV2.getX()
    );
}