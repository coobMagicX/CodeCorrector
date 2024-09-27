private Object readResolve() {
    calculateHashCode();
    return this;
}