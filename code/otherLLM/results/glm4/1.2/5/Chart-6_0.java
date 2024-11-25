public boolean equals(Object obj) {
    if (obj == null || !(obj instanceof ShapeList)) {
        return false;
    }

    ShapeList other = (ShapeList) obj;

    // Check for equality of the list size
    if (this.size() != other.size()) {
        return false;
    }

    // Iterate through both lists and compare shapes
    for (int i = 0; i < this.size(); i++) {
        Shape shape1 = this.get(i);
        Shape shape2 = other.get(i);

        // Check if either shape is null, which would be different
        if ((shape1 == null && shape2 != null) || (shape1 != null && !shape1.equals(shape2))) {
            return false;
        }
    }

    return true;
}