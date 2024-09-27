public Object clone() throws CloneNotSupportedException {
    TimeSeries clone = (TimeSeries) super.clone();
    clone.data = new ArrayList<>(this.data);
    return clone;
}