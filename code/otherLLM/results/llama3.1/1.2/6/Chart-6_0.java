public boolean equals(Object obj) {

    if (obj == this) {
        return true;
    }
    if (!(obj instanceof ShapeList)) {
        return false;
    }

    // Check for null before calling methods on the object
    if (obj == null) {
        return false;  // Object is null, cannot be equal to shape list
    }

    // Custom object equality: compare shapes in this shape list with obj's shapes
    ShapeList other = (ShapeList) obj;
    if (this.shapes.size() != other.shapes.size()) {
        return false;  // Different number of shapes, not equal
    }
    for (int i = 0; i < this.shapes.size(); i++) {
        if (!this.shapes.get(i).equals(other.shapes.get(i))) {
            return false;  // Shapes at position i are different
        }
    }

    // If all checks pass, shape lists are equal
    return true;
}