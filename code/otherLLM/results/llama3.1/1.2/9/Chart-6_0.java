public boolean equals(Object obj) {

    if (obj == this) {  // Handle null values, as per repair strategy 1
        return true;
    }
    if (!(obj instanceof ShapeList)) {
        return false;
    }

    // Properly override equals method for ShapeList class, as per repair strategy 2
    ShapeList other = (ShapeList)obj;
    if (shapes.size() != other.shapes.size()) {  // Compare shape lists by size
        return false;
    }
    for (int i = 0; i < shapes.size(); i++) {
        if (!shapes.get(i).equals(other.shapes.get(i))) {
            return false;
        }
    }

    return super.equals(obj);  // Call superclass's equals method

}