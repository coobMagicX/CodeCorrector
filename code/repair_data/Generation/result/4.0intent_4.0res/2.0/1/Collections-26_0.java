private Object readResolve() {
    calculateHashCode(keys); // Recalculate the hash code based on the current state of 'keys'
    return this;
}