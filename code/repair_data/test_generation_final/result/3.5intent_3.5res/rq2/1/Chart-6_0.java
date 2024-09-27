public boolean equals(Object obj) {
    if (this == obj) {
        return true;
    }
    if (obj == null || getClass() != obj.getClass()) {
        return false;
    }
    ShapeList shapeList = (ShapeList) obj;
    // Compare the shapes in the list
    return super.equals(obj) && this.shapes.equals(shapeList.shapes);
}