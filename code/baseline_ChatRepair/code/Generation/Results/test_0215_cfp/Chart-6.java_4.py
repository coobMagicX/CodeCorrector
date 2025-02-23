public boolean equals(Object obj) {
    if (this == obj) {
        return true;  // reference equality check
    }
    if (!(obj instanceof ShapeList)) {
        return false;  // type check
    }
    ShapeList other = (ShapeList) obj;
    if (this.size() != other.size()) {
        return false;  // size check
    }
    for (int i = 0; i < this.size(); i++) {
        Shape shape1 = this.get(i);
        Shape shape2 = other.get(i);
        if (shape1 == null && shape2 != null || shape1 != null && !shape