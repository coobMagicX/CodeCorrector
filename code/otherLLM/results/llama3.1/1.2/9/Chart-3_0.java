public TimeSeries createCopy(int start, int end)
throws CloneNotSupportedException {
    if (start < 0) {
        throw new IllegalArgumentException("Requires start >= 0.");
    }
    if (end < start) {
        throw new IllegalArgumentException("Requires start <= end.");
    }
    TimeSeries copy = (TimeSeries) super.clone();
    copy.data = new java.util.ArrayList();
    if (this.data.size() > 0) {
        for (int index = start; index <= end; index++) {
            TimeSeriesDataItem item
                    = (TimeSeriesDataItem) this.data.get(index);
            TimeSeriesDataItem clone = (TimeSeriesDataItem) item.clone();
            try {
                copy.add(clone);
                if (copy.data.size() > 0 && index == end) {
                    double maxY = getMaxY(copy.data);
                    double minY = getMinY(copy.data);
                    copy.maxY = maxY;
                    copy.minY = minY;
                }
            }
            catch (SeriesException e) {
                e.printStackTrace();
            }
        }
    }
    return copy;
}

private double getMaxY(List<TimeSeriesDataItem> dataItems) {
    if (dataItems.isEmpty()) {
        return 0.0;
    }
    double maxY = dataItems.get(0).yValue;
    for (int i = 1; i < dataItems.size(); i++) {
        TimeSeriesDataItem item = dataItems.get(i);
        if (item.yValue > maxY) {
            maxY = item.yValue;
        }
    }
    return maxY;
}

private double getMinY(List<TimeSeriesDataItem> dataItems) {
    if (dataItems.isEmpty()) {
        return 0.0;
    }
    double minY = dataItems.get(0).yValue;
    for (int i = 1; i < dataItems.size(); i++) {
        TimeSeriesDataItem item = dataItems.get(i);
        if (item.yValue < minY) {
            minY = item.yValue;
        }
    }
    return minY;
}