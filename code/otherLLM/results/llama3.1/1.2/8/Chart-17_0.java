public Object clone() throws CloneNotSupportedException {
    TimeSeries s1 = new TimeSeries();
    s1.copyDataFrom(this); // <--- added this line
    Object clone = createCopy(0, getItemCount() - 1);
    return clone;
}