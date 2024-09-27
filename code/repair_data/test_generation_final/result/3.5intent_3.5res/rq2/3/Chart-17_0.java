public Object clone() throws CloneNotSupportedException {
    TimeSeries clone = (TimeSeries) super.clone();
    clone.data = (ArrayList<Double>) this.data.clone();
    return clone;
}