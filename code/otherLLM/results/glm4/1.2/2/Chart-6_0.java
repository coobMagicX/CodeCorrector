public boolean equals(Object obj) {
    if (obj == this) {
        return true;
    }
    if (!(obj instanceof ShapeList)) {
        return false;
    }

    ShapeList other = (ShapeList) obj;

    // Check the length of both lists to handle serialization/deserialization
    if (this.size() != other.size()) {
        return false;
    }

    for (int i = 0; i < this.size(); i++) {
        // If shapes are null, check explicitly or handle custom behaviors
        if (this.get(i) == null ? other.get(i) != null : !this.get(i).equals(other.get(i))) {
            return false;
        }
    }

    return true;
}