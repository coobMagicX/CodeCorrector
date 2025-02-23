public Object clone() throws CloneNotSupportedException {
    if (getItemCount() == 0) {
        // Assuming there's some kind of no-arg constructor or equivalent method to handle empty