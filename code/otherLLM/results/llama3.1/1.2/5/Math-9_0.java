public class Line {
    // existing methods and fields...

    public Vector3D subtract(Vector3D v1, Vector3D v2) {
        return new Vector3D(v1.x - v2.x, v1.y - v2.y, v1.z - v2.z);
    }

    public Vector3D negate(Vector3D v) {
        return new Vector3D(-v.x, -v.y, -v.z);
    }
}

public class TestLine {
    // existing test cases...

    public void testRevert() {
        Line line = new Line(new Vector3D(1, 2, 3), direction); // assuming 'direction' is defined
        Line reverted = line.revert();
        
        assertAlmostEqual(reverted.direction, negate(direction)); // utilize the negate method
        
        System.out.println("Test pass: revert() method correctly inverts the direction of a Line object.");
    }

    public void assertAlmostEqual(Vector3D v1, Vector3D v2) {
        double precision = 0.000001;
        if (Math.abs(v1.x - v2.x) > precision ||
            Math.abs(v1.y - v2.y) > precision ||
            Math.abs(v1.z - v2.z) > precision) {
            throw new AssertionError("Vectors are not almost equal");
        }
    }
}