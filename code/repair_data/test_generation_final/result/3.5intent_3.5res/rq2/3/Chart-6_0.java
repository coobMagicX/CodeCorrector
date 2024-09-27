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
    for (int i = 0; i < this.size(); i++) {
        if (!this.get(i).equals(other.get(i))) {
            return false;
        }
    }
    return true;
}

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
    for (int i = 0; i < this.size(); i++) {
        if (!this.get(i).equals(other.get(i))) {
            return false;
        }
    }
    return true;
}