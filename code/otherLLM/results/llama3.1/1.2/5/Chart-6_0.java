public boolean equals(Object obj) {

    if (obj == null || !(obj instanceof ShapeList)) {
        return false;
    }
    if (this.shapes == null && ((ShapeList)obj).shapes == null) {
        return true;
    }
    if (this.shapes != null && ((ShapeList)obj).shapes != null) {
        return this.shapes.equals(((ShapeList)obj).shapes);
    }
    return false;

}