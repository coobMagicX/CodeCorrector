public boolean equals(Object obj) {
    if (this == obj) {
        return true;
    }
    if (!(obj instanceof ShapeList)) {
        return false;
    }
    ShapeList other = (ShapeList) obj;
    // Assuming `this.shapes` and `other.shapes` are the lists containing shapes.
    if (this.shapes == null) {
        return other.shapes == null;
    }
    return this.shapes.equals(other.shapes);
}
