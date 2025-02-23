public boolean equals(Object obj) {
    if (this == obj) {
        return true;
    }
    if (!(obj instanceof ShapeList)) {
        return false;
    }
    ShapeList other = (ShapeList) obj;
    if (this.size() != other.size()) {
        return false;
    }
    for (int i = 0; i < this.size(); i++) {
        Shape shape1 = this.get(i);
       