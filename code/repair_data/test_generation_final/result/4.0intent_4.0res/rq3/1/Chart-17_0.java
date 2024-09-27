public Object clone() throws CloneNotSupportedException {
    try {
        Object clone = createCopy(0, getItemCount() - 1);
        return clone;
    } catch (Exception e) {
        throw new CloneNotSupportedException("Failed to clone object: " + e.getMessage());
    }
}