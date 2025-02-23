public Object clone() throws CloneNotSupportedException {
    if (getItemCount() == 0) {
        // Use an appropriate way to handle cloning of an empty series
        return super.clone();  // Or however your class should handle an empty clone
    } else {
        Object clone = createCopy(0, getItemCount() - 1);
        return