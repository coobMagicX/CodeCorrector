public TimeSeries createCopy(RegularTimePeriod start, RegularTimePeriod end)
throws CloneNotSupportedException {

if (start == null) {
throw new IllegalArgumentException("Null 'start' argument.");
}
if (end == null) {
throw new IllegalArgumentException("Null 'end' argument.");
}
if (start.compareTo(end) > 0) {
throw new IllegalArgumentException(
"Requires start on or before end.");
}

boolean emptyRange = false;
int startIndex = getIndex(start);
if (startIndex < 0) {
startIndex = -(startIndex + 1);
if (startIndex == this.data.size()) {
emptyRange = true;  // start is after last data item
}
}
int endIndex = getIndex(end);
if (endIndex < 0) {             // end period is not in original series
endIndex = -(endIndex + 1); // this is first item AFTER end period
endIndex = Math.max(endIndex - 1, 0);    // so this is last item BEFORE end
}

if (endIndex < 0) {
emptyRange = true;
}
if (startIndex > endIndex) {
emptyRange = true; // start index is after end index
}
if (emptyRange) {
TimeSeries copy = (TimeSeries) super.clone();
copy.data = new java.util.ArrayList();
return copy;
} else {
ArrayList<TimeSeriesDataItem> dataCopy = new ArrayList<>();
for (int i = startIndex; i <= endIndex; i++) {
dataCopy.add(this.data.get(i));
}
TimeSeries copy = new TimeSeries(getName(), getDomain(), getRange(),
getClass());
copy.data = dataCopy;
return copy;
}

}