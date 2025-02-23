public boolean equals(Object obj) {
    if (this == obj) {
        return true;
    }
    if (!(obj instanceof ShapeList)) {
        return false;
    }
    ShapeList other = (ShapeList) obj;

    int size = this.size();
    if (size != other.size()) {
        return false;
    }

    for (int i = 0; i < size; i++) {
        Shape s1 = this.get(i);
        Shape s2 = other.get(i);
        if (s1 == null) {
            if (