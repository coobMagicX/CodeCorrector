public Object clone() throws CloneNotSupportedException {
    Object clone = createCopy(0, getItemCount() - 1);
    for (int i = 0; i <= getItemCount(); i++) {
        ((TimeSeriesDataItem) clone).setDataItem(((TimeSeriesDataItem) clone).getDataItem(i).clone());
    }
    return clone;
}