public Object clone() throws CloneNotSupportedException {
    TimeSeries ts = (TimeSeries) super.clone();
    ts.data = new ArrayList<>(this.data);
    return ts;
}