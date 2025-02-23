public Object clone() throws CloneNotSupportedException {
    if (getItemCount() == 0) {
        // Return a new instance or appropriately handle the case for zero items
        return super.clone();  // or suitably handle using another method
    }
    Object clone = createCopy(0, getItemCount() - 1);
    return clone;
}
