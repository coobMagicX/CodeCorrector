public boolean equals(Object obj) {

    if (obj == this) {
        return true;
    }
    if (!(obj instanceof ShapeList)) {
        return false;
    }

    // Check for null values and serialized data
    if (obj == null || !(obj instanceof Serializable)) {
        return false;
    }

    ShapeList other = (ShapeList) obj;

    // Compare shape lists
    if (!super.equals(obj)) {
        return false;
    }
    
    // Additional check to ensure shapes are equal
    for (int i = 0; i < this.shapes.length; i++) {
        if (this.shapes[i] != other.shapes[i]) {
            return false;
        }
    }

    return true;
}