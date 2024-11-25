public Object clone() throws CloneNotSupportedException {
    TimeSeries copy = (TimeSeries) super.clone();
    copy.data = new ArrayList();
    for (int i = 0; i < getItemCount(); i++) {
        copy.data.add(getDataItem(i).clone());
    }
    return copy;
}