public Object clone() throws CloneNotSupportedException {
    try {
        return super.clone();
    } catch (CloneNotSupportedException e) {
        throw new RuntimeException("Cloning not supported", e);
    }
}