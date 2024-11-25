public boolean equals(Object obj) {
    if (obj == this) {
        return true;
    }
    if (!(obj instanceof ShapeList)) {
        return false;
    }

    ShapeList other = (ShapeList) obj;

    // Assuming getShapes() returns a List<Shape>
    if (this.getShapes().size() != other.getShapes().size()) {
        return false;
    }

    for (int i = 0; i < this.getShapes().size(); i++) {
        if (!this.getShapes().get(i).equals(other.getShapes().get(i))) {
            return false;
        }
    }
    
    return true;
}