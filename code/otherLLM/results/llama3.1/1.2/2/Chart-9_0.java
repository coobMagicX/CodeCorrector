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
emptyRange = true;  
}
}
int endIndex = getIndex(end);
if (endIndex < 0) {             // end period is not in original series
endIndex = -(endIndex + 1); 
}

// Fix: Check if startIndex and endIndex are both negative before returning a copy with empty data
if (startIndex >= 0 && endIndex >= 0) {
return createCopy(startIndex, endIndex);
} else if (emptyRange) {
TimeSeries copy = (TimeSeries) super.clone();
copy.data = new java.util.ArrayList();
return copy;
}
// Fix: Call createCopy method with indices if start and end dates are within the original series
else {
RegularTimePeriod startDate = getStart(startIndex);
RegularTimePeriod endDate = getEnd(endIndex);
if (startDate.compareTo(endDate) <= 0) {
return createCopy(startDate, endDate);
} else {
throw new IllegalArgumentException("Invalid time range.");
}
}

}