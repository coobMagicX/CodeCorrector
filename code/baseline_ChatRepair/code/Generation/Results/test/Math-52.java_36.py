q0 = 0.0;
if (k.getNormSq() != 0) {  // Ensure k is not a zero vector
    q0 = vRef.dotProduct(k) / (2 * k.getNormSq());
} else {
    // When k is a zero vector, setting q0 directly as neutral element of rotation
    q0 = 1.0;
}
