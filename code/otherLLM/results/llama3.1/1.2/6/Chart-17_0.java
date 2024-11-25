public Object clone() throws CloneNotSupportedException {
    TimeSeries timeSeriesClone = createCopy(0, getItemCount() - 1);
    timeSeriesClone.data = new java.util.ArrayList(data); // Deep copy the data
    return timeSeriesClone;
}