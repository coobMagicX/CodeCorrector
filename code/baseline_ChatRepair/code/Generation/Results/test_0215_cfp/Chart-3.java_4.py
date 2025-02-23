import java.util.ArrayList;

public TimeSeries createCopy(int start, int end) throws CloneNotSupportedException {
    if (start < 0) {
        throw new IllegalArgumentException("Requires start >= 0.");
    }
    if (end < start) {
        throw new IllegalArgumentException("Requires start <= end.");
    }
    if (end >= this.data.size()) {
        throw new IllegalArgumentException("End index exceeds data size.");
    }

    TimeSeries copy = (TimeSeries) super.clone();
    copy.data = new ArrayList();

    for (int index = start; index <= end; index++) {
        TimeSeriesDataItem item = (TimeSeriesDataItem) this.data.get(index);
        TimeSeriesDataItem clone = (TimeSeriesDataItem) item.clone();
        try {
            copy.add(clone);
        } catch (SeriesException e) {
            System.err.println("Error adding item to series: " + e.getMessage());
            e.printStackTrace();
        }
    }

    return copy;
}
