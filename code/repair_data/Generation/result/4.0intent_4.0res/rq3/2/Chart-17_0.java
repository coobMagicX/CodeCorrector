public Object clone() throws CloneNotSupportedException {
    // Assuming createCopy(int start, int end) creates a copy of a portion of a collection
    // and getItemCount() returns the number of items in the collection
    // This implementation assumes that we want to clone the entire collection.
    Object clone = createCopy(0, getItemCount() - 1);
    return clone;
}