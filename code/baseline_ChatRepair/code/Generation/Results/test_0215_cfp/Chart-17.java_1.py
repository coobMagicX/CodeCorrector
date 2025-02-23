public Object clone() throws CloneNotSupportedException {
    Object clone;
    int itemCount = getItemCount();
    if (itemCount > 0) {
        clone = createCopy(0, itemCount - 1);
    } else {
        // Perform a deep clone or other appropriate operation for empty datasets
        clone = super.clone(); // Considering a simplified deep clone
    }
    return clone;
}
