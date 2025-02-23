public Object clone() throws CloneNotSupportedException {
    if (getItemCount() == 0) {
        // Directly use clone from superclass if the item count is zero
        return super.clone();
    } else {
        // Create a copy for non-empty series
        Object clone = createCopy(0, getItemCount() - 1