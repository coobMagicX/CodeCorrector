public boolean equals(Object obj) {
    if (this == obj) {
        return true;
    }
    if (!(obj instanceof ShapeList)) {
        return false;
    }
    ShapeList other = (ShapeList) obj;
    if (this.shapes.size() != other.shapes.size()) {
        return false;
   