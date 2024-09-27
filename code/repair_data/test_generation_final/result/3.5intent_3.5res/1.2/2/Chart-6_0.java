public boolean equals(Object obj) {
    if (obj == this) {
        return true;
    }
    if (!(obj instanceof ShapeList)) {
        return false;
    }

    ShapeList other = (ShapeList) obj;
    if (this.size() != other.size()) {
        return false;
    }

    Iterator<Shape> thisIterator = this.iterator();
    Iterator<Shape> otherIterator = other.iterator();

    while (thisIterator.hasNext() && otherIterator.hasNext()) {
        Shape thisShape = thisIterator.next();
        Shape otherShape = otherIterator.next();

        if (thisShape == null) {
            if (otherShape != null) {
                return false;
            }
        } else {
            if (!thisShape.equals(otherShape)) {
                return false;
            }
        }
    }

    return true;
}