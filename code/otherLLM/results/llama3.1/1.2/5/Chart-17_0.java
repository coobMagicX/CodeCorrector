public Object clone() throws CloneNotSupportedException {
    TimeSeries clone = new TimeSeries();
    clone.setDomainDescription(getDomainDescription());
    clone.setRangeDescription(getRangeDescription());
    clone.setMaximumItemAge(getMaximumItemAge());
    clone.setMaximumItemCount(getMaximumItemCount());
    clone.setItemCount(getItemCount());

    for (int i = 0; i < getItemCount(); i++) {
        clone.addDataItem(getDataItem(i));
    }
    
    return clone;
}