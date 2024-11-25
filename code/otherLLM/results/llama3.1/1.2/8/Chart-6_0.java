public boolean equals(Object obj) {

    if (obj == this) {
        return true;
    }
    if (!(obj instanceof ShapeList)) {
        return false;
    }

    // Check if the other object is null before trying to compare it.
    if (obj == null) {
        return false;
    }

    // Compare the sizes of the ShapeLists
    int count1 = size();
    int count2 = ((ShapeList) obj).size();

    if (count1 != count2) {
        return false;
    }

    // Compare the shapes at each position in the lists
    for (int i = 0; i < count1; i++) {
        Shape shape1 = getShape(i);
        Shape shape2 = ((ShapeList) obj).getShape(i);

        if (shape1 == null && shape2 != null) {
            return false;
        }
        if (shape1 != null && shape2 == null) {
            return false;
        }

        // If both shapes are not null, compare them
        if (shape1 != null && shape2 != null) {
            if (!SerialUtilities.equals(shape1, shape2)) {
                return false;
            }
        }
    }

    return true;

}