public boolean equals(Object obj) {
    if (obj == this) {
        return true;
    }
    if (!(obj instanceof ShapeList)) {
        return false;
    }
    ShapeList other = (ShapeList) obj; // Cast obj to ShapeList type
    return super.equals(other); // Call the equals method on super class with the correctly cast object
}