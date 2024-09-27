public Object clone() throws CloneNotSupportedException {
    TimeSeries clone = (TimeSeries) super.clone();
    clone.data = (List) ((ArrayList) this.data).clone();
    return clone;
}