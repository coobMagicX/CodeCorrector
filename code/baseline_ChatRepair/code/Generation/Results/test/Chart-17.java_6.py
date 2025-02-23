public Object clone() throws CloneNotSupportedException {
    if (getItemCount() == 0) {
        // Return a new instance or handle accordingly based on the cloning logic
        return super.clone(); // This assumes your class supports cloning otherwise
    } else {
        Object clone = createCopy(0, getItemCount() - 1);
        return clone;
    }
}
