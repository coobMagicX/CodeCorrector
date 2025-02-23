public Object clone() throws CloneNotSupportedException {
    if (getItemCount() == 0) {
        // Directly use clone from superclass if no items are present
        return super.clone();
    } else {
        // Create a copy using valid start and end indices when items are present
        Object clone = createCopy(0, getItemCount() - 1);
        return clone;
    }
