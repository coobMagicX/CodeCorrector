public Object clone() throws CloneNotSupportedException {
    Object clone = createCopy(0, getItemCount() - 1);
    if (clone instanceof TimeSeries) {
        ((TimeSeries) clone).setData(new ArrayList<>());
    }
    return clone;
}