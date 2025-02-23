k = new Vector3D(uRef.getY() * q3 - uRef.getZ() * q2,
                uRef.getZ() * q1 - uRef.getX() * q3,
                uRef.getX() * q2 - uRef.getY() * q1);
q0 = vRef.dotProduct(k) / (2 * k.getNormSq());
