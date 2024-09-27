public Object clone() throws CloneNotSupportedException {
    if (getItemCount() == 0) {
        throw new CloneNotSupportedException("Cannot clone an empty collection.");
    }
    Object clone = null;
    try {
        clone = createCopy(0, getItemCount() - 1);  // Assuming createCopy is meant to do a deep copy
    } catch (Exception e) {
        throw new CloneNotSupportedException("Failed to clone: " + e.getMessage());
    }
    return clone;
}

