public boolean equals(Object obj) {

    if (obj == this) {
        return true;
    }
    if (!(obj instanceof ShapeList)) {
        return false;
    }

    // Check for null shapes in the other object's list
    if (((ShapeList)obj).getShapes() == null) {
        return getShapes() == null;
    }

    // Check equality of shapes within the lists
    Set<Shape> thisSet = new HashSet<>();
    for (Shape shape : getShapes()) {
        thisSet.add(shape);
    }
    Set<Shape> otherSet = new HashSet<>();
    for (Shape shape : ((ShapeList)obj).getShapes()) {
        otherSet.add(shape);
    }

    return thisSet.equals(otherSet);

}