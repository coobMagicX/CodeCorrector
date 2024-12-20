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
endIndex = Math.max(0, endIndex - 1);    // so this is last item BEFORE end
}
if ((startIndex == this.data.size()) || (endIndex < startIndex)) {
emptyRange = true;
}
if (emptyRange) {
TimeSeries copy = (TimeSeries) super.clone();
copy.data = new java.util.ArrayList();
return copy;
}
else {
int realStartIndex = Math.max(0, startIndex);
int realEndIndex = Math.min(this.data.size() - 1, endIndex);
TimeSeries copy = createCopy(realStartIndex, realEndIndex);
if (start.compareTo(copy.getRegularTimePeriod(0)) < 0) {
copy.data.add(0, null); // add dummy item before start
} else if (end.compareTo(copy.getRegularTimePeriod(copy.size() - 1)) > 0) {
copy.data.add(null); // add dummy item after end
}
return copy;
}

}