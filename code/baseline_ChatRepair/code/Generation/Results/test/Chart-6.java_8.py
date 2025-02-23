public boolean equals(Object obj) {
    if (this == obj) {
        return true;
    }
    if (obj == null || getClass() != obj.getClass()) {
        return false;
    }
    ShapeList other = (ShapeList) obj;
    if (this.size() != other.size()) {
        return false;
    }
    for (int i = 0; i < this.size(); i++) {
        if (!this.get