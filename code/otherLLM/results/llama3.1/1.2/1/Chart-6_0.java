public boolean equals(Object obj) {

    if (obj == this) {
        return true;
    }
    if (!(obj instanceof ShapeList)) {
        return false;
    }
    
    // Add a check for null values in ShapeList
    if (this.shapes == null && ((ShapeList)obj).shapes == null) {
        return true; // Both lists are empty, so they're equal
    }

    // Compare shapes in both lists
    int length = this.shapes.length;
    Shape[] shapesObj = ((ShapeList)obj).shapes;

    for (int i = 0; i < length; i++) {
        if (this.shapes[i] != shapesObj[i]) {
            return false;
        }
    }

    // If all shapes are equal, return true
    return true;
}