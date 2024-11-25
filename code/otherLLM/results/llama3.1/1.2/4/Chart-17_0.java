public Object clone() throws CloneNotSupportedException {
    TimeSeries copy = createCopy(0, getItemCount() - 1);
    return copy;
}