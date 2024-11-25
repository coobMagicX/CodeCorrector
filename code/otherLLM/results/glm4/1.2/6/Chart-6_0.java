public boolean equals(Object obj) {
    // Check if the object is an instance of ShapeList and is not null
    if (obj == this || !(obj instanceof ShapeList)) {
        return false;
    }

    // Cast to ShapeList to compare its contents
    ShapeList other = (ShapeList) obj;

    // Check if both lists have the same size
    if (this.size() != other.size()) {
        return false;
    }

    // Compare each element of the list for equality
    for (int i = 0; i < this.size(); i++) {
        if (!this.get(i).equals(other.get(i))) {
            return false;
        }
    }

    // If all checks pass, the objects are equal
    return true;
}