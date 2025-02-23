public Object clone() throws CloneNotSupportedException {
    if (getItemCount() == 0) {
        return super.clone(); // Return a shallow copy if the series is empty
    }
    Object clone = createCopy(0, getItemCount() - 1);
    return clone;
}
