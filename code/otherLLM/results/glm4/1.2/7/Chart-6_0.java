public boolean equals(Object obj) {

    if (obj == this) {
        return true;
    }
    if (!(obj instanceof ShapeList)) {
        return false;
    }
    
    ShapeList other = (ShapeList) obj;

    // Compare the contents of the list
    if (this.size() != other.size()) {
        return false;
    }

    // Since equals() in List doesn't compare elements properly after deserialization,
    // we need to manually compare each element.
    for (int i = 0; i < this.size(); i++) {
        Shape thisShape = this.getShape(i);
        Shape otherShape = other.getShape(i);

        if (thisShape == null && otherShape != null) {
            return false;
        }
        if (thisShape != null && !thisShape.equals(otherShape)) {
            return false;
        }
    }

    // Also call super.equals to compare any fields not included in the list
    return super.equals(obj);
}